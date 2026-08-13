package com;

import com.model.*;
import com.service.SupportCenter;
import com.strategy.ChannelSpecialistRoutingStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== GELİŞMİŞ DESTEK MERKEZİ SİMÜLASYONU ===\n");

        // 1. Destek Merkezi & Strateji Başlatılıyor
        SupportCenter supportCenter = new SupportCenter(new ChannelSpecialistRoutingStrategy());

        // 2. Temsilciler Oluşturuluyor ve Kanalları Tanımlanıyor
        Agent agent1 = new Agent("A1", "Ahmet", "Yılmaz");
        agent1.addSupportedChannel(Channel.WHATSAPP);
        agent1.addSupportedChannel(Channel.TELEGRAM);

        Agent agent2 = new Agent("A2", "Fatma", "Kaya");
        agent2.addSupportedChannel(Channel.FACEBOOK);
        agent2.addSupportedChannel(Channel.INSTAGRAM);

        Agent agent3 = new Agent("A3", "Ali", "Demir");
        agent3.addSupportedChannel(Channel.WHATSAPP);
        agent3.addSupportedChannel(Channel.FACEBOOK);
        agent3.addSupportedChannel(Channel.TELEGRAM);
        agent3.addSupportedChannel(Channel.INSTAGRAM);

        // Temsilciler sisteme ekleniyor
        supportCenter.addAgent(agent1);
        supportCenter.addAgent(agent2);
        supportCenter.addAgent(agent3);

        // 3. Müşteriler Geliyor ve Çağrı Başlatılıyor
        Contact c1 = new Contact("C1", "Mehmet", "Aydın", Channel.WHATSAPP);
        Contact c2 = new Contact("C2", "Ayşe", "Şahin", Channel.FACEBOOK);
        Contact c3 = new Contact("C3", "Can", "Öztürk", Channel.TELEGRAM);

        System.out.println("--- 1. MÜŞTERİ GELİŞLERİ VE OTURUM EŞLEŞMELERİ ---");
        supportCenter.addContact(c1); // A1 ile eşleşir (A1 BUSY olur)
        supportCenter.addContact(c2); // A2 ile eşleşir (A2 BUSY olur)
        supportCenter.addContact(c3); // A3 ile eşleşir (A3 BUSY olur)

        // Müşteri 4 geliyor ama boşta temsilci olmadığı için KUYRUKTA BEKLER
        Contact c4 = new Contact("C4", "Zeynep", "Yıldız", Channel.WHATSAPP);
        supportCenter.addContact(c4);

        // 4. MESAİ BİTİMİ / TRANSFER SENARYOSU
        System.out.println("\n--- 2. TRANSFER VE MESAİ BİTİMİ SENARYOSU ---");
        // Ahmet'in (A1) mesaisi bitti, durumu OFFLINE yapılıyor ve işi transfer ediliyor
        agent1.setStatus(AgentStatus.OFFLINE);
        supportCenter.transferAgent("A1");

        // Ali (A3) çağrısını bitirdi ve ONLINE oldu
        supportCenter.getSession().closeSession("CHAT_" + System.currentTimeMillis()); // Örnek oturum kapatma
        agent3.setStatus(AgentStatus.ONLINE);
        supportCenter.getQueueManager().addAvailableAgent(agent3); // Otomatik kuyruktakileri işler
    }
}