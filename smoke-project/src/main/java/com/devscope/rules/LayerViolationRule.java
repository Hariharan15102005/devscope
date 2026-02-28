package com.devscope.rules;

import com.devscope.engine.parser.ParsedJavaClass;
import com.devscope.engine.rulesengine.RuleContext;
import com.devscope.engine.rulesengine.RuleResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LayerViolationRule implements Rule {

    @Override
    public String name() {
        return "LayerViolationRule";
    }

    @Override
    public List<RuleResult.RuleViolation> evaluate(RuleContext context) {
        List<RuleResult.RuleViolation> violations = new ArrayList<>();
        for (ParsedJavaClass parsedClass : context.getClasses()) {
            boolean serviceImportingController = parsedClass.getPackageName().contains("service")
                    && parsedClass.getImports().stream().anyMatch(item -> item.contains(".controller."));

            if (serviceImportingController) {
                RuleResult.RuleViolation violation = new RuleResult.RuleViolation();
                violation.setCode("V-LAYER-001");
                violation.setType("LAYER_VIOLATION");
                violation.setSeverity("MEDIUM");
                violation.setTitle("Service depends on controller layer");
                violation.setDescription("Detected controller dependency inside service package.");
                violation.setAffectedClasses(List.of(parsedClass.getFullName()));
                violation.setRecommendation("Move shared behavior to a lower-level abstraction used by both layers.");
                violations.add(violation);
            }
        }
        return violations;
    }
}
