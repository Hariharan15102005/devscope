package com.devscope.service;

import com.devscope.dto.response.DashboardResponse;
import com.devscope.exception.AnalysisException;
import com.devscope.model.DependencyEntity;
import com.devscope.model.MetricEntity;
import com.devscope.model.ViolationEntity;
import com.devscope.repository.DependencyRepository;
import com.devscope.repository.JavaClassRepository;
import com.devscope.repository.MetricRepository;
import com.devscope.repository.ViolationRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final JavaClassRepository javaClassRepository;
    private final DependencyRepository dependencyRepository;
    private final MetricRepository metricRepository;
    private final ViolationRepository violationRepository;

    public DashboardService(
            JavaClassRepository javaClassRepository,
            DependencyRepository dependencyRepository,
            MetricRepository metricRepository,
            ViolationRepository violationRepository
    ) {
        this.javaClassRepository = javaClassRepository;
        this.dependencyRepository = dependencyRepository;
        this.metricRepository = metricRepository;
        this.violationRepository = violationRepository;
    }

    public DashboardResponse getDashboardSummary(Long analysisId) {
        if (analysisId == null) {
            throw new AnalysisException("analysisId is required");
        }

        var classes = javaClassRepository.findByAnalysisRun_Id(analysisId);
        var dependencies = dependencyRepository.findByAnalysisRun_Id(analysisId);
        var metrics = metricRepository.findByAnalysisRun_Id(analysisId);
        var violations = violationRepository.findByAnalysisRun_Id(analysisId);

        int cycleCount = (int) dependencies.stream().filter(DependencyEntity::isCycle).count();
        int healthScore = calculateHealthScore(violations);

        DashboardResponse response = new DashboardResponse();
        response.setAnalysisId(analysisId);

        DashboardResponse.Summary summary = new DashboardResponse.Summary();
        summary.setTotalClasses(classes.size());
        summary.setTotalDependencies(dependencies.size());
        summary.setTotalViolations(violations.size());
        summary.setCyclicDependencyCount(cycleCount);
        summary.setHealthScore(healthScore);
        response.setSummary(summary);

        response.setRiskOverview(metrics.stream()
                .filter(metric -> !"LOW".equalsIgnoreCase(metric.getRiskLevel()))
                .limit(10)
                .map(metric -> new DashboardResponse.RiskOverviewItem(
                        metric.getClassName(),
                        metric.getRiskLevel(),
                        metric.getDependencyCount() > 15 ? "Too many dependencies" : "High line count"
                ))
                .toList());

        response.setTotalPackages((int) classes.stream().map(item -> item.getPackageName()).distinct().count());
        response.setTotalClasses(classes.size());
        response.setTotalFiles(classes.size());
        response.setTotalDependencies(dependencies.size());
        response.setHealthScore(healthScore);
        response.setCyclicDependenciesCount(cycleCount);

        response.setLayerDistribution(buildLayerDistribution(classes));
        response.setComplexityDistribution(buildComplexityDistribution(metrics));
        response.setTopRiskyClasses(metrics.stream().limit(5)
                .map(item -> new DashboardResponse.RiskyClassItem(item.getClassName(), toRiskScore(item.getRiskLevel())))
                .toList());
        response.setTopViolations(violations.stream().limit(5)
                .map(item -> new DashboardResponse.ViolationItem(item.getCode(), item.getTitle(), toSeverityScore(item.getSeverity())))
                .toList());

        return response;
    }

    private int calculateHealthScore(List<ViolationEntity> violations) {
        int score = 100;
        for (ViolationEntity violation : violations) {
            if ("HIGH".equalsIgnoreCase(violation.getSeverity())) {
                score -= 10;
            } else if ("MEDIUM".equalsIgnoreCase(violation.getSeverity())) {
                score -= 5;
            }
        }
        return Math.max(0, score);
    }

    private Map<String, Integer> buildLayerDistribution(List<com.devscope.model.JavaClassEntity> classes) {
        Map<String, Integer> layerDistribution = new LinkedHashMap<>();
        layerDistribution.put("Controller", (int) classes.stream().filter(item -> item.getPackageName().contains("controller")).count());
        layerDistribution.put("Service", (int) classes.stream().filter(item -> item.getPackageName().contains("service")).count());
        layerDistribution.put("Repository", (int) classes.stream().filter(item -> item.getPackageName().contains("repository")).count());
        layerDistribution.put("Others", classes.size() - layerDistribution.values().stream().mapToInt(Integer::intValue).sum());
        return layerDistribution;
    }

    private Map<String, Integer> buildComplexityDistribution(List<MetricEntity> metrics) {
        Map<String, Integer> complexityDistribution = new LinkedHashMap<>();
        complexityDistribution.put("1-5", (int) metrics.stream().filter(item -> item.getMethodCount() <= 5).count());
        complexityDistribution.put("6-10", (int) metrics.stream().filter(item -> item.getMethodCount() >= 6 && item.getMethodCount() <= 10).count());
        complexityDistribution.put("11-15", (int) metrics.stream().filter(item -> item.getMethodCount() >= 11 && item.getMethodCount() <= 15).count());
        complexityDistribution.put("16+", (int) metrics.stream().filter(item -> item.getMethodCount() >= 16).count());
        return complexityDistribution;
    }

    private int toRiskScore(String riskLevel) {
        if ("HIGH".equalsIgnoreCase(riskLevel)) {
            return 90;
        }
        if ("MEDIUM".equalsIgnoreCase(riskLevel)) {
            return 65;
        }
        return 40;
    }

    private int toSeverityScore(String severity) {
        if ("HIGH".equalsIgnoreCase(severity)) {
            return 5;
        }
        if ("MEDIUM".equalsIgnoreCase(severity)) {
            return 3;
        }
        return 1;
    }
}
