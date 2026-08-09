package com.model;

public class Contact {
    private String id;
    private String name;
    private Channel channel;
    private Agent assignedAgent; // Müşterinin ait olduğu / zimmetli temsilci

    // Varsayılan (Özel temsilcisi olmayan müşteri)
    public Contact(String id, String name, Channel channel) {
        this.id = id;
        this.name = name;
        this.channel = channel;
        this.assignedAgent = null;
    }

    // Doğrudan temsilci atamasıyla oluşturma
    public Contact(String id, String name, Channel channel, Agent assignedAgent) {
        this.id = id;
        this.name = name;
        this.channel = channel;
        this.assignedAgent = assignedAgent;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Channel getChannel() { return channel; }
    
    public Agent getAssignedAgent() { return assignedAgent; }
    public void setAssignedAgent(Agent assignedAgent) { this.assignedAgent = assignedAgent; }

    public boolean hasAssignedAgent() {
        return this.assignedAgent != null;
    }

    @Override
    public String toString() {
        String agentInfo = hasAssignedAgent() ? " (Temsilci: " + assignedAgent.getName() + ")" : " (Temsilci Yok)";
        return name + " [" + channel + "]" + agentInfo;
    }
}