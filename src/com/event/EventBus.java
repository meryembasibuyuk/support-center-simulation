package com.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventBus {
    private static final Logger LOGGER = Logger.getLogger(EventBus.class.getName());
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    public void publish(DomainEvent event) {
        for (EventListener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING,
                        "Listener event islerken hata firlatti: " + event.getClass().getSimpleName(), e);
            }
        }
    }
}
