package com.model;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * State Pattern'in hafif bir versiyonu: her durumun hangi durumlara
 * gecebilecegini bir gecis tablosuyla garanti altina alir. Bu sayede
 * orijinal koddaki "ONBREAK'e giren bir temsilci sonsuza kadar orada kalir"
 * ve "OFFLINE bir temsilci dogrudan BUSY yapilabilir" gibi tutarsizliklar
 * derleme zamaninda degil ama calisma zamaninda garanti altina alinmis olur.
 */
public enum AgentStatus {
    ONLINE,
    BUSY,
    OFFLINE,
    ONBREAK;

    private static final Map<AgentStatus, Set<AgentStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(AgentStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(ONLINE, EnumSet.of(BUSY, ONBREAK, OFFLINE));
        ALLOWED_TRANSITIONS.put(BUSY, EnumSet.of(ONLINE, OFFLINE));
        ALLOWED_TRANSITIONS.put(ONBREAK, EnumSet.of(ONLINE, OFFLINE));
        ALLOWED_TRANSITIONS.put(OFFLINE, EnumSet.of(ONLINE));
    }

    public boolean canTransitionTo(AgentStatus target) {
        if (this == target) {
            return true;
        }
        Set<AgentStatus> allowed = ALLOWED_TRANSITIONS.get(this);
        return allowed != null && allowed.contains(target);
    }
}