package com.model;

import java.util.HashMap;
import java.util.Map;

public class Session {
    // Diyagramdaki birebir yapı: HashMap<ChatHistoryId, HashMap<Contact, Agent>>
    private HashMap<String, HashMap<Contact, Agent>> sessionMap;

    public Session() {
        this.sessionMap = new HashMap<>();
    }

    public void createSession(String chatHistoryId, Contact contact, Agent agent) {
        HashMap<Contact, Agent> innerMap = new HashMap<>();
        innerMap.put(contact, agent);
        sessionMap.put(chatHistoryId, innerMap);

        contact.setSession(chatHistoryId);
        contact.setStatus("INCALL");
        agent.setStatus(AgentStatus.BUSY);

        System.out.println("✅ Session Başlatıldı | ID: " + chatHistoryId + " | Müşteri: " + contact.getName() + " -> Agent: " + agent.getName());
    }

    public void closeSession(String chatHistoryId) {
        if (sessionMap.containsKey(chatHistoryId)) {
            HashMap<Contact, Agent> innerMap = sessionMap.get(chatHistoryId);
            for (Map.Entry<Contact, Agent> entry : innerMap.entrySet()) {
                entry.getKey().setSession(null);
                entry.getKey().setStatus("WAITING");
                entry.getValue().setStatus(AgentStatus.ONLINE);
            }
            sessionMap.remove(chatHistoryId);
            System.out.println("⏹️ Session Kapatıldı: " + chatHistoryId);
        }
    }

    public HashMap<String, HashMap<Contact, Agent>> getSessionMap() {
        return sessionMap;
    }
}