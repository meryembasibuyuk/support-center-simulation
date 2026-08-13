package com.service;

import com.model.Contact;
import java.util.PriorityQueue;
import java.util.Queue;

public class QueueManager {
    // PriorityQueue sayesinde VIP kişiler sıranın otomatik önüne geçer
    private Queue<Contact> queue = new PriorityQueue<>();

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
}