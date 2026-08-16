package com.model;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class Contact implements Comparable<Contact> {
    private final String contactId;
    private final String name;
    private final String surname;
    private final Channel channel;
    private final boolean isVip;
    private final Instant enqueuedAt;

    public Contact(String contactId, String name, String surname, Channel channel, boolean isVip) {
        if (contactId == null || contactId.isBlank()) {
            throw new IllegalArgumentException("contactId bos olamaz");
        }
        if (channel == null) {
            throw new IllegalArgumentException("channel null olamaz");
        }
        this.contactId = contactId;
        this.name = name;
        this.surname = surname;
        this.channel = channel;
        this.isVip = isVip;
        this.enqueuedAt = Instant.now();
    }

    public String getContactId() { return contactId; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public Channel getChannel() { return channel; }
    public boolean isVip() { return isVip; }
    public Instant getEnqueuedAt() { return enqueuedAt; }

    /** SLA/timeout mekanizmalari icin bir hook: kuyrukta ne kadar bekledigini verir. */
    public long waitingSeconds() {
        return Duration.between(enqueuedAt, Instant.now()).getSeconds();
    }

    // VIP olanlar once; ayni oncelik grubunda (VIP-VIP ya da normal-normal)
    // kuyruga giris zamanina gore FIFO garantisi saglanir. Orijinal kodda
    // sadece isVip karsilastirildigi icin ayni oncelikteki musteriler arasinda
    // siralama garanti degildi (PriorityQueue kararsiz siralama yapabiliyordu).
    @Override
    public int compareTo(Contact other) {
        int vipCompare = Boolean.compare(other.isVip, this.isVip);
        if (vipCompare != 0) {
            return vipCompare;
        }
        return this.enqueuedAt.compareTo(other.enqueuedAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contact)) return false;
        Contact other = (Contact) o;
        return contactId.equals(other.contactId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactId);
    }
}
