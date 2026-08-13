package com.strategy;

import com.model.Agent;
import com.model.AgentStatus;
import com.model.Contact;

import java.util.List;

public class ChannelSpecialistRoutingStrategy implements RoutingStrategy {
    @Override
    public Agent route(Contact contact, List<Agent> agents) {
        for (Agent agent : agents) {
            // Temsilcinin ONLINE olması ve müşterinin kanalını desteklemesi gerekir
            if (agent.getStatus() == AgentStatus.ONLINE && agent.getSupportedChannels().contains(contact.getChannel())) {
                return agent;
            }
        }
        return null; // Uygun / müsait temsilci yoksa null döner (müşteri kuyrukta bekler)
    }
}