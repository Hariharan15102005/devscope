package com.devscope.rules;

import com.devscope.engine.rulesengine.RuleContext;
import com.devscope.engine.rulesengine.RuleResult;

import java.util.List;

public interface Rule {
    String name();

    List<RuleResult.RuleViolation> evaluate(RuleContext context);
}
