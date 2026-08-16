package com.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basit bir Observer/Publish-Subscribe implementasyonu.
 * SupportCenter'daki System.out.println cagrilarinin yerini alir: is kurallari
 * artik loglamadan/bildirimden bagimsiz calisir (SRP), yeni bir dinleyici
 * (metrik toplama, dashboard event'i, vs.) eklemek mevcut kodu degistirmez (OCP).
 */
public class EventBus {
    private static final Logger LOGGER = Logger.getLogger(EventBus.class.getName());
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void subscribe(EventListener listener) {
        listeners.add(listener);
    }

    /**
     * Event'i tum listener'lara yayinlar. Bir listener beklenmedik bir
     * exception firlatirsa bile diger listener'lar ve ana is akisi bundan
     * etkilenmez (resilience / hata izolasyonu).
     */
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
