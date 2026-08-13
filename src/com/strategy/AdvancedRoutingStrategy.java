package com.strategy;

import com.model.Agent;
import com.model.AgentStatus;
import com.model.Contact;
import java.util.List;

public class AdvancedRoutingStrategy implements RoutingStrategy {

    @Override
    public Agent route(List<Agent> agents, Contact contact, Agent lastInteractedAgent) {
        // 1. Sticky Agent Senaryosu: Dün görüştüğü temsilci ONLINE mı, kapasitesi var mı ve kanalı destekliyor mu?
        if (lastInteractedAgent != null && 
            lastInteractedAgent.getStatus() == AgentStatus.ONLINE &&
            lastInteractedAgent.hasCapacity() &&
            lastInteractedAgent.supportsChannel(contact.getChannel())) {
            
            System.out.println("[YÖNLENDİRME] Müşteri önceden görüştüğü temsilciye (" 
                               + lastInteractedAgent.getName() + ") yönlendirildi.");
            return lastInteractedAgent;
        }

        // 2. Genel Yönlendirme: Durumu ONLINE olan, kanalı destekleyen ve kapasitesi olan ilk temsilci
        return agents.stream()
                .filter(a -> a.getStatus() == AgentStatus.ONLINE) // Mola (ONBREAK), BUSY veya OFFLINE olanlar elenir
                .filter(a -> a.supportsChannel(contact.getChannel()))
                .filter(Agent::hasCapacity)
                .findFirst()
                .orElse(null);
    }
}