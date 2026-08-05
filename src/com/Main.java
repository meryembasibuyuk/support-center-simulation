package com;

import com.model.Agent;
import com.model.Message;
import com.service.SupportCenter;
import com.strategy.LeastBusyRoutingStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== DESTEK MERKEZİ SİMÜLASYONU BAŞLIYOR ===\n");

        SupportCenter supportCenter = new SupportCenter(new LeastBusyRoutingStrategy());

        Agent agent1 = new Agent("A1", "Temsilci 1");
        Agent agent2 = new Agent("A2", "Temsilci 2");
        Agent agent3 = new Agent("A3", "Temsilci 3");

        supportCenter.addAgent(agent1);
        supportCenter.addAgent(agent2);
        supportCenter.addAgent(agent3);

        // 3. Yoğun Mesaj Akışı (Kuyruk Testi)
        System.out.println("--- MESAJLAR GELİYOR ---");
        supportCenter.receiveMessage(new Message("M1", "Sipariş durumu sorgulama"));
        supportCenter.receiveMessage(new Message("M2", "Kargo gecikmesi şikayeti"));
        supportCenter.receiveMessage(new Message("M3", "Ürün iade talebi"));
        supportCenter.receiveMessage(new Message("M4", "Ürün stok bilgisi"));

        // 4. Anlık Durumu Raporla
        supportCenter.printCurrentStatus();

        // 5. Bir temsilcinin işini bitirmesi ve kuyruktaki mesajın otomatik atanması
        System.out.println("\n--- İŞ TAMAMLAMA VE KUYRUK YÖNETİMİ ---");
        supportCenter.completeTask("A1");

        // 6. Son İstatistikleri Yazdır
        supportCenter.printSummary();
    }
}