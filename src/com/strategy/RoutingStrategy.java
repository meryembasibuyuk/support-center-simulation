package com.strategy;

import com.model.Agent;
import java.util.List;

public interface RoutingStrategy {
    // Müsait temsilciler arasından kurala uygun olanı seçer
    Agent selectAgent(List<Agent> agents);
}