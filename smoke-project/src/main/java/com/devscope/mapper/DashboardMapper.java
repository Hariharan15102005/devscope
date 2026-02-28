package com.devscope.mapper;

import com.devscope.dto.response.DashboardResponse;
import com.devscope.model.DependencyEntity;
import com.devscope.model.JavaClassEntity;
import com.devscope.model.MetricEntity;
import com.devscope.model.ViolationEntity;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DashboardMapper {

    public DashboardResponse toResponse(
            List<JavaClassEntity> classes,
            List<DependencyEntity> dependencies,
            List<MetricEntity> metrics,
            List<ViolationEntity> violations
    ) {
        DashboardResponse response = new DashboardResponse();
        response.setTotalClasses(classes.size());
        response.setTotalDependencies(dependencies.size());
        response.setTotalFiles(classes.size());
        response.setTotalPackages((int) classes.stream().map(JavaClassEntity::getPackageName).distinct().count());

        int cycleCount = (int) dependencies.stream().filter(DependencyEntity::isCycle).count();
        response.setCyclicDependenciesCount(cycleCount);
        response.setHealthScore(Math.max(0, 100 - cycleCount * 5.0));

        Map<String, Integer> layerDistribution = new LinkedHashMap<>();
        layerDistribution.put("Controller", (int) classes.stream().filter(item -> item.getPackageName().contains("controller")).count());
        layerDistribution.put("Service", (int) classes.stream().filter(item -> item.getPackageName().contains("service")).count());
        layerDistribution.put("Repository", (int) classes.stream().filter(item -> item.getPackageName().contains("repository")).count());
        layerDistribution.put("Others", classes.size() - layerDistribution.values().stream().mapToInt(Integer::intValue).sum());
        response.setLayerDistribution(layerDistribution);

        Map<String, Integer> complexityDistribution = new LinkedHashMap<>();
        complexityDistribution.put("1-5", (int) metrics.stream().filter(item -> item.getMethodCount() <= 5).count());
        complexityDistribution.put("6-10", (int) metrics.stream().filter(item -> item.getMethodCount() >= 6 && item.getMethodCount() <= 10).count());
        complexityDistribution.put("11-15", (int) metrics.stream().filter(item -> item.getMethodCount() >= 11 && item.getMethodCount() <= 15).count());
        complexityDistribution.put("16+", (int) metrics.stream().filter(item -> item.getMethodCount() >= 16).count());
        response.setComplexityDistribution(complexityDistribution);

        response.setTopRiskyClasses(metrics.stream()
                .limit(5)
                .map(item -> new DashboardResponse.RiskyClassItem(item.getClassName(), toRiskScore(item.getRiskLevel())))
                .toList());

        response.setTopViolations(violations.stream()
                .limit(5)
                .map(item -> new DashboardResponse.ViolationItem(item.getCode(), item.getTitle(), toSeverityScore(item.getSeverity())))
                .toList());

        return response;
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
