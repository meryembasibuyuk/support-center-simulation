package com;

import com.model.*;
import com.service.SupportCenter;
import com.strategy.ChannelSpecialistRoutingStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== GELİŞMİŞ DESTEK MERKEZİ SİMÜLASYONU ===\n");

        SupportCenter supportCenter = new SupportCenter(new ChannelSpecialistRoutingStrategy());

        // 1. Temsilciler Oluşturuluyor ve Kanal Yetkileri Tanımlanıyor
        Agent agent1 = new Agent("A1", "Temsilci 1 (WP & Telegram Uzmanı)");
        agent1.addSupportedChannel(Channel.WHATSAPP);
        agent1.addSupportedChannel(Channel.TELEGRAM);

        Agent agent2 = new Agent("A2", "Temsilci 2 (Facebook & Instagram Uzmanı)");
        agent2.addSupportedChannel(Channel.FACEBOOK);
        agent2.addSupportedChannel(Channel.INSTAGRAM);

        Agent agent3 = new Agent("A3", "Temsilci 3 (Tüm Kanallar)");
        agent3.addSupportedChannel(Channel.WHATSAPP);
        agent3.addSupportedChannel(Channel.FACEBOOK);
        agent3.addSupportedChannel(Channel.TELEGRAM);
        agent3.addSupportedChannel(Channel.INSTAGRAM);
        
        // Agent 3 mola/mola durumunda olsun
        agent3.setStatus(AgentStatus.ON_BREAK);

        supportCenter.addAgent(agent1);
        supportCenter.addAgent(agent2);
        supportCenter.addAgent(agent3);

        // 2. Müşteriler Oluşturuluyor
        Contact c1 = new Contact("C1", "Ahmet", Channel.WHATSAPP);
        Contact c2 = new Contact("C2", "Fatma", Channel.FACEBOOK);
        Contact c3 = new Contact("C3", "Ali", Channel.TELEGRAM);

        // 3. Mesaj Simülasyonu
        Message m1 = new Message("M1", c1, "WhatsApp sipariş durumu?");
        Message m2 = new Message("M2", c2, "Facebook ürün sorgulama.");
        Message m3 = new Message("M3", c3, "Telegram bot bağlantısı koptu.");

        // Mesajlar İşleniyor
        supportCenter.handleMessage(m1); // Agent 1'e gider
        supportCenter.handleMessage(m2); // Agent 2'ye gider
        supportCenter.handleMessage(m3); // Agent 1'e gider (Agent 3 mola durumunda olduğu için)
    }
}