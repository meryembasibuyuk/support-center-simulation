package com.service;

import com.event.EventBus;
import com.event.SessionEndedEvent;
import com.event.SessionStartedEvent;
import com.event.TransferEvent;
import com.exception.AgentNotAvailableException;
import com.model.Agent;
import com.model.AgentStatus;
import com.model.Contact;
import com.model.Session;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Oturumlarin (Session) yasam dongusunu yonetir: baslatma, bitirme, transfer.
 * Onceki tek-god-class SupportCenter'dan ayristirilarak SRP saglanir.
 */
public class SessionLifecycleService {
    private final List<Session> activeSessions = new CopyOnWriteArrayList<>();
    private final IdGenerator idGenerator;
    private final EventBus eventBus;

    public SessionLifecycleService(IdGenerator idGenerator, EventBus eventBus) {
        this.idGenerator = idGenerator;
        this.eventBus = eventBus;
    }

    public Session startSession(Agent agent, Contact contact) {
        Session session = new Session(idGenerator.nextId(), agent, contact);
        agent.addSession(session); // kapasite/durum kontrolu Agent icinde atomik yapilir
        activeSessions.add(session);
        eventBus.publish(new SessionStartedEvent(session));
        return session;
    }

    public void endSession(Session session, String reason) {
        session.getAgent().removeSession(session);
        activeSessions.remove(session);
        eventBus.publish(new SessionEndedEvent(session, reason));
    }

    public void transferSession(Session session, Agent targetAgent) {
        if (targetAgent.getStatus() != AgentStatus.ONLINE || !targetAgent.hasCapacity()) {
            throw new AgentNotAvailableException(
                    "Hedef temsilci (" + targetAgent.getAgentId() + ") transfer icin musait degil.");
        }
        Agent oldAgent = session.getAgent();
        oldAgent.removeSession(session);
        session.setAgent(targetAgent);
        targetAgent.addSession(session);
        eventBus.publish(new TransferEvent(session, oldAgent, targetAgent));
    }

    public Session findActiveSessionByContactId(String contactId) {
        for (Session s : activeSessions) {
            if (s.getContact().getContactId().equals(contactId)) {
                return s;
            }
        }
        return null;
    }

    public List<Session> getActiveSessions() {
        return List.copyOf(activeSessions);
    }
}
