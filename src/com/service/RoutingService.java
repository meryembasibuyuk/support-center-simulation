package com.service;

import com.model.Agent;
import com.model.Contact;
import com.strategy.RoutingStrategy;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoutingService {
    private static final Logger LOGGER = Logger.getLogger(RoutingService.class.getName());
    private final RoutingStrategy strategy;

    public RoutingService(RoutingStrategy strategy) {
        this.strategy = strategy;
    }

    public Agent findAgent(List<Agent> agents, Contact contact, Agent lastInteractedAgent) {
        try {
            return strategy.route(agents, contact, lastInteractedAgent);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Routing stratejisi hata firlatti, musteri kuyrukta bekletiliyor: " + contact.getContactId(), e);
            return null;
        }
    }
}
