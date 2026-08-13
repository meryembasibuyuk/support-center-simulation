package com;

import com.model.*;
import com.service.SupportCenter;
import com.strategy.ChannelSpecialistRoutingStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== GELİŞMİŞ DESTEK MERKEZİ SİMÜLASYONU ===\n");

        SupportCenter supportCenter = new SupportCenter(new ChannelSpecialistRoutingStrategy());

        Agent agent1 = new Agent("A1", "Ahmet", "Yılmaz");
        agent1.addSupportedChannel(Channel.WHATSAPP);

        Agent agent2 = new Agent("A2", "Fatma", "Kaya");
        agent2.addSupportedChannel(Channel.FACEBOOK);

        Agent agent3 = new Agent("A3", "Ali", "Demir");
        agent3.addSupportedChannel(Channel.WHATSAPP);

        supportCenter.addAgent(agent1);
        supportCenter.addAgent(agent2);
        supportCenter.addAgent(agent3);

        Contact c1 = new Contact("C1", "Mehmet", "Aydın", Channel.WHATSAPP);
        Contact c2 = new Contact("C2", "Ayşe", "Şahin", Channel.FACEBOOK);
        Contact c3 = new Contact("C3", "Can", "Öztürk", Channel.WHATSAPP);

        supportCenter.addContact(c1);
        supportCenter.addContact(c2);
        supportCenter.addContact(c3);

        // 4 Parametreli güncel Contact kullanımı (contId, name, surname, channel)
        Contact c4 = new Contact("C4", "Zeynep", "Yıldız", Channel.WHATSAPP);
        supportCenter.addContact(c4);

        // Transfer Senaryosu
        agent1.setStatus(AgentStatus.OFFLINE);
        supportCenter.transferAgent("A1");
    }
}