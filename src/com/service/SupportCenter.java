package com.service;

import com.event.EventBus;
import com.event.LoggingEventListener;
import com.model.Agent;
import com.model.Contact;
import com.model.Session;
import com.strategy.RoutingStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SupportCenter {
    private static final Logger LOGGER = Logger.getLogger(SupportCenter.class.getName());

    private final List<Agent> agents = new CopyOnWriteArrayList<>();
    private final Map<String, Agent> customerLastAgentMap = new ConcurrentHashMap<>();

    // processQueue() bastan sona bu kilit altinda calisir; iki thread ayni
    // anda addContact/processQueue cagirirsa bile ayni musterinin iki kez
    // islenmesi ya da kuyrugun tutarsiz okunmasi engellenir.
    private final ReentrantLock queueProcessingLock = new ReentrantLock();

    private final QueueService queueService;
    private final RoutingService routingService;
    private final SessionLifecycleService sessionLifecycleService;
    private final AgentLifecycleService agentLifecycleService;
    private final EventBus eventBus;

    /** Geriye donuk uyumluluk icin: varsayilan kuyruk asiri yuklenme esigi 5. */
    public SupportCenter(RoutingStrategy routingStrategy) {
        this(routingStrategy, 5);
    }

    public SupportCenter(RoutingStrategy routingStrategy, int queueOverloadThreshold) {
        this.eventBus = new EventBus();
        this.eventBus.subscribe(new LoggingEventListener());
        this.queueService = new QueueService(new QueueManager(), eventBus, queueOverloadThreshold);
        this.routingService = new RoutingService(routingStrategy);
        this.sessionLifecycleService = new SessionLifecycleService(new UuidIdGenerator(), eventBus);
        this.agentLifecycleService = new AgentLifecycleService(eventBus);
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    /** Ek dinleyici eklemek icin (metrik toplama, dashboard vs.) disariya aciyoruz. */
    public EventBus getEventBus() {
        return eventBus;
    }

    public void addContact(Contact contact) {
        LOGGER.info("[GELEN CAGRI] " + contact.getName() + " (" + contact.getChannel()
                + ") baglandi. VIP mi: " + contact.isVip());

        Session existingSession = sessionLifecycleService.findActiveSessionByContactId(contact.getContactId());
        if (existingSession != null) {
            LOGGER.info("[OMNICHANNEL] " + contact.getName() + " zaten "
                    + existingSession.getAgent().getName() + " ile gorusmede. Mesaj ayni temsilciye baglandi.");
            return;
        }

        queueService.enqueue(contact);
        processQueue();
    }

    public void processQueue() {
        queueProcessingLock.lock();
        try {
            while (!queueService.isEmpty()) {
                Contact contact = queueService.peek();
                if (contact == null) {
                    break;
                }
                Agent lastAgent = customerLastAgentMap.get(contact.getContactId());
                Agent targetAgent = routingService.findAgent(agents, contact, lastAgent);

                if (targetAgent == null) {
                    LOGGER.info("[KUYRUKTA BEKLIYOR] " + contact.getName()
                            + " icin uygun temsilci yok / temsilciler mesgul veya molada.");
                    break;
                }

                Contact dequeued = queueService.dequeue();
                try {
                    sessionLifecycleService.startSession(targetAgent, dequeued);
                    customerLastAgentMap.put(dequeued.getContactId(), targetAgent);
                } catch (Exception e) {
                    // Baska bir thread araya girip kapasiteyi doldurmus olabilir:
                    // musteriyi kaybetmemek icin kuyruga geri koy ve dongudeyi durdur.
                    LOGGER.log(Level.WARNING,
                            "Oturum baslatilamadi, musteri kuyruga geri konuluyor: " + dequeued.getContactId(), e);
                    queueService.enqueue(dequeued);
                    break;
                }
            }
        } finally {
            queueProcessingLock.unlock();
        }
    }

    public void handleAgentNoAnswer(Agent agent, Contact contact) {
        agentLifecycleService.handleNoAnswer(agent, contact);
        queueService.enqueue(contact);
        processQueue();
    }

    /** Orijinal kodda karsiligi olmayan yeni metot: moladaki temsilciyi tekrar aktif eder. */
    public void returnAgentFromBreak(Agent agent) {
        agentLifecycleService.returnFromBreak(agent);
        processQueue();
    }

    public void transferSession(Session session, Agent targetAgent) {
        try {
            sessionLifecycleService.transferSession(session, targetAgent);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "[TRANSFER BASARISIZ] " + e.getMessage());
        }
    }

    public void handleAgentOffline(Agent agent) {
        agentLifecycleService.handleOffline(agent);

        List<Session> toRequeue = new ArrayList<>(sessionLifecycleService.getActiveSessions());
        for (Session session : toRequeue) {
            if (session.getAgent().equals(agent)) {
                sessionLifecycleService.endSession(session, "agent_offline");
                LOGGER.info("[MESAI BITISI] " + session.getContact().getName() + " musterisi tekrar kuyruga aktariliyor.");
                queueService.enqueue(session.getContact());
            }
        }
        processQueue();
    }
}