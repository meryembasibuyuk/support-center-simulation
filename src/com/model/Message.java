package com.model;

public class Message {
    private String id;
    private String content;
    private Contact sender;

    public Message(String id, String content, Contact sender) {
        this.id = id;
        this.content = content;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Contact getSender() {
        return sender;
    }
}