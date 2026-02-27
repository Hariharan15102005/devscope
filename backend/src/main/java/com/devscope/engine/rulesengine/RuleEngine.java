package com.devscope.engine.rulesengine;

import com.devscope.rules.Rule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleEngine {

    private final List<Rule> rules;

    public RuleEngine(List<Rule> rules) {
        this.rules = rules;
    }

    public RuleResult execute(RuleContext context) {
        RuleResult result = new RuleResult();
        for (Rule rule : rules) {
            result.addViolations(rule.evaluate(context));
        }
        return result;
    }
}
