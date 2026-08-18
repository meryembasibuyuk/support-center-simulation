package com.alarm.rules;

import com.alarm.Alarm;
import com.alarm.AlarmRule;
import com.alarm.AlarmSeverity;
import com.event.DomainEvent;
import com.event.SessionEndedEvent;
import com.event.TransferEvent;
import com.model.Agent;
import com.model.Session;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ayni session kisa surede birden fazla kez transfer edilirse
 * ("hot potato" - musteri agent'lar arasinda surukleniyor) CRITICAL alarm uretir.
 *
 * NOT (baglanti kuruldu): clearSession() onceden yaziliydi ama hicbir yerden
 * cagirilmiyordu ("Su an EventBus'a ayrica abone edilmedi" yorumu artik
 * gecersiz). SessionLifecycleService.endSession() her session bittiginde
 * SessionEndedEvent yayinladigi ve AlarmService zaten HER event'i tum
 * kurallara (bu kural dahil) evaluate() ile ilettigi icin, ayri bir abonelik
 * kurmaya gerek yok - evaluate() artik SessionEndedEvent'i yakalayip
 * clearSession() cagiriyor. Boylece bitmis bir session'in transfer gecmisi,
 * zaman penceresi dolana kadar beklemeden hemen temizleniyor (bellek ve
 * dogruluk kazanimi: bitmis bir session'in eski transfer sayisi, ayni
 * sessionId yeniden kullanilirsa - id ureticisi degisirse - yanlislikla
 * yeni bir session'a miras kalmaz).
 */
public class ExcessiveTransferAlarmRule implements AlarmRule {
    private final int maxTransfers;
    private final Duration window;
    private final ConcurrentHashMap<String, List<Instant>> transfersBySession = new ConcurrentHashMap<>();

    public ExcessiveTransferAlarmRule(int maxTransfers, Duration window) {
        this.maxTransfers = maxTransfers;
        this.window = window;
    }

    @Override
    public Optional<Alarm> evaluate(DomainEvent event) {
        if (event instanceof SessionEndedEvent) {
            SessionEndedEvent endedEvent = (SessionEndedEvent) event;
            Session session = endedEvent.getSession();
            if (session != null) {
                clearSession(session.getSessionId());
            }
            return Optional.empty();
        }

        if (event instanceof TransferEvent) {
            TransferEvent transferEvent = (TransferEvent) event;
            Session session = transferEvent.getSession();
            Agent from = transferEvent.getFromAgent();
            Agent to = transferEvent.getToAgent();
            Instant now = transferEvent.occurredAt();
            String sessionId = session.getSessionId();

            List<Instant> history = transfersBySession.computeIfAbsent(
                sessionId, k -> new CopyOnWriteArrayList<>()
            );
            history.add(now);
            history.removeIf(t -> Duration.between(t, now).compareTo(window) > 0);

            int size = history.size();

          
            if (size == 0) {
                transfersBySession.remove(sessionId, history);
            }

            if (size >= maxTransfers) {
                return Optional.of(new Alarm(
                    UUID.randomUUID().toString(),
                    AlarmSeverity.CRITICAL,
                    String.format("Session %s son %d saniyede %d kez transfer edildi (son: %s -> %s)",
                        sessionId,
                        window.getSeconds(),
                        size,
                        from != null ? from.getAgentId() : "N/A",
                        to != null ? to.getAgentId() : "N/A"
                    ),
                    "TransferEvent",
                    now
                ));
            }
        }
        return Optional.empty();
    }

    
    public void clearSession(String sessionId) {
        transfersBySession.remove(sessionId);
    }
}