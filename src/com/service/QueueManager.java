package com.service;

import com.model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class QueueManager {
    private ArrayList<Contact> waiting;
    private ArrayList<Agent> available;
    private Session session;

    public QueueManager(Session session) {
        this.waiting = new ArrayList<>();
        this.available = new ArrayList<>();
        this.session = session;
    }

    public void addWaitingContact(Contact contact) {
        contact.setStatus("WAITING");
        waiting.add(contact);
        System.out.println("⏳ [KUYRUK] Müşteri beklemeye alındı: " + contact.getName() + " " + contact.getSurname() + " (" + contact.getChannel() + ")");
        processQueue();
    }

    public void addAvailableAgent(Agent agent) {
        if (agent.getStatus() == AgentStatus.ONLINE && !available.contains(agent)) {
            available.add(agent);
        }
        processQueue();
    }

    public void processQueue() {
        if (waiting.isEmpty()) return;

        Iterator<Contact> iterator = waiting.iterator();
        while (iterator.hasNext()) {
            Contact contact = iterator.next();
            Agent matchedAgent = findAgentForContact(contact);

            if (matchedAgent != null) {
                iterator.remove();
                available.remove(matchedAgent);

                String chatHistoryId = "CHAT_" + System.currentTimeMillis();
                session.createSession(chatHistoryId, contact, matchedAgent);
            }
        }
    }

    private Agent findAgentForContact(Contact contact) {
        for (Agent agent : available) {
            if (agent.getStatus() == AgentStatus.ONLINE && agent.getSupportedChannels().contains(contact.getChannel())) {
                return agent;
            }
        }
        return null;
    }

    // Diyagramdaki transfer(agentId) metodu
    public void transfer(String agentId) {
        System.out.println("\n🔄 [TRANSFER] Agent ID: " + agentId + " durumu değişti (OFFLINE/ONBREAK). Çağrı aktarılıyor...");

        String activeChatHistoryId = null;
        Contact targetContact = null;

        for (Map.Entry<String, HashMap<Contact, Agent>> entry : session.getSessionMap().entrySet()) {
            for (Map.Entry<Contact, Agent> inner : entry.getValue().entrySet()) {
                if (inner.getValue().getId().equals(agentId)) {
                    activeChatHistoryId = entry.getKey();
                    targetContact = inner.getKey();
                    break;
                }
            }
        }

        if (targetContact != null && activeChatHistoryId != null) {
            session.closeSession(activeChatHistoryId);
            System.out.println("🔀 " + targetContact.getName() + " başka bir temsilciye aktarılmak üzere yeniden kuyruğa alınıyor...");
            addWaitingContact(targetContact);
        } else {
            System.out.println("ℹ️ Agent ID: " + agentId + " üzerinde aktif oturum bulunamadı.");
        }
    }

    public ArrayList<Contact> getWaiting() { return waiting; }
    public ArrayList<Agent> getAvailable() { return available; }
}