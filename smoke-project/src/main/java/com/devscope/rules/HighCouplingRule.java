package com.devscope.rules;

import com.devscope.engine.graph.GraphAnalyzer;
import com.devscope.engine.rulesengine.RuleContext;
import com.devscope.engine.rulesengine.RuleResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class HighCouplingRule implements Rule {

    private final GraphAnalyzer graphAnalyzer;

    public HighCouplingRule(GraphAnalyzer graphAnalyzer) {
        this.graphAnalyzer = graphAnalyzer;
    }

    @Override
    public String name() {
        return "HighCouplingRule";
    }

    @Override
    public List<RuleResult.RuleViolation> evaluate(RuleContext context) {
        List<RuleResult.RuleViolation> violations = new ArrayList<>();
        Set<String> highCouplingNodes = graphAnalyzer.findHighCouplingNodes(context.getDependencyGraph(), 8);
        for (String className : highCouplingNodes) {
            RuleResult.RuleViolation violation = new RuleResult.RuleViolation();
            violation.setCode("V-COUPLING-001");
            violation.setType("HIGH_COUPLING");
            violation.setSeverity("MEDIUM");
            violation.setTitle("High coupling detected");
            violation.setDescription("Class has excessive incoming/outgoing dependency connections.");
            violation.setAffectedClasses(List.of(className));
            violation.setRecommendation("Introduce interfaces and reduce direct concrete dependencies.");
            violations.add(violation);
        }
        return violations;
    }
}
