package com.event;

import java.util.logging.Logger;

public class LoggingEventListener implements EventListener {
    private static final Logger LOGGER = Logger.getLogger("SupportCenter");

    @Override
    public void onEvent(DomainEvent event) {
        if (event instanceof SessionStartedEvent) {
            SessionStartedEvent e = (SessionStartedEvent) event;
            LOGGER.info("[OTURUM BASLADI] " + e.getSession().getSessionId()
                    + " | Musteri: " + e.getSession().getContact().getName()
                    + " -> Temsilci: " + e.getSession().getAgent().getName());
        } else if (event instanceof SessionEndedEvent) {
            SessionEndedEvent e = (SessionEndedEvent) event;
            LOGGER.info("[OTURUM BITTI] " + e.getSession().getSessionId() + " | Sebep: " + e.getReason());
        } else if (event instanceof AgentOfflineEvent) {
            AgentOfflineEvent e = (AgentOfflineEvent) event;
            LOGGER.info("[MESAI BITISI] Temsilci " + e.getAgent().getName() + " offline oldu.");
        } else if (event instanceof AgentNoAnswerEvent) {
            AgentNoAnswerEvent e = (AgentNoAnswerEvent) event;
            LOGGER.info("[PAS GECTI] " + e.getAgent().getName() + " yanit vermedi, molaya alindi.");
        } else if (event instanceof TransferEvent) {
            TransferEvent e = (TransferEvent) event;
            LOGGER.info("[TRANSFER] " + e.getSession().getSessionId() + " : "
                    + e.getFromAgent().getName() + " -> " + e.getToAgent().getName());
        } else {
            LOGGER.info("[EVENT] " + event.getClass().getSimpleName());
        }
    }
}