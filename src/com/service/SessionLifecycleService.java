package com.service;

import com.event.EventBus;
import com.event.SessionEndedEvent;
import com.event.SessionStartedEvent;
import com.event.TransferEvent;
import com.model.Agent;
import com.model.Contact;
import com.model.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


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
        Agent oldAgent = session.getAgent();
        if (oldAgent != null && oldAgent.equals(targetAgent)) {
            return; // zaten bu temsilcide, yapilacak bir sey yok
        }

        targetAgent.addSession(session); // atomik kontrol + ekleme, uygun degilse burada fırlatir
        if (oldAgent != null) {
            oldAgent.removeSession(session);
        }
        session.setAgent(targetAgent);
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
        // Java 8 uyumlu: List.copyOf() Java 10+ gerektirir
        return Collections.unmodifiableList(new ArrayList<>(activeSessions));
    }
}