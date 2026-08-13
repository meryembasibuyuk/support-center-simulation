package com.service;

import com.model.*;
import com.strategy.RoutingStrategy;
import java.util.*;

public class SupportCenter {
    private List<Agent> agents = new ArrayList<>();
    private List<Session> activeSessions = new ArrayList<>();
    private QueueManager queueManager = new QueueManager();
    private RoutingStrategy routingStrategy;
    
    // Sticky Agent için müşteri - temsilci geçmiş haritası
    private Map<String, Agent> customerLastAgentMap = new HashMap<>();

    public SupportCenter(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    // Senaryo: Gelen İletişim & Omnichannel Çakışması Kontrolü
    public void addContact(Contact contact) {
        System.out.println("\n[GELEN ÇAĞRI] " + contact.getName() + " (" + contact.getChannel() + ") bağlandı. VIP mi: " + contact.isVip());
        
        // Omnichannel Collision: Müşterinin zaten açık bir oturumu var mı?
        Session existingSession = findActiveSessionByContactId(contact.getContactId());
        if (existingSession != null) {
            System.out.println("[OMNICHANNEL] " + contact.getName() + " zaten " 
                               + existingSession.getAgent().getName() + " ile görüşmede. Mesaj aynı temsilciye bağlandı.");
            return;
        }

        queueManager.enqueue(contact);
        processQueue();
    }

    public void processQueue() {
        while (!queueManager.isEmpty()) {
            Contact contact = queueManager.peek();
            Agent lastAgent = customerLastAgentMap.get(contact.getContactId());
            
            Agent targetAgent = routingStrategy.route(agents, contact, lastAgent);

            if (targetAgent != null) {
                queueManager.dequeue();
                Session session = new Session("S-" + UUID.randomUUID().toString().substring(0, 4), targetAgent, contact);
                
                targetAgent.addSession(session);
                activeSessions.add(session);
                customerLastAgentMap.put(contact.getContactId(), targetAgent);

                System.out.println("[OTURUM BAŞLADI] Oturum ID: " + session.getSessionId() 
                                   + " | Müşteri: " + contact.getName() 
                                   + " -> Temsilci: " + targetAgent.getName());
            } else {
                System.out.println("[KUYRUKTA BEKLİYOR] " + contact.getName() + " için uygun temsilci yok / temsilciler meşgul veya molada.");
                break;
            }
        }
    }

    // Senaryo: Pas Geçme (Agent No-Answer)
    public void handleAgentNoAnswer(Agent agent, Contact contact) {
        System.out.println("\n[PAS GEÇTİ] " + agent.getName() + " yanıt vermedi. Geçici olarak molaya/pasife alınıyor...");
        agent.setStatus(AgentStatus.ONBREAK);
        
        // Müşteriyi kuyruğa geri koy ve yeniden yönlendir
        queueManager.enqueue(contact);
        processQueue();
    }

    // Senaryo: Transfer (Escalation)
    public void transferSession(Session session, Agent targetAgent) {
        if (targetAgent.getStatus() == AgentStatus.ONLINE && targetAgent.hasCapacity()) {
            Agent oldAgent = session.getAgent();
            oldAgent.removeSession(session);
            
            session.setAgent(targetAgent);
            targetAgent.addSession(session);
            
            System.out.println("\n[TRANSFER] Oturum " + oldAgent.getName() 
                               + " temsilcisinden " + targetAgent.getName() + " temsilcisine devredildi.");
        } else {
            System.out.println("\n[TRANSFER BAŞARISIZ] Hedef temsilci müsait değil.");
        }
    }

    // Senaryo: Mesai Bitişi (Agent Offline)
    public void handleAgentOffline(Agent agent) {
        System.out.println("\n[MESAİ BİTİŞİ] Temsilci " + agent.getName() + " offline oldu.");
        agent.setStatus(AgentStatus.OFFLINE);

        List<Session> sessionsToTransfer = new ArrayList<>(agent.getActiveSessions());
        for (Session session : sessionsToTransfer) {
            agent.removeSession(session);
            activeSessions.remove(session);
            System.out.println("[MESAİ BİTİŞİ] " + session.getContact().getName() + " müşterisi tekrar kuyruğa aktarılıyor.");
            queueManager.enqueue(session.getContact());
        }
        processQueue();
    }

    private Session findActiveSessionByContactId(String contactId) {
        return activeSessions.stream()
                .filter(s -> s.getContact().getContactId().equals(contactId))
                .findFirst()
                .orElse(null);
    }
}