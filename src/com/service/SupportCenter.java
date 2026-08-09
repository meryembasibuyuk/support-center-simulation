package com.service;

import com.model.Agent;
import com.model.Message;
import com.strategy.RoutingStrategy;

import java.util.ArrayList;
import java.util.List;

public class SupportCenter {
    private List<Agent> agents;
    private RoutingStrategy routingStrategy;

    public SupportCenter(RoutingStrategy routingStrategy) {
        this.agents = new ArrayList<>();
        this.routingStrategy = routingStrategy;
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    public void setRoutingStrategy(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
    }

    public void handleMessage(Message message) {
        System.out.println("\n[Yeni Mesaj] Gönderen: " + message.getSender() + " | Mesaj: \"" + message.getContent() + "\"");
        
        Agent assignedAgent = routingStrategy.route(message, agents);
        
        if (assignedAgent != null) {
            assignedAgent.assignMessage(message);
            System.out.println(" SUCCESS: Mesaj " + assignedAgent.getName() + " isimli temsilciye atandı.");
        } else {
            System.out.println(" ERROR: Mesaj atanacak uygun temsilci bulunamadı!");
        }
    }
}