package com.service;

import com.model.Agent;
import com.model.Message;
import com.strategy.RoutingStrategy;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class SupportCenter {
    private final List<Agent> agents = new ArrayList<>();
    private final Queue<Message> waitingQueue = new ArrayDeque<>();
    private RoutingStrategy routingStrategy;

    public SupportCenter(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
    }

    public void setRoutingStrategy(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    // Yeni mesaj geldiğinde çalışan metot
    public void receiveMessage(Message message) {
        System.out.println("\n---> Yeni Mesaj Geldi: [" + message.getId() + "] " + message.getContent());
        Agent availableAgent = routingStrategy.selectAgent(agents);

        if (availableAgent != null) {
            assignMessageToAgent(message, availableAgent);
        } else {
            waitingQueue.add(message);
            System.out.println("ALERT: Müsait temsilci yok! Mesaj kuyruğa alındı: " + message.getId());
        }
        printStatus();
    }

    // Temsilci işini bitirdiğinde çalışan metot
    public void completeJob(Agent agent) {
        System.out.println("\n<--- İş Tamamlandı: Temsilci [" + agent.getName() + "] işini bitirdi.");
        agent.setBusy(false);

        // Bekleyen mesaj varsa sıradakini temsilciye ata (FIFO)
        if (!waitingQueue.isEmpty()) {
            Message nextMessage = waitingQueue.poll();
            System.out.println("Kuyrukta bekleyen mesaj temsilciye yönlendiriliyor...");
            assignMessageToAgent(nextMessage, agent);
        }

        printStatus();
    }

    private void assignMessageToAgent(Message message, Agent agent) {
        agent.setBusy(true);
        agent.incrementJobCount();
        System.out.println("ATAMA: Mesaj [" + message.getId() + "] -> Temsilci [" + agent.getName() + "]");
    }

    public void printStatus() {
        long busyCount = agents.stream().filter(Agent::isBusy).count();
        long freeCount = agents.size() - busyCount;

        System.out.println("--- ANLIK DURUM ---");
        System.out.println("Müsait Temsilci Sayısı : " + freeCount);
        System.out.println("Meşgul Temsilci Sayısı : " + busyCount);
        System.out.println("Bekleyen Mesaj Sayısı  : " + waitingQueue.size());
        System.out.println("-------------------");
    }

    public void printSummary() {
        System.out.println("\n==========================================");
        System.out.println("       SİMÜLASYON ÖZETİ VE İSTATİSTİKLER   ");
        System.out.println("==========================================");
        for (Agent agent : agents) {
            System.out.println("Temsilci: " + agent.getName() + " | Toplam Aldığı İş: " + agent.getCompletedJobCount());
        }
        System.out.println("==========================================");
    }
}