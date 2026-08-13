package com;

import com.model.*;
import com.service.SupportCenter;
import com.strategy.AdvancedRoutingStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== DESTEK MERKEZİ SİMÜLASYONU (GÜNCEL BÜTÜN SENARYOLAR) ===\n");

        SupportCenter supportCenter = new SupportCenter(new AdvancedRoutingStrategy());

        // 1. Temsilciler ve Kanallar Oluşturuluyor
        Agent agent1 = new Agent("A1", "Ahmet", "Yılmaz", 2); // Max 2 kapasite
        agent1.addSupportedChannel(Channel.WHATSAPP);
        agent1.addSupportedChannel(Channel.INSTAGRAM);

        Agent agent2 = new Agent("A2", "Fatma", "Kaya", 2); // Max 2 kapasite
        agent2.addSupportedChannel(Channel.TELEGRAM);
        agent2.addSupportedChannel(Channel.FACEBOOK);

        supportCenter.addAgent(agent1);
        supportCenter.addAgent(agent2);

        // 2. Normal Müşteri Bağlantıları
        Contact c1 = new Contact("C1", "Mehmet", "Aydın", Channel.INSTAGRAM, false);
        Contact c2 = new Contact("C2", "Ayşe", "Şahin", Channel.TELEGRAM, false);
        
        supportCenter.addContact(c1); // Ahmet'e atandı
        supportCenter.addContact(c2); // Fatma'ya atandı

        // 3. VIP Müşteri ve Sıra Önceliği Senaryosu
        Contact c3 = new Contact("C3", "Can", "Öztürk", Channel.INSTAGRAM, true); // VIP
        supportCenter.addContact(c3); // Kuyruğun en önüne geçer

        // 4. Omnichannel Çakışması Senaryosu (Mehmet başka kanaldan da yazıyor)
        Contact c1_duplicate = new Contact("C1", "Mehmet", "Aydın", Channel.FACEBOOK, false);
        supportCenter.addContact(c1_duplicate); // Yeni temsilci atanmaz, Ahmet'e yönlendirilir

        // 5. Mola (ONBREAK) Senaryosu
        System.out.println("\n--- MOLA SENARYOSU ---");
        agent2.setStatus(AgentStatus.ONBREAK);

        // 6. Mesai Bitişi (OFFLINE) Senaryosu
        supportCenter.handleAgentOffline(agent1);
    }
}