package com.devscope.service;

import com.devscope.dto.response.ClassDetailResponse;
import com.devscope.dto.response.StructureTreeResponse;
import org.springframework.stereotype.Service;
// This service provides the structure tree and class details for the application. In a real implementation,
// it would likely integrate with the graph service to derive class relationships and metrics, but here we use a static dataset for demonstration purposes.
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class StructureService {

    private final Map<String, ClassDetailResponse> classIndex;

    public StructureService() {
        this.classIndex = buildClassIndex();
    }

    public StructureTreeResponse getStructureTree(String projectId, String filterType) {
        String normalizedFilter = normalize(filterType);
        Map<String, List<StructureTreeResponse.ClassNode>> grouped = new LinkedHashMap<>();

        for (ClassDetailResponse detail : classIndex.values()) {
            if (normalizedFilter != null && !normalize(detail.getAnnotation()).equals(normalizedFilter)) {
                continue;
            }

            grouped.computeIfAbsent(detail.getPackageName(), key -> new ArrayList<>())
                    .add(new StructureTreeResponse.ClassNode(detail.getFullName(), detail.getClassName(), detail.getAnnotation()));
        }

        List<StructureTreeResponse.PackageNode> packages = grouped.entrySet().stream()
                .map(entry -> new StructureTreeResponse.PackageNode(entry.getKey(), entry.getValue()))
                .toList();

        StructureTreeResponse response = new StructureTreeResponse();
        response.setPackages(packages);
        return response;
    }

    public ClassDetailResponse getClassDetail(String projectId, String className) {
        if (className == null || className.isBlank()) {
            return null;
        }

        ClassDetailResponse exact = classIndex.get(className);
        if (exact != null) {
            return exact;
        }

        return classIndex.values().stream()
                .filter(item -> item.getClassName().equals(className))
                .findFirst()
                .orElse(null);
    }

    private static String normalize(String value) {
        return value == null ? null : value.trim().toLowerCase(Locale.ROOT);
    }

    private static ClassDetailResponse createDetail(
            String fullName,
            String annotation,
            int riskScore,
            int methodCount,
            int dependencyCount,
            String description
    ) {
        ClassDetailResponse response = new ClassDetailResponse();
        response.setFullName(fullName);

        int lastDot = fullName.lastIndexOf('.');
        String packageName = lastDot > 0 ? fullName.substring(0, lastDot) : "default";
        String className = lastDot > 0 ? fullName.substring(lastDot + 1) : fullName;

        response.setPackageName(packageName);
        response.setClassName(className);
        response.setAnnotation(annotation);
        response.setRiskScore(riskScore);
        response.setMethodCount(methodCount);
        response.setDependencyCount(dependencyCount);
        response.setDescription(description);

        return response;
    }

    private static Map<String, ClassDetailResponse> buildClassIndex() {
        Map<String, ClassDetailResponse> index = new LinkedHashMap<>();

        List<ClassDetailResponse> classes = List.of(
                createDetail("com.devscope.controller.DashboardController", "Controller", 41, 7, 4, "Exposes dashboard summary APIs."),
                createDetail("com.devscope.controller.AnalysisController", "Controller", 74, 10, 8, "Handles analysis workflow and orchestration endpoints."),
                createDetail("com.devscope.service.DashboardService", "Service", 52, 9, 6, "Builds dashboard aggregates and summaries."),
                createDetail("com.devscope.service.InsightAggregationService", "Service", 83, 14, 12, "Aggregates insights and computes composite risk metrics."),
                createDetail("com.devscope.repository.InsightRepository", "Repository", 60, 6, 3, "Persists and retrieves insight entities."),
                createDetail("com.devscope.repository.RuleViolationRepository", "Repository", 67, 8, 4, "Queries violation records and severity trends."),
                createDetail("com.devscope.engine.graph.DependencyResolver", "Component", 92, 19, 17, "Resolves class dependency graph and cycle paths."),
                createDetail("com.devscope.engine.rulesengine.RuleOrchestrator", "Component", 88, 16, 14, "Runs static analysis rules in execution pipeline."),
                createDetail("com.devscope.engine.scanner.RepositoryScanner", "Component", 79, 13, 9, "Scans repository source and emits structure artifacts."),
                createDetail("com.devscope.engine.metrics.ClassMetricsCalculator", "Component", 69, 12, 11, "Calculates complexity, coupling, and maintainability metrics.")
        );

        for (ClassDetailResponse detail : classes) {
            index.put(detail.getFullName(), detail);
        }

        return index;
    }
}
