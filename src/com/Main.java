package com;

import com.model.Agent;
import com.model.Message;
import com.service.SupportCenter;
import com.strategy.FirstAvailableRoutingStrategy;


public class Main {
    public static void main(String[] args) {
        // 1. Destek merkezini 'İlk Müsait Temsilci' stratejisi ile başlat
        SupportCenter supportCenter = new SupportCenter(new FirstAvailableRoutingStrategy());

        // 2. Başlangıçta 3 temsilci ekle
        Agent agent1 = new Agent("A1", "Temsilci 1");
        Agent agent2 = new Agent("A2", "Temsilci 2");
        Agent agent3 = new Agent("A3", "Temsilci 3");

        supportCenter.addAgent(agent1);
        supportCenter.addAgent(agent2);
        supportCenter.addAgent(agent3);

        System.out.println("=== SİMÜLASYON BAŞLIYOR ===");

        // 3. Akış Senaryosu (Ödevdeki gibi aralarda tamamlanma olan akış)
        Message m1 = new Message("M1", "Şifremi unuttum.");
        Message m2 = new Message("M2", "Fatura detayımı öğrenmek istiyorum.");
        Message m3 = new Message("M3", "Kargo nerede kaldı?");
        Message m4 = new Message("M4", "Ürün arızalı geldi.");
        Message m5 = new Message("M5", "İade talebi oluşturmak istiyorum.");

        // Adım adımları simüle et
        supportCenter.receiveMessage(m1); // A1 alır
        supportCenter.receiveMessage(m2); // A2 alır
        supportCenter.receiveMessage(m3); // A3 alır
        
        // Tüm temsilciler doldu, bu mesaj kuyruğa girecek:
        supportCenter.receiveMessage(m4); // Kuyruk: M4

        // A1 işini bitirsin -> Kuyruktaki M4'ü otomatik almalı
        supportCenter.completeJob(agent1);

        // Yeni mesaj gelsin -> Yine kuyruğa girmeli
        supportCenter.receiveMessage(m5); // Kuyruk: M5

        // Temsilciler işlerini bitirsin
        supportCenter.completeJob(agent2); // M5'i A2 alır
        supportCenter.completeJob(agent3);
        supportCenter.completeJob(agent1);
        supportCenter.completeJob(agent2);

        // 4. Özet Çıktı
        supportCenter.printSummary();
    }
}