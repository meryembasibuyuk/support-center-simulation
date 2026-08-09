package com.model;

import java.util.ArrayList;
import java.util.List;

public class Agent {
    private String id;
    private String name;
    private List<Contact> assignedContacts; // Bu temsilciye ait müşteriler
    private List<Message> activeMessages;   // Üzerinde çalıştığı aktif mesajlar

    public Agent(String id, String name) {
        this.id = id;
        this.name = name;
        this.assignedContacts = new ArrayList<>();
        this.activeMessages = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Contact> getAssignedContacts() { return assignedContacts; }
    public List<Message> getActiveMessages() { return activeMessages; }

    // Müşteriyi temsilciye bağlama (Aitlik ilişkisi)
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