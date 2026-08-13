package com.model;

public class Contact {
    private String contId;
    private String name;
    private String surname;
    private Channel channel;
    private String session; // Diyagramdaki SESSION alanı
    private String status;  // WAITING, INCALL

    public Contact(String contId, String name, String surname, Channel channel) {
        this.contId = contId;
        this.name = name;
        this.surname = surname;
        this.channel = channel;
        this.session = null;
        this.status = "WAITING";
    }

    public void sendMessage(String message) {
        System.out.println("[Müşteri] " + name + " " + surname + " (" + channel + "): " + message);
    }

    // Getter & Setter
    public String getContId() { return contId; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public Channel getChannel() { return channel; }
    public String getSession() { return session; }
    public void setSession(String session) { this.session = session; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}