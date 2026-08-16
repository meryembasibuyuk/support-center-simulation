package com.service;

import com.model.Contact;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Thread-safe kuyruk. Onceki implementasyon ciplak bir PriorityQueue
 * kullaniyordu ve hicbir senkronizasyonu yoktu; concurrent enqueue/dequeue
 * cagrilarinda veri bozulmasi veya kayip riski vardi. PriorityBlockingQueue
 * VIP onceligini korurken ic kilitlemeyi kendisi saglar.
 */
public class QueueManager {
    private final PriorityBlockingQueue<Contact> queue = new PriorityBlockingQueue<>();

    public void enqueue(Contact contact) {
        queue.add(contact);
    }

    public Contact dequeue() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Contact peek() {
        return queue.peek();
    }

    public int size() {
        return queue.size();
    }
}
