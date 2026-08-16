package com.event;

import com.model.Agent;
import com.model.Session;

public class TransferEvent extends AbstractEvent {
    private final Session session;
    private final Agent fromAgent;
    private final Agent toAgent;

    public TransferEvent(Session session, Agent fromAgent, Agent toAgent) {
        this.session = session;
        this.fromAgent = fromAgent;
        this.toAgent = toAgent;
    }

    public Session getSession() { return session; }
    public Agent getFromAgent() { return fromAgent; }
    public Agent getToAgent() { return toAgent; }
}
