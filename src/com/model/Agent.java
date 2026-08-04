package com.model;

public class Agent {
    private final String id;
    private final String name;
    private boolean busy;
    private int completedJobCount;

    public Agent(String id, String name) {
        this.id = id;
        this.name = name;
        this.busy = false;
        this.completedJobCount = 0;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isBusy() { return busy; }
    public void setBusy(boolean busy) { this.busy = busy; }
    public int getCompletedJobCount() { return completedJobCount; }
    public void incrementJobCount() { this.completedJobCount++; }
    
}
