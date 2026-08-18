package com.alarm.rules;

import com.alarm.Alarm;
import com.alarm.AlarmRule;
import com.alarm.AlarmSeverity;
import com.event.AgentOfflineEvent;
import com.event.DomainEvent;
import com.model.Agent;

import java.util.Optional;
import java.util.UUID;

/**
 * Session'in bilinen "anormal" bir sebeple sonlanmasi durumunda alarm uretir.
 *
 * Su an kod tabaninda endSession() sadece SupportCenter.handleAgentOffline()
 * icinde cagriliyor - baska bir normal kapanis yolu (orn. musteri tarafindan
 * sonlandirma) henuz implemente edilmemis. Session ve event siniflarinda ayri
 * bir "reason" alani bulunmadigi icin, anormal sonlanma su an sadece
 * AgentOfflineEvent'in uretilmis olmasindan anlasilabiliyor.
 *
 * Ileride yeni anormal sonlanma sebepleri eklenirse (orn. Session'a bir
 * "reason" alani eklenmesi ya da yeni event tipleri gelmesi), bu sinifin
 * evaluate() metodu ona gore genisletilmeli - ornegin bir instanceof zinciri
 * ya da (reason alani eklenirse) bir ABNORMAL_REASONS set kontrolu ile.
 */
public class AbnormalSessionEndAlarmRule implements AlarmRule {

    @Override
    public Optional<Alarm> evaluate(DomainEvent event) {
        if (event instanceof AgentOfflineEvent) {
            AgentOfflineEvent offlineEvent = (AgentOfflineEvent) event;
            Agent agent = offlineEvent.getAgent();

            String message = "Session, temsilci " + agent.getAgentId()
                    + " offline oldugu icin anormal sekilde sonlandi.";

            return Optional.of(new Alarm(
                    UUID.randomUUID().toString(),
                    AlarmSeverity.WARNING,
                    message,
                    "AgentOfflineEvent",
                    event.occurredAt()
            ));
        }
        return Optional.empty();
    }
}