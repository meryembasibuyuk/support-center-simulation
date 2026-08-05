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
        this.available = true; // Başlangıçta temsilci müsait
        this.assignedMessageCount = 0;
        this.currentMessage = null;
    }

    // Mesaj atama metodu (SupportCenter'daki assignMessage hatasını çözer)
    public void assignMessage(Message message) {
        this.currentMessage = message;
        this.available = false; // İş aldığı için meşgul duruma geçer
        this.assignedMessageCount++; // Aldığı toplam iş sayısı artar
    }

    // İşi tamamlama metodu (SupportCenter'daki completeCurrentTask hatasını çözer)
    public void completeCurrentTask() {
        this.currentMessage = null;
        this.available = true; // İşi bittiği için tekrar müsait olur
    }

    // --- Getter ve Setter Metotları ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getAssignedMessageCount() {
        return assignedMessageCount;
    }

    public Message getCurrentMessage() {
        return currentMessage;
    }
}