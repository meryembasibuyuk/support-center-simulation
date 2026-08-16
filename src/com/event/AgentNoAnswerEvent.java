package com.event;

import com.model.Agent;
import com.model.Contact;

public class AgentNoAnswerEvent extends AbstractEvent {
    private final Agent agent;
    private final Contact contact;

    public AgentNoAnswerEvent(Agent agent, Contact contact) {
        this.agent = agent;
        this.contact = contact;
    }

    public Agent getAgent() { return agent; }
    public Contact getContact() { return contact; }
}
