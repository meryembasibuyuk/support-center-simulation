package com.model;

public class Contact implements Comparable<Contact> {
    private String contactId;
    private String name;
    private String surname;
    private Channel channel;
    private boolean isVip;

    public Contact(String contactId, String name, String surname, Channel channel, boolean isVip) {
        this.contactId = contactId;
        this.name = name;
        this.surname = surname;
        this.channel = channel;
        this.isVip = isVip;
    }

    public String getContactId() { return contactId; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public Channel getChannel() { return channel; }
    public boolean isVip() { return isVip; }

    // VIP olan müşterileri kuyrukta en öne geçiren kıyaslama mantığı
    @Override
    public int compareTo(Contact other) {
        return Boolean.compare(other.isVip, this.isVip);
    }
}