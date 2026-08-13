package com.model;

public class Session {
    private String sessionId;
    private Agent agent;
    private Contact contact;

    public Session(String sessionId, Agent agent, Contact contact) {
        this.sessionId = sessionId;
        this.agent = agent;
        this.contact = contact;
    }

    public String getSessionId() { return sessionId; }
    public Agent getAgent() { return agent; }
    public void setAgent(Agent agent) { this.agent = agent; }
    public Contact getContact() { return contact; }
}