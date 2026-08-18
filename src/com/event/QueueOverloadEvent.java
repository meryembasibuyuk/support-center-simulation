package com.event;

public class QueueOverloadEvent extends AbstractEvent {
    private final int queueSize;
    private final int threshold;

    public QueueOverloadEvent(int queueSize, int threshold) {
        this.queueSize = queueSize;
        this.threshold = threshold;
    }

    public int getQueueSize() { return queueSize; }
    public int getThreshold() { return threshold; }
}