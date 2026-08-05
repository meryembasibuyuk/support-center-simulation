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
// 3. Akış Senaryosu (Temsilci sayısından fazla mesaj göndererek kuyruğu test etme)
        supportCenter.receiveMessage(new Message("M1", "Şifremi unuttum, yardım edebilir misiniz?"));
        supportCenter.receiveMessage(new Message("M2", "İade talebi oluşturmak istiyorum."));
        supportCenter.receiveMessage(new Message("M3", "Kargom nerede kaldı?"));
        
        // Bu mesajlar müsait temsilci kalmadığı için kuyruğa (Queue) eklenecek
        supportCenter.receiveMessage(new Message("M4", "Fatura adresimi değiştirmek istiyorum."));
        supportCenter.receiveMessage(new Message("M5", "Ürün stok bilgisi öğrenebilir miyim?"));

        // Temsilciler işlerini bitirdikçe kuyruktakiler otomatik olarak atanacak
        supportCenter.completeJob(agent1); 
        supportCenter.completeJob(agent2); 
        
        supportCenter.receiveMessage(new Message("M6", "Üyelik iptali hakkında."));

        supportCenter.completeJob(agent3);
        supportCenter.completeJob(agent1);

        // 4. Özet Çıktısı
        supportCenter.printSummary();
    }
}
       