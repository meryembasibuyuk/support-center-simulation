package com.service;

import com.model.Contact;

public class QueueService {
    private final QueueManager queueManager;

    public QueueService(QueueManager queueManager) {
        this.queueManager = queueManager;
    }

    public void enqueue(Contact contact) {
        queueManager.enqueue(contact);
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