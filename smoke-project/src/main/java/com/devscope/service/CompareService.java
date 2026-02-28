package com.devscope.service;

import com.devscope.dto.response.CompareResponse;
import com.devscope.model.AnalysisRun;
import com.devscope.repository.AnalysisRunRepository;
import com.devscope.repository.MetricRepository;
import com.devscope.repository.ViolationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompareService {

    private final AnalysisRunRepository analysisRunRepository;
    private final MetricRepository metricRepository;
    private final ViolationRepository violationRepository;

    public CompareService(
            AnalysisRunRepository analysisRunRepository,
            MetricRepository metricRepository,
            ViolationRepository violationRepository
    ) {
        this.analysisRunRepository = analysisRunRepository;
        this.metricRepository = metricRepository;
        this.violationRepository = violationRepository;
    }

    public CompareResponse compare(Long baseAnalysisId, Long targetAnalysisId) {
        var baseMetrics = metricRepository.findByAnalysisRun_Id(baseAnalysisId);
        var targetMetrics = metricRepository.findByAnalysisRun_Id(targetAnalysisId);
        var baseViolations = violationRepository.findByAnalysisRun_Id(baseAnalysisId);
        var targetViolations = violationRepository.findByAnalysisRun_Id(targetAnalysisId);

        Map<String, com.devscope.model.MetricEntity> baseByClass = baseMetrics.stream()
                .collect(Collectors.toMap(com.devscope.model.MetricEntity::getClassName, item -> item, (first, second) -> first));
        Map<String, com.devscope.model.MetricEntity> targetByClass = targetMetrics.stream()
                .collect(Collectors.toMap(com.devscope.model.MetricEntity::getClassName, item -> item, (first, second) -> first));

        List<CompareResponse.MetricDifference> metricDifferences = targetByClass.entrySet().stream()
                .map(entry -> {
                    var target = entry.getValue();
                    var base = baseByClass.get(entry.getKey());
                    int baseLoc = base == null ? 0 : base.getLinesOfCode();
                    int baseDeps = base == null ? 0 : base.getDependencyCount();
                    String baseRisk = base == null ? "LOW" : base.getRiskLevel();
                    return new CompareResponse.MetricDifference(
                            entry.getKey(),
                            target.getLinesOfCode() - baseLoc,
                            target.getDependencyCount() - baseDeps,
                            baseRisk,
                            target.getRiskLevel()
                    );
                })
                .toList();

        int cycleBefore = (int) baseViolations.stream().filter(item -> "CYCLIC_DEPENDENCY".equalsIgnoreCase(item.getType())).count();
        int cycleAfter = (int) targetViolations.stream().filter(item -> "CYCLIC_DEPENDENCY".equalsIgnoreCase(item.getType())).count();

        double complexityBefore = baseMetrics.stream().mapToInt(com.devscope.model.MetricEntity::getMethodCount).average().orElse(0);
        double complexityAfter = targetMetrics.stream().mapToInt(com.devscope.model.MetricEntity::getMethodCount).average().orElse(0);

        CompareResponse.Summary summary = new CompareResponse.Summary();
        summary.setViolationChange(targetViolations.size() - baseViolations.size());
        summary.setAvgComplexityChange(round1(complexityAfter - complexityBefore));
        summary.setCycleChange(cycleAfter - cycleBefore);
        summary.setRiskChange(riskScore(targetMetrics) - riskScore(baseMetrics));
        summary.setComplexityBefore(round1(complexityBefore));
        summary.setComplexityAfter(round1(complexityAfter));

        CompareResponse response = new CompareResponse();
        response.setSummary(summary);
        response.setMetricDifferences(metricDifferences);
        response.setViolationDifferences(new CompareResponse.ViolationDifferences(
                Math.max(0, targetViolations.size() - baseViolations.size()),
                Math.max(0, baseViolations.size() - targetViolations.size())
        ));
        return response;
    }

    public List<String> getAvailableAnalyses(Long analysisId) {
        AnalysisRun run = analysisRunRepository.findById(analysisId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown analysisId: " + analysisId));
        String projectKey = run.getProject().getProjectKey();
        return analysisRunRepository.findByProject_ProjectKeyOrderByCreatedAtDesc(projectKey).stream()
                .map(AnalysisRun::getId)
                .map(String::valueOf)
                .toList();
    }

    private int riskScore(List<com.devscope.model.MetricEntity> metrics) {
        return metrics.stream().mapToInt(metric -> {
            if ("HIGH".equalsIgnoreCase(metric.getRiskLevel())) {
                return 3;
            }
            if ("MEDIUM".equalsIgnoreCase(metric.getRiskLevel())) {
                return 2;
            }
            return 1;
        }).sum();
    }

    private double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
