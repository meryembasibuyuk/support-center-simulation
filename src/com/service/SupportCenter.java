package com.service;

import com.model.Agent;
import com.model.Message;
import com.strategy.RoutingStrategy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SupportCenter {
    private List<Agent> agents = new ArrayList<>();
    private Queue<Message> waitingQueue = new LinkedList<>();
    private RoutingStrategy strategy;

    public SupportCenter(RoutingStrategy strategy) {
        this.strategy = strategy;
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    public void receiveMessage(Message message) {
        Agent availableAgent = strategy.route(agents);
        if (availableAgent != null) {
            availableAgent.assignMessage(message);
            System.out.println("Mesaj (" + message.getId() + ") -> " + availableAgent.getName() + " temsilcisine atandi.");
        } else {
            waitingQueue.add(message);
            System.out.println("Tum temsilciler dolu! Mesaj (" + message.getId() + ") kuyruga alindi.");
        }
    }

    public void completeTask(String agentId) {
        for (Agent agent : agents) {
            if (agent.getId().equals(agentId)) {
                agent.completeCurrentTask();
                System.out.println("<--- Is Tamamlandi: Temsilci [" + agent.getName() + "] isini bitirdi.");

                if (!waitingQueue.isEmpty()) {
                    Message nextMessage = waitingQueue.poll();
                    agent.assignMessage(nextMessage);
                    System.out.println("Kuyruktaki Mesaj (" + nextMessage.getId() + ") -> " + agent.getName() + " temsilcisine atandi.");
                }
                break;
            }
        }
    }

    public void printCurrentStatus() {
        int availableCount = 0;
        int busyCount = 0;

        for (Agent agent : agents) {
            if (agent.isAvailable()) {
                availableCount++;
            } else {
                busyCount++;
            }
        }

        System.out.println("\n--- ANLIK DURUM ---");
        System.out.println("Musait Temsilci Sayisi : " + availableCount);
        System.out.println("Mesgul Temsilci Sayisi : " + busyCount);
        System.out.println("Bekleyen Mesaj Sayisi  : " + waitingQueue.size());
    }

    public void printSummary() {
        System.out.println("\n--- SIMULASYON OZETI VE ISTATISTIKLER ---");
        for (Agent agent : agents) {
            System.out.println("Temsilci: " + agent.getName() + " | Toplam Aldigi Is: " + agent.getAssignedMessageCount());
        }
    }
}