package com.event;

import com.model.Session;

public class SessionStartedEvent extends AbstractEvent {
    private final Session session;

    public SessionStartedEvent(Session session) {
        this.session = session;
    }

    public Session getSession() { return session; }
}
