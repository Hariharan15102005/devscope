package com.devscope.engine.insight;

import com.devscope.engine.metrics.MetricSnapshot;
import com.devscope.engine.rulesengine.RuleResult;
import org.springframework.stereotype.Component;

@Component
public class PriorityEngine {

    public String assignImpact(RuleResult.RuleViolation violation, MetricSnapshot metricSnapshot) {
        if ("HIGH".equalsIgnoreCase(violation.getSeverity())) {
            return "HIGH";
        }
        if (metricSnapshot != null && "HIGH".equalsIgnoreCase(metricSnapshot.getRiskLevel())) {
            return "HIGH";
        }
        if ("MEDIUM".equalsIgnoreCase(violation.getSeverity())) {
            return "MEDIUM";
        }
        return "LOW";
    }
}
