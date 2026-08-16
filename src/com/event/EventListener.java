package com.event;

public interface EventListener {
    void onEvent(DomainEvent event);
}
