package com;

import com.alarm.AlarmService;
import com.alarm.ConsoleAlarmNotifier;
import com.alarm.rules.AgentOfflineAlarmRule;
import com.alarm.rules.OrphanedSessionAlarmRule;
import com.alarm.rules.SlowSessionStartAlarmRule;
import com.alarm.rules.EventFrequencyAlarmRule;
import com.alarm.rules.QueueOverloadAlarmRule;
import com.alarm.rules.AgentNoAnswerAlarmRule;
import com.alarm.rules.ExcessiveTransferAlarmRule;
import com.alarm.rules.AbnormalSessionEndAlarmRule;
import com.event.AgentOfflineEvent;
import com.event.AgentNoAnswerEvent;
import com.model.*;
import com.service.SupportCenter;
import com.strategy.AdvancedRoutingStrategy;
import com.strategy.ChannelSpecialistRoutingStrategy;
import com.strategy.FirstAvailableRoutingStrategy;
import com.strategy.LeastBusyRoutingStrategy;
import com.strategy.RoutingStrategy;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== DESTEK MERKEZI SIMULASYONU (REFAKTOR EDILMIS) ===\n");

        SupportCenter supportCenter = new SupportCenter(new AdvancedRoutingStrategy());

        // --- ALARM SISTEMI KURULUMU ---
        AlarmService alarmService = new AlarmService(supportCenter.getEventBus());

        alarmService.addRule(new AgentOfflineAlarmRule());
        alarmService.addRule(new OrphanedSessionAlarmRule());
        alarmService.addRule(new EventFrequencyAlarmRule(
                AgentOfflineEvent.class, 3, Duration.ofSeconds(30)
        ));
        alarmService.addRule(new SlowSessionStartAlarmRule(Duration.ofMillis(500)));
        alarmService.addRule(new QueueOverloadAlarmRule());
        alarmService.addRule(new AgentNoAnswerAlarmRule());
        alarmService.addRule(new EventFrequencyAlarmRule(
                AgentNoAnswerEvent.class, 3, Duration.ofMinutes(5)
        ));
        alarmService.addRule(new ExcessiveTransferAlarmRule(3, Duration.ofMinutes(10)));
        alarmService.addRule(new AbnormalSessionEndAlarmRule());

        alarmService.addNotifier(new ConsoleAlarmNotifier());
        // --------------------------------

        Agent agent1 = new Agent("A1", "Ahmet", "Yilmaz", 2);
        agent1.addSupportedChannel(Channel.WHATSAPP);
        agent1.addSupportedChannel(Channel.INSTAGRAM);

        Agent agent2 = new Agent("A2", "Fatma", "Kaya", 2);
        agent2.addSupportedChannel(Channel.TELEGRAM);
        agent2.addSupportedChannel(Channel.FACEBOOK);

        // Transfer testinde hedef temsilci olarak kullanilacak.
        Agent agent3 = new Agent("A3", "Zeynep", "Demir", 2);
        agent3.addSupportedChannel(Channel.WHATSAPP);
        agent3.addSupportedChannel(Channel.FACEBOOK);

        supportCenter.addAgent(agent1);
        supportCenter.addAgent(agent2);
        supportCenter.addAgent(agent3);

        Contact c1 = new Contact("C1", "Mehmet", "Aydin", Channel.INSTAGRAM, false);
        Contact c2 = new Contact("C2", "Ayse", "Sahin", Channel.TELEGRAM, false);

        supportCenter.addContact(c1);
        supportCenter.addContact(c2);

        Contact c3 = new Contact("C3", "Can", "Ozturk", Channel.INSTAGRAM, true);
        supportCenter.addContact(c3);

        Contact c1Duplicate = new Contact("C1", "Mehmet", "Aydin", Channel.FACEBOOK, false);
        supportCenter.addContact(c1Duplicate);

        System.out.println("\n--- MOLA SENARYOSU ---");
        agent2.transitionTo(AgentStatus.ONBREAK);

        System.out.println("\n--- MOLADAN DONUS SENARYOSU ---");
        supportCenter.returnAgentFromBreak(agent2);

        // --- TRANSFER TESTI: ExcessiveTransferAlarmRule tetiklenmesi icin
        // ayni session'i kisa surede 3 kez transfer ediyoruz (hot potato). ---
        System.out.println("\n--- ASIRI TRANSFER SENARYOSU ---");
        List<Session> ahmetsSessions = agent1.getActiveSessions();
        if (!ahmetsSessions.isEmpty()) {
            Session mehmetSession = ahmetsSessions.get(0);
            supportCenter.transferSession(mehmetSession, agent3); // agent1 -> agent3 (1)
            supportCenter.transferSession(mehmetSession, agent1); // agent3 -> agent1 (2)
            supportCenter.transferSession(mehmetSession, agent3); // agent1 -> agent3 (3) -> ALARM
        }

        // --- CEVAPSIZ CAGRI TESTI: AgentNoAnswerAlarmRule + frekans alarmi ---
        System.out.println("\n--- CEVAPSIZ CAGRI SENARYOSU ---");
        Contact tempContact1 = new Contact("C10", "Deniz", "Koc", Channel.TELEGRAM, false);
        Contact tempContact2 = new Contact("C11", "Elif", "Turan", Channel.TELEGRAM, false);
        Contact tempContact3 = new Contact("C12", "Burak", "Celik", Channel.TELEGRAM, false);
        supportCenter.handleAgentNoAnswer(agent2, tempContact1); // 1
        supportCenter.handleAgentNoAnswer(agent2, tempContact2); // 2
        supportCenter.handleAgentNoAnswer(agent2, tempContact3); // 3 -> frekans ALARM

        System.out.println("\n--- MESAI BITISI SENARYOSU ---");
        supportCenter.handleAgentOffline(agent1);

        // --- KUYRUK ASIRI YUKLENME TESTI: ayri, dusuk esikli bir
        // SupportCenter ornegi ile QueueOverloadAlarmRule test ediliyor. ---
        System.out.println("\n--- KUYRUK ASIRI YUKLENME SENARYOSU (ayri ornek, esik=2) ---");
        SupportCenter overloadDemoCenter = new SupportCenter(new AdvancedRoutingStrategy(), 2);
        AlarmService overloadAlarmService = new AlarmService(overloadDemoCenter.getEventBus());
        overloadAlarmService.addRule(new QueueOverloadAlarmRule());
        overloadAlarmService.addNotifier(new ConsoleAlarmNotifier());
        overloadDemoCenter.addContact(new Contact("Q1", "Test1", "User", Channel.WHATSAPP, false));
        overloadDemoCenter.addContact(new Contact("Q2", "Test2", "User", Channel.WHATSAPP, false)); // esik(2)->WARNING
        overloadDemoCenter.addContact(new Contact("Q3", "Test3", "User", Channel.WHATSAPP, false));
        overloadDemoCenter.addContact(new Contact("Q4", "Test4", "User", Channel.WHATSAPP, false)); // esik*2(4)->CRITICAL

        // --- ROUTING STRATEJI KARSILASTIRMA SENARYOSU: daha once hicbir yerden
        // cagirilmayan FirstAvailable / ChannelSpecialist / LeastBusy stratejileri
        // burada kendi izole (SupportCenter'a bagli olmayan) ornek verisiyle
        // dogrudan calistirilip AdvancedRoutingStrategy ile karsilastiriliyor.
        // Boylece hem "olu kod" olmaktan cikiyorlar hem de aralarindaki fark
        // somut olarak gorulebiliyor. ---
        System.out.println("\n--- ROUTING STRATEJI KARSILASTIRMA SENARYOSU ---");

        Agent specialistAgent = new Agent("SP1", "Uzman", "Temsilci", 2);
        specialistAgent.addSupportedChannel(Channel.WHATSAPP);

        Agent generalistAgent = new Agent("GN1", "Genel", "Temsilci", 2);
        generalistAgent.addSupportedChannel(Channel.WHATSAPP);
        generalistAgent.addSupportedChannel(Channel.TELEGRAM);
        generalistAgent.addSupportedChannel(Channel.FACEBOOK);
        generalistAgent.addSupportedChannel(Channel.INSTAGRAM);

        // generalistAgent'i onceden mesgul ediyoruz ki LeastBusyRoutingStrategy'nin
        // farkli bir secim yaptigini gorebilelim.
        Contact busyingContact = new Contact("BZ1", "Mesgul", "Yapan", Channel.WHATSAPP, false);
        generalistAgent.addSession(new Session("S-demo-busy", generalistAgent, busyingContact));

        // Kasitli olarak genelist temsilciyi listede once koyuyoruz: boylece
        // "ilk uygun olani sec" mantigi ile "uzmanlasmis/en az mesgul olani sec"
        // mantigi arasindaki fark net gorulur.
        List<Agent> comparisonAgents = Arrays.asList(generalistAgent, specialistAgent);
        Contact demoContact = new Contact("DM1", "Ornek", "Musteri", Channel.WHATSAPP, false);

        RoutingStrategy[] strategies = {
                new FirstAvailableRoutingStrategy(),
                new ChannelSpecialistRoutingStrategy(),
                new LeastBusyRoutingStrategy(),
                new AdvancedRoutingStrategy()
        };

        for (RoutingStrategy strategy : strategies) {
            Agent chosen = strategy.route(comparisonAgents, demoContact, null);
            System.out.println(strategy.getClass().getSimpleName() + " -> "
                    + (chosen != null
                        ? chosen.getName() + " " + chosen.getSurname() + " (ID: " + chosen.getAgentId() + ")"
                        : "uygun temsilci yok"));
        }

        System.out.println("\n=== SIMULASYON TAMAMLANDI ===");
    }
}