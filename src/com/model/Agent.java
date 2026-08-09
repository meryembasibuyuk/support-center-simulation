package com.model;

import java.util.ArrayList;
import java.util.List;

public class Agent {
    private String id;
    private String name;
    private AgentStatus status;
    private List<Channel> supportedChannels; // Temsilcinin hizmet verebildiği kanallar
    private List<Contact> assignedContacts;
    private List<Message> activeMessages;

    public Agent(String id, String name) {
        this.id = id;
        this.name = name;
        this.status = AgentStatus.ONLINE; // Varsayılan olarak online
        this.supportedChannels = new ArrayList<>();
        this.assignedContacts = new ArrayList<>();
        this.activeMessages = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public AgentStatus getStatus() { return status; }
    public void setStatus(AgentStatus status) { this.status = status; }
    public List<Channel> getSupportedChannels() { return supportedChannels; }
    public List<Contact> getAssignedContacts() { return assignedContacts; }
    public List<Message> getActiveMessages() { return activeMessages; }

    // Kanal yetkisi ekleme
    public void addSupportedChannel(Channel channel) {
        if (!supportedChannels.contains(channel)) {
            supportedChannels.add(channel);
        }
    }

    // Temsilci bu kanala bakabilir mi ve müsait mi?
    public boolean canHandle(Channel channel) {
        return this.status == AgentStatus.ONLINE && supportedChannels.contains(channel);
    }

    public void addContact(Contact contact) {
        if (!assignedContacts.contains(contact)) {
            assignedContacts.add(contact);
            contact.setAssignedAgent(this);
        }
    }

    public void assignMessage(Message message) {
        activeMessages.add(message);
    }

    public int getActiveTaskCount() {
        return activeMessages.size();
    }
}