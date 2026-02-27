package com.devscope.service;

import com.devscope.dto.response.CompareResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompareService {

    public CompareResponse compare(String baseAnalysisId, String targetAnalysisId) {
        int seed = Math.abs((baseAnalysisId + "->" + targetAnalysisId).hashCode());

        int violationChange = (seed % 15) - 7;
        int cycleChange = (seed % 7) - 3;
        int riskChange = (seed % 21) - 10;
        double complexityBefore = 18 + (seed % 11);
        double avgComplexityChange = ((seed % 120) - 60) / 10.0;
        double complexityAfter = Math.max(1, complexityBefore + avgComplexityChange);

        CompareResponse.Summary summary = new CompareResponse.Summary();
        summary.setViolationChange(violationChange);
        summary.setAvgComplexityChange(Math.round(avgComplexityChange * 10.0) / 10.0);
        summary.setCycleChange(cycleChange);
        summary.setRiskChange(riskChange);
        summary.setComplexityBefore(Math.round(complexityBefore * 10.0) / 10.0);
        summary.setComplexityAfter(Math.round(complexityAfter * 10.0) / 10.0);

        List<CompareResponse.MetricDifference> metricDifferences = List.of(
                createDiff("com.devscope.engine.graph.DependencyResolver", seed + 11),
                createDiff("com.devscope.engine.rulesengine.RuleOrchestrator", seed + 23),
                createDiff("com.devscope.service.InsightAggregationService", seed + 41),
                createDiff("com.devscope.service.StructureService", seed + 65),
                createDiff("com.devscope.service.DashboardService", seed + 87)
        );

        int added = Math.max(0, violationChange + 6);
        int removed = Math.max(0, 6 - violationChange);
        CompareResponse.ViolationDifferences violationDifferences = new CompareResponse.ViolationDifferences(added, removed);

        CompareResponse response = new CompareResponse();
        response.setSummary(summary);
        response.setMetricDifferences(metricDifferences);
        response.setViolationDifferences(violationDifferences);
        return response;
    }

    public List<String> getAvailableAnalyses(String projectId) {
        return List.of(
                projectId + "-analysis-2026-01-15",
                projectId + "-analysis-2026-01-29",
                projectId + "-analysis-2026-02-12",
                projectId + "-analysis-2026-02-26"
        );
    }

    private static CompareResponse.MetricDifference createDiff(String className, int seed) {
        int locDiff = (seed % 41) - 20;
        int dependencyDiff = (seed % 9) - 4;

        String riskBefore = pickRisk(seed);
        String riskAfter = shiftRisk(riskBefore, locDiff + dependencyDiff);
        return new CompareResponse.MetricDifference(className, locDiff, dependencyDiff, riskBefore, riskAfter);
    }

    private static String pickRisk(int seed) {
        int value = Math.abs(seed % 3);
        if (value == 0) return "LOW";
        if (value == 1) return "MEDIUM";
        return "HIGH";
    }

    private static String shiftRisk(String riskBefore, int trend) {
        int index = switch (riskBefore) {
            case "LOW" -> 0;
            case "MEDIUM" -> 1;
            default -> 2;
        };

        int delta = trend > 8 ? 1 : trend < -8 ? -1 : 0;
        int next = Math.min(2, Math.max(0, index + delta));

        return switch (next) {
            case 0 -> "LOW";
            case 1 -> "MEDIUM";
            default -> "HIGH";
        };
    }
}
