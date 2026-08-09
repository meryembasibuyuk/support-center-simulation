package com;

import com.model.*;
import com.service.SupportCenter;
import com.strategy.LeastBusyRoutingStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== DESTEK MERKEZİ SİMÜLASYONU BAŞLIYOR ===\n");

        SupportCenter supportCenter = new SupportCenter(new LeastBusyRoutingStrategy());

        // 1. Temsilciler Oluşturuluyor
        Agent agent1 = new Agent("A1", "Temsilci 1 (WhatsApp Temsilcisi)");
        Agent agent2 = new Agent("A2", "Temsilci 2 (Genel Destek)");
        Agent agent3 = new Agent("A3", "Temsilci 3 (Genel Destek)");

        supportCenter.addAgent(agent1);
        supportCenter.addAgent(agent2);
        supportCenter.addAgent(agent3);

        // 2. Müşteriler Oluşturuluyor
        Contact c1 = new Contact("C1", "Ahmet", Channel.WHATSAPP);
        Contact c2 = new Contact("C2", "Mehmet", Channel.WHATSAPP);
        Contact c3 = new Contact("C3", "Ayşe", Channel.WHATSAPP);
        Contact c4 = new Contact("C4", "Fatma", Channel.FACEBOOK);
        Contact c5 = new Contact("C5", "Ali", Channel.TELEGRAM);

        // 3. Aitlik İlişkisi Kuruluyor (WP müşterileri Temsilci 1'e zimmetleniyor)
        agent1.addContact(c1);
        agent1.addContact(c2);
        agent1.addContact(c3);

        // 4. Mesaj Simülasyonu
        Message m1 = new Message("M1", c1, "WhatsApp siparişim nerede?");
        Message m2 = new Message("M2", c2, "WhatsApp ürün iadesi yapmak istiyorum.");
        Message m3 = new Message("M3", c3, "WhatsApp ödeme hatası.");
        Message m4 = new Message("M4", c4, "Facebook Messenger ile ulaşıyorum.");
        Message m5 = new Message("M5", c5, "Telegram bot çalışmıyor.");

        // Mesajlar İşleniyor
        supportCenter.handleMessage(m1); // Temsilci 1'e gider (Aitlikten dolayı)
        supportCenter.handleMessage(m2); // Temsilci 1'e gider (Aitlikten dolayı)
        supportCenter.handleMessage(m3); // Temsilci 1'e gider (Aitlikten dolayı)
        supportCenter.handleMessage(m4); // Müsait olan Temsilci 2 veya 3'e gider
        supportCenter.handleMessage(m5); // Müsait olan Temsilci 2 veya 3'e gider
    }
}