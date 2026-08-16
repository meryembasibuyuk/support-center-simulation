package com.model;

import java.time.Instant;

public class Session {
    private final String sessionId;
    private volatile Agent agent;
    private final Contact contact;
    private final Instant createdAt;

    public Session(String sessionId, Agent agent, Contact contact) {
        this.sessionId = sessionId;
        this.agent = agent;
        this.contact = contact;
        this.createdAt = Instant.now();
    }

    public String getSessionId() { return sessionId; }
    public Agent getAgent() { return agent; }
    public void setAgent(Agent agent) { this.agent = agent; }
    public Contact getContact() { return contact; }
    public Instant getCreatedAt() { return createdAt; }
}
