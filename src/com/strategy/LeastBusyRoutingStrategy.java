package com.strategy;

import com.model.Agent;
import com.model.Message;
import java.util.List;

public class LeastBusyRoutingStrategy implements RoutingStrategy {

    @Override
    public Agent route(Message message, List<Agent> availableAgents) {
        if (availableAgents == null || availableAgents.isEmpty()) {
            return null;
        }

        // 1. ÖNCELİK: Müşterinin özel/ait olduğu bir temsilci var mı?
        if (message.getSender().hasAssignedAgent()) {
            Agent assignedAgent = message.getSender().getAssignedAgent();
            if (availableAgents.contains(assignedAgent)) {
                System.out.println("-> Aitlik Bağlantısı: Mesaj doğrudan " + message.getSender().getName() + "'in özel temsilcisine (" + assignedAgent.getName() + ") yönlendirildi.");
                return assignedAgent;
            }
        }

        // 2. ÖNCELİK: Özel temsilcisi yoksa en az yoğun temsilciyi seç
        Agent leastBusyAgent = availableAgents.get(0);
        for (Agent agent : availableAgents) {
            if (agent.getActiveTaskCount() < leastBusyAgent.getActiveTaskCount()) {
                leastBusyAgent = agent;
            }
        }
        
        System.out.println("-> Genel Yönlendirme: Mesaj en müsait temsilciye (" + leastBusyAgent.getName() + ") yönlendirildi.");
        return leastBusyAgent;
    }
}
