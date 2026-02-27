package com.devscope.engine.rulesengine;

import com.devscope.engine.graph.DependencyGraph;
import com.devscope.engine.metrics.MetricSnapshot;
import com.devscope.engine.metrics.MetricsCalculator;
import com.devscope.engine.parser.ParsedJavaClass;

import java.util.List;
import java.util.Map;

public class RuleContext {
    private final List<ParsedJavaClass> classes;
    private final DependencyGraph dependencyGraph;
    private final Map<String, MetricSnapshot> metricsByClass;

    public RuleContext(
            List<ParsedJavaClass> classes,
            DependencyGraph dependencyGraph,
                Map<String, MetricSnapshot> metricsByClass
    ) {
        this.classes = classes;
        this.dependencyGraph = dependencyGraph;
        this.metricsByClass = metricsByClass;
    }

    public List<ParsedJavaClass> getClasses() {
        return classes;
    }

    public DependencyGraph getDependencyGraph() {
        return dependencyGraph;
    }

    public Map<String, MetricSnapshot> getMetricsByClass() {
        return metricsByClass;
    }
}
