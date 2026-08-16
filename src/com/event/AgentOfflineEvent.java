package com.event;

import com.model.Agent;

public class AgentOfflineEvent extends AbstractEvent {
    private final Agent agent;

    public AgentOfflineEvent(Agent agent) {
        this.agent = agent;
    }

    public Agent getAgent() { return agent; }
}
