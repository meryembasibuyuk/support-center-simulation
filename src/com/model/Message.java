package com.model;

public class Message {
    private String id;
    private Contact sender;
    private String content;

    public Message(String id, Contact sender, String content) {
        this.id = id;
        this.sender = sender;
        this.content = content;
    }

    public String getId() { return id; }
    public Contact getSender() { return sender; }
    public String getContent() { return content; }
}