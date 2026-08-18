package com.alarm;

import com.event.DomainEvent;
import com.event.EventBus;
import com.event.EventListener;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlarmService implements EventListener {
   
    private final List<AlarmRule> rules = new CopyOnWriteArrayList<>();
    private final List<AlarmNotifier> notifiers = new CopyOnWriteArrayList<>();

    public AlarmService(EventBus eventBus) {
        eventBus.subscribe(this);
    }

    public void addRule(AlarmRule rule) {
        rules.add(rule);
    }

    public void addNotifier(AlarmNotifier notifier) {
        notifiers.add(notifier);
    }

    @Override
    public void onEvent(DomainEvent event) {
        for (AlarmRule rule : rules) {
            try {
                Optional<Alarm> alarm = rule.evaluate(event);
                alarm.ifPresent(this::fireAlarm);
            } catch (Exception e) {
                // Bir kuralin hata vermesi diger kurallarin calismasini engellememeli.
                // TODO: gercek bir loglama kutuphanesi (SLF4J vb.) eklenince buraya tasinacak.
                System.err.println("[AlarmService] Kural calisirken hata olustu: "
                        + rule.getClass().getSimpleName() + " - " + e);
            }
        }
    }

    private void fireAlarm(Alarm alarm) {
        for (AlarmNotifier notifier : notifiers) {
            try {
                notifier.notify(alarm);
            } catch (Exception e) {
                // Bir notifier'in hata vermesi diger notifier'lara alarmin
                // ulasmasini engellememeli (orn. e-posta gönderimi patlarsa
                // konsol bildirimi yine de calismali).
                System.err.println("[AlarmService] Notifier hata verdi: "
                        + notifier.getClass().getSimpleName() + " - " + e);
            }
        }
    }
}