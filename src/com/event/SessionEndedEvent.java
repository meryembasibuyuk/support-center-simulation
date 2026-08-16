package com.event;

import com.model.Session;

public class SessionEndedEvent extends AbstractEvent {
    private final Session session;
    private final String reason;

    public SessionEndedEvent(Session session, String reason) {
        this.session = session;
        this.reason = reason;
    }

    public Session getSession() { return session; }
    public String getReason() { return reason; }
}
