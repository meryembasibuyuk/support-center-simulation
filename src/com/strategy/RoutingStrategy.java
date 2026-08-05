package com.strategy;

import com.model.Agent;
import java.util.List;

public interface RoutingStrategy {
    /**
     * Temsilci listesi içinden mesajın yönlendirileceği en uygun temsilciyi seçer.
     * @param agents Sistemdeki tüm temsilcilerin listesi
     * @return Seçilen temsilci veya tüm temsilciler meşgulse null
     */
    Agent route(List<Agent> agents);
}