package com.service;

import com.event.EventBus;
import com.event.QueueOverloadEvent;
import com.model.Contact;

public class QueueService {
    private final QueueManager queueManager;
    private final EventBus eventBus;
    private final int overloadThreshold;

    public QueueService(QueueManager queueManager, EventBus eventBus, int overloadThreshold) {
        this.queueManager = queueManager;
        this.eventBus = eventBus;
        this.overloadThreshold = overloadThreshold;
    }

    public void enqueue(Contact contact) {
        queueManager.enqueue(contact);
        checkOverload();
    }

    private void checkOverload() {
        int currentSize = queueManager.size();
        if (currentSize >= overloadThreshold && eventBus != null) {
            eventBus.publish(new QueueOverloadEvent(currentSize, overloadThreshold));
        }
    }

    public Contact peek() {
        return queueManager.peek();
    }

    public Contact dequeue() {
        return queueManager.dequeue();
    }

    public boolean isEmpty() {
        return queueManager.isEmpty();
    }

    public int size() {
        return queueManager.size();
    }
}