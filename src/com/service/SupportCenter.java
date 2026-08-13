package com.service;

import com.model.*;
import com.strategy.RoutingStrategy;

public class SupportCenter {
    private RoutingStrategy routingStrategy;
    private QueueManager queueManager;
    private Session session;

    public SupportCenter(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
        this.session = new Session();
        this.queueManager = new QueueManager(this.session);
    }

    public void addAgent(Agent agent) {
        queueManager.addAvailableAgent(agent);
    }

    public void addContact(Contact contact) {
        queueManager.addWaitingContact(contact);
    }

    public void transferAgent(String agentId) {
        queueManager.transfer(agentId);
    }

    public Session getSession() {
        return session;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public RoutingStrategy getRoutingStrategy() {
        return routingStrategy;
    }

    public void setRoutingStrategy(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
    }
}