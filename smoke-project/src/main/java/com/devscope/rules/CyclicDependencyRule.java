package com.devscope.rules;

import com.devscope.engine.graph.GraphAnalyzer;
import com.devscope.engine.rulesengine.RuleContext;
import com.devscope.engine.rulesengine.RuleResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CyclicDependencyRule implements Rule {

    private final GraphAnalyzer graphAnalyzer;

    public CyclicDependencyRule(GraphAnalyzer graphAnalyzer) {
        this.graphAnalyzer = graphAnalyzer;
    }

    @Override
    public String name() {
        return "CyclicDependencyRule";
    }

    @Override
    public List<RuleResult.RuleViolation> evaluate(RuleContext context) {
        List<RuleResult.RuleViolation> violations = new ArrayList<>();
        List<List<String>> cycles = graphAnalyzer.detectCycles(context.getDependencyGraph());
        for (List<String> cycle : cycles) {
            RuleResult.RuleViolation violation = new RuleResult.RuleViolation();
            violation.setCode("V-CYCLE-001");
            violation.setType("CYCLIC_DEPENDENCY");
            violation.setSeverity("HIGH");
            violation.setTitle("Cyclic dependency detected");
            violation.setDescription("Detected a dependency cycle in the class graph.");
            violation.setAffectedClasses(cycle);
            violation.setRecommendation("Break cycle by introducing stable interfaces or event boundaries.");
            violations.add(violation);
        }
        return violations;
    }
}
