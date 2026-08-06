package com.model;

public class Agent {
    private String id;
    private String name;
    private boolean available;
    private int assignedMessageCount;
    private Message currentMessage;

    public Agent(String id, String name) {
        this.id = id;
        this.name = name;
        this.available = true;
        this.assignedMessageCount = 0;
        this.currentMessage = null;
    }

    public void assignMessage(Message message) {
        this.currentMessage = message;
        this.available = false;
        this.assignedMessageCount++;
    }

    public void completeCurrentTask() {
        this.currentMessage = null;
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getAssignedMessageCount() {
        return assignedMessageCount;
    }

    public Message getCurrentMessage() {
        return currentMessage;
    }
}