package com.strategy;

import com.model.Agent;
import com.model.AgentStatus;
import com.model.Message;

import java.util.List;
import java.util.stream.Collectors;

public class ChannelSpecialistRoutingStrategy implements RoutingStrategy {

    @Override
    public Agent route(Message message, List<Agent> availableAgents) {
        if (availableAgents == null || availableAgents.isEmpty()) {
            return null;
        }

        // 1. ÖNCELİK: Özel aitlik ilişkisi varsa ve temsilci ONLINE durumundaysa
        if (message.getSender().hasAssignedAgent()) {
            Agent assignedAgent = message.getSender().getAssignedAgent();
            if (assignedAgent.getStatus() == AgentStatus.ONLINE && availableAgents.contains(assignedAgent)) {
                System.out.println("-> [Aitlik] " + message.getSender().getName() + " özel temsilcisine (" + assignedAgent.getName() + ") yönlendirildi.");
                return assignedAgent;
            }
        }

        // 2. ÖNCELİK: Kanal Uzmanlığı Filitresi (ONLINE ve Gelen Kanalı Destekleyenler)
        List<Agent> eligibleAgents = availableAgents.stream()
                .filter(agent -> agent.canHandle(message.getChannel()))
                .collect(Collectors.toList());

        if (eligibleAgents.isEmpty()) {
            System.out.println("-> [Kanal Hatası] " + message.getChannel() + " kanalına bakabilecek ONLINE temsilci bulunamadı!");
            return null;
        }

        // 3. Uzman temsilciler arasından en az yoğun olanı seç
        Agent leastBusyAgent = eligibleAgents.get(0);
        for (Agent agent : eligibleAgents) {
            if (agent.getActiveTaskCount() < leastBusyAgent.getActiveTaskCount()) {
                leastBusyAgent = agent;
            }
        }

        System.out.println("-> [Kanal Uzmanı] Mesaj " + message.getChannel() + " uzmanı olan " + leastBusyAgent.getName() + " temsilcisine atandı.");
        return leastBusyAgent;
    }
}