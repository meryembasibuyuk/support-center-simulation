package com.model;

import com.exception.AgentNotAvailableException;
import com.exception.InvalidStateTransitionException;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe hale getirilmis Agent sinifi.
 *
 * Onceki versiyonda hasCapacity() kontrolu ile addSession() cagrisi arasinda
 * atomiklik yoktu: iki thread ayni anda hasCapacity()==true gorup ayni
 * temsilciye maxCapacity ustunde oturum ekleyebiliyordu (klasik TOCTOU / race
 * condition). Burada tum kapasite kontrolu + ekleme + durum gecisi tek bir
 * ReentrantLock altinda atomik olarak yapiliyor.
 */
public class Agent {
    private final String agentId;
    private final String name;
    private final String surname;
    private volatile AgentStatus status;
    private final List<Channel> supportedChannels = new CopyOnWriteArrayList<>();
    private final List<Session> activeSessions = new CopyOnWriteArrayList<>();
    private final int maxCapacity;
    private final ReentrantLock lock = new ReentrantLock();

    public Agent(String agentId, String name, String surname, int maxCapacity) {
        if (agentId == null || agentId.isBlank()) {
            throw new IllegalArgumentException("agentId bos olamaz");
        }
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("maxCapacity 0'dan buyuk olmali");
        }
        this.agentId = agentId;
        this.name = name;
        this.surname = surname;
        this.maxCapacity = maxCapacity;
        this.status = AgentStatus.ONLINE;
    }

    public void addSupportedChannel(Channel channel) {
        if (channel != null) {
            supportedChannels.add(channel);
        }
    }

    public boolean supportsChannel(Channel channel) {
        return supportedChannels.contains(channel);
    }

    public boolean hasCapacity() {
        lock.lock();
        try {
            return activeSessions.size() < maxCapacity;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Kapasite kontrolu + ekleme + gerekiyorsa BUSY'e gecis tek kilit altinda,
     * atomik olarak yapilir. Kapasite doluysa veya temsilci ONLINE degilse
     * AgentNotAvailableException firlatilir (sessizce yanlis davranmak yerine).
     */
    public void addSession(Session session) {
        lock.lock();
        try {
            if (activeSessions.size() >= maxCapacity) {
                throw new AgentNotAvailableException(agentId + " kapasitesi dolu, yeni oturum eklenemez.");
            }
            if (status != AgentStatus.ONLINE) {
                throw new AgentNotAvailableException(agentId + " su an ONLINE degil (" + status + "), oturum eklenemez.");
            }
            activeSessions.add(session);
            if (activeSessions.size() >= maxCapacity) {
                transitionToInternal(AgentStatus.BUSY);
            }
        } finally {
            lock.unlock();
        }
    }

    public void removeSession(Session session) {
        lock.lock();
        try {
            activeSessions.remove(session);
            if (activeSessions.size() < maxCapacity && status == AgentStatus.BUSY) {
                transitionToInternal(AgentStatus.ONLINE);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Durum gecisini AgentStatus'daki gecis tablosuna gore dogrulayarak uygular.
     * Gecersiz bir gecis InvalidStateTransitionException firlatir.
     */
    public void transitionTo(AgentStatus target) {
        lock.lock();
        try {
            transitionToInternal(target);
        } finally {
            lock.unlock();
        }
    }

    private void transitionToInternal(AgentStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new InvalidStateTransitionException(
                    agentId + " icin gecersiz durum gecisi: " + status + " -> " + target);
        }
        this.status = target;
    }

    public String getAgentId() { return agentId; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public AgentStatus getStatus() { return status; }
    public List<Session> getActiveSessions() { return List.copyOf(activeSessions); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;
        Agent other = (Agent) o;
        return agentId.equals(other.agentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agentId);
    }
}