package com.model;

import java.util.ArrayList;
import java.util.List;

public class Agent {
    private String agentId;
    private String name;
    private String surname;
    private AgentStatus status;
    private List<Channel> supportedChannels = new ArrayList<>();
    private List<Session> activeSessions = new ArrayList<>();
    private int maxCapacity = 3; // Varsayılan eşzamanlı görüşme sınırı

    public Agent(String agentId, String name, String surname, int maxCapacity) {
        this.agentId = agentId;
        this.name = name;
        this.surname = surname;
        this.status = AgentStatus.ONLINE;
        this.maxCapacity = maxCapacity;
    }

    public void addSupportedChannel(Channel channel) {
        supportedChannels.add(channel);
    }

    public boolean supportsChannel(Channel channel) {
        return supportedChannels.contains(channel);
    }

    public boolean hasCapacity() {
        return activeSessions.size() < maxCapacity;
    }

    public void addSession(Session session) {
        activeSessions.add(session);
        if (activeSessions.size() >= maxCapacity) {
            this.status = AgentStatus.BUSY;
        }
    }

    public void removeSession(Session session) {
        activeSessions.remove(session);
        if (activeSessions.size() < maxCapacity && this.status == AgentStatus.BUSY) {
            this.status = AgentStatus.ONLINE;
        }
    }

    // Getter & Setter
    public String getAgentId() { return agentId; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public AgentStatus getStatus() { return status; }
    public void setStatus(AgentStatus status) { this.status = status; }
    public List<Session> getActiveSessions() { return activeSessions; }
}