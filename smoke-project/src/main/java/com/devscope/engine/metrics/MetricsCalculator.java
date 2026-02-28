package com.devscope.engine.metrics;

import com.devscope.engine.graph.DependencyGraph;
import com.devscope.engine.parser.ParsedJavaClass;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class MetricsCalculator {

    public Map<String, MetricSnapshot> calculateMetrics(List<ParsedJavaClass> classes, DependencyGraph graph) {
        Map<String, MetricSnapshot> snapshots = new LinkedHashMap<>();
        for (ParsedJavaClass parsedClass : classes) {
            int dependencyCount = graph.dependenciesOf(parsedClass.getFullName()).size();
            int methodCount = parsedClass.getMethods().size();
            int linesOfCode = parsedClass.getLinesOfCode();

            MetricSnapshot snapshot = new MetricSnapshot();
            snapshot.setClassName(parsedClass.getFullName());
            snapshot.setLinesOfCode(linesOfCode);
            snapshot.setMethodCount(methodCount);
            snapshot.setDependencyCount(dependencyCount);
            snapshot.setRiskLevel(assignRiskLevel(linesOfCode, methodCount, dependencyCount));
            snapshots.put(parsedClass.getFullName(), snapshot);
        }
        return snapshots;
    }

    public String assignRiskLevel(int loc, int methods, int dependencies) {
        if (loc > 300 || dependencies > 15) {
            return "HIGH";
        }
        if (loc > 150) {
            return "MEDIUM";
        }
        return "LOW";
    }
}
