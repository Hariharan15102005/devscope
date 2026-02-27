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
        int score = loc + (methods * 3) + (dependencies * 4);
        if (score >= 160) {
            return "HIGH";
        }
        if (score >= 90) {
            return "MEDIUM";
        }
        return "LOW";
    }
}
