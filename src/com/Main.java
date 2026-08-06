package com;

import com.model.Agent;
import com.model.Contact;
import com.model.Message;
import com.service.SupportCenter;
import com.strategy.LeastBusyRoutingStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== DESTEK MERKEZİ SİMÜLASYONU BAŞLIYOR ===\n");

        SupportCenter supportCenter = new SupportCenter(new LeastBusyRoutingStrategy());

        // Müşteri Temsilcileri
        Agent agent1 = new Agent("A1", "Temsilci 1");
        Agent agent2 = new Agent("A2", "Temsilci 2");
        Agent agent3 = new Agent("A3", "Temsilci 3");

        supportCenter.addAgent(agent1);
        supportCenter.addAgent(agent2);
        supportCenter.addAgent(agent3);

        // İletişim Kişileri (Müşteriler)
        Contact c1 = new Contact("C1", "Ahmet Yilmaz", "ahmet@mail.com");
        Contact c2 = new Contact("C2", "Ayse Kaya", "ayse@mail.com");
        Contact c3 = new Contact("C3", "Mehmet Demir", "mehmet@mail.com");
        Contact c4 = new Contact("C4", "Zeynep Celik", "zeynep@mail.com");

        System.out.println("--- MESAJLAR GELİYOR ---");
        supportCenter.receiveMessage(new Message("M1", "Siparis durumu sorgulama", c1));
        supportCenter.receiveMessage(new Message("M2", "Kargo gecikmesi sikayeti", c2));
        supportCenter.receiveMessage(new Message("M3", "Urun iade talebi", c3));
        supportCenter.receiveMessage(new Message("M4", "Urun stok bilgisi", c4));

        supportCenter.printCurrentStatus();

        System.out.println("\n--- İŞ TAMAMLAMA VE KUYRUK YÖNETİMİ ---");
        supportCenter.completeTask("A1");

        supportCenter.printSummary();
    }
}