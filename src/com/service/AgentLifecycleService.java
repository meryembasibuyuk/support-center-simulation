package com.service;

import com.event.AgentNoAnswerEvent;
import com.event.AgentOfflineEvent;
import com.event.EventBus;
import com.model.Agent;
import com.model.AgentStatus;
import com.model.Contact;


public class AgentLifecycleService {
    private final EventBus eventBus;

    public AgentLifecycleService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void handleNoAnswer(Agent agent, Contact contact) {
        agent.transitionTo(AgentStatus.ONBREAK);
        eventBus.publish(new AgentNoAnswerEvent(agent, contact));
    }

    public void returnFromBreak(Agent agent) {
        agent.transitionTo(AgentStatus.ONLINE);
    }

    public void handleOffline(Agent agent) {
        agent.transitionTo(AgentStatus.OFFLINE);
        eventBus.publish(new AgentOfflineEvent(agent));
    }
}
