package com;

import com.model.*;
import com.service.SupportCenter;
import com.strategy.AdvancedRoutingStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== DESTEK MERKEZI SIMULASYONU (REFAKTOR EDILMIS) ===\n");

        SupportCenter supportCenter = new SupportCenter(new AdvancedRoutingStrategy());

        Agent agent1 = new Agent("A1", "Ahmet", "Yilmaz", 2); // Max 2 kapasite
        agent1.addSupportedChannel(Channel.WHATSAPP);
        agent1.addSupportedChannel(Channel.INSTAGRAM);

        Agent agent2 = new Agent("A2", "Fatma", "Kaya", 2); // Max 2 kapasite
        agent2.addSupportedChannel(Channel.TELEGRAM);
        agent2.addSupportedChannel(Channel.FACEBOOK);

        supportCenter.addAgent(agent1);
        supportCenter.addAgent(agent2);

        // Normal musteri baglantilari
        Contact c1 = new Contact("C1", "Mehmet", "Aydin", Channel.INSTAGRAM, false);
        Contact c2 = new Contact("C2", "Ayse", "Sahin", Channel.TELEGRAM, false);

        supportCenter.addContact(c1); // Ahmet'e atandi
        supportCenter.addContact(c2); // Fatma'ya atandi

        // VIP musteri ve sira onceligi
        Contact c3 = new Contact("C3", "Can", "Ozturk", Channel.INSTAGRAM, true);
        supportCenter.addContact(c3); // Kuyrugun en onune gecer

        // Omnichannel cakismasi (Mehmet baska kanaldan da yaziyor)
        Contact c1Duplicate = new Contact("C1", "Mehmet", "Aydin", Channel.FACEBOOK, false);
        supportCenter.addContact(c1Duplicate); // Yeni temsilci atanmaz, Ahmet'e yonlendirilir

        // Mola senaryosu
        System.out.println("\n--- MOLA SENARYOSU ---");
        agent2.transitionTo(AgentStatus.ONBREAK);

        // Moladan donus senaryosu (orijinal kodda hic yoktu, agent sonsuza kadar takili kalirdi)
        System.out.println("\n--- MOLADAN DONUS SENARYOSU ---");
        supportCenter.returnAgentFromBreak(agent2);

        // Mesai bitisi senaryosu
        supportCenter.handleAgentOffline(agent1);
    }
}
