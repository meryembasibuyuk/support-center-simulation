package com.model;

import java.util.HashSet;
import java.util.Set;

public class Agent {
    private String id;
    private String name;
    private String surname;
    private AgentStatus status;
    private Set<Channel> supportedChannels;

    public Agent(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.status = AgentStatus.ONLINE; // Varsayılan olarak müsait
        this.supportedChannels = new HashSet<>();
    }

    public void addSupportedChannel(Channel channel) {
        supportedChannels.add(channel);
    }

    public void sendMessage(String message) {
        System.out.println("[Temsilci] " + name + " " + surname + ": " + message);
    }

    // Getter & Setter
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public AgentStatus getStatus() { return status; }
    public void setStatus(AgentStatus status) { this.status = status; }
    public Set<Channel> getSupportedChannels() { return supportedChannels; }
}