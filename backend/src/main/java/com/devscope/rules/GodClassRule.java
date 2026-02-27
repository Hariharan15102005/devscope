package com.devscope.rules;

import com.devscope.engine.metrics.MetricsCalculator;
import com.devscope.engine.metrics.MetricSnapshot;
import com.devscope.engine.rulesengine.RuleContext;
import com.devscope.engine.rulesengine.RuleResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GodClassRule implements Rule {

    @Override
    public String name() {
        return "GodClassRule";
    }

    @Override
    public List<RuleResult.RuleViolation> evaluate(RuleContext context) {
        List<RuleResult.RuleViolation> violations = new ArrayList<>();
        for (MetricSnapshot metric : context.getMetricsByClass().values()) {
            if (metric.getLinesOfCode() > 500 || metric.getMethodCount() > 25) {
                RuleResult.RuleViolation violation = new RuleResult.RuleViolation();
                violation.setCode("V-GOD-001");
                violation.setType("GOD_CLASS");
                violation.setSeverity("HIGH");
                violation.setTitle("Potential god class detected");
                violation.setDescription("Class has unusually high size or method volume.");
                violation.setAffectedClasses(List.of(metric.getClassName()));
                violation.setRecommendation("Split responsibilities into focused collaborators.");
                violations.add(violation);
            }
        }
        return violations;
    }
}
