package com.devscope.service;

import com.devscope.dto.response.DashboardResponse;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    public DashboardResponse getDashboardSummary(String projectId) {
        int seed = Math.abs(projectId == null ? 1 : projectId.hashCode());

        int totalPackages = 12 + (seed % 24);
        int totalClasses = 80 + (seed % 180);
        int totalFiles = 120 + (seed % 300);
        int totalDependencies = 30 + (seed % 160);
        int cyclicDependenciesCount = seed % 9;

        int controller = Math.max(1, totalClasses / 8);
        int service = Math.max(1, totalClasses / 4);
        int repository = Math.max(1, totalClasses / 7);
        int others = Math.max(1, totalClasses - controller - service - repository);

        double healthScore = Math.max(25, 100 - (cyclicDependenciesCount * 4.0) - (totalDependencies * 0.08));

        Map<String, Integer> layerDistribution = new LinkedHashMap<>();
        layerDistribution.put("Controller", controller);
        layerDistribution.put("Service", service);
        layerDistribution.put("Repository", repository);
        layerDistribution.put("Others", others);

        Map<String, Integer> complexityDistribution = new LinkedHashMap<>();
        complexityDistribution.put("1-5", 18 + (seed % 8));
        complexityDistribution.put("6-10", 14 + (seed % 7));
        complexityDistribution.put("11-15", 8 + (seed % 5));
        complexityDistribution.put("16+", 3 + (seed % 4));

        List<DashboardResponse.RiskyClassItem> topRiskyClasses = List.of(
                new DashboardResponse.RiskyClassItem("com.devscope.engine.graph.DependencyResolver", 92),
                new DashboardResponse.RiskyClassItem("com.devscope.engine.rulesengine.RuleOrchestrator", 88),
                new DashboardResponse.RiskyClassItem("com.devscope.service.InsightAggregationService", 83),
                new DashboardResponse.RiskyClassItem("com.devscope.engine.scanner.RepositoryScanner", 79),
                new DashboardResponse.RiskyClassItem("com.devscope.controller.AnalysisController", 74)
        );

        List<DashboardResponse.ViolationItem> topViolations = List.of(
                new DashboardResponse.ViolationItem("V-1021", "High cyclomatic complexity in DependencyResolver", 5),
                new DashboardResponse.ViolationItem("V-1042", "Field injection detected in Service layer", 4),
                new DashboardResponse.ViolationItem("V-1068", "Potential N+1 query pattern in repository", 4),
                new DashboardResponse.ViolationItem("V-1094", "Controller method exceeds max length", 3),
                new DashboardResponse.ViolationItem("V-1103", "Low test coverage for parser package", 3)
        );

        DashboardResponse response = new DashboardResponse();
        response.setTotalPackages(totalPackages);
        response.setTotalClasses(totalClasses);
        response.setTotalFiles(totalFiles);
        response.setTotalDependencies(totalDependencies);
        response.setHealthScore(Math.round(healthScore * 10.0) / 10.0);
        response.setLayerDistribution(layerDistribution);
        response.setComplexityDistribution(complexityDistribution);
        response.setTopRiskyClasses(topRiskyClasses);
        response.setTopViolations(topViolations);
        response.setCyclicDependenciesCount(cyclicDependenciesCount);

        return response;
    }
}
