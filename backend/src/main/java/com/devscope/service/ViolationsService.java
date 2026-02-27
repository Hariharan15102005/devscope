package com.devscope.service;

import com.devscope.dto.response.ViolationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ViolationsService {

    public List<ViolationResponse> getViolations(String projectId) {
        return List.of(
                new ViolationResponse(
                        "V-1021",
                        "GOD_CLASS",
                        "HIGH",
                        "DependencyResolver has excessive responsibilities",
                        "DependencyResolver combines graph construction, cycle detection, and scoring logic in one class, increasing maintenance risk.",
                        List.of("com.devscope.engine.graph.DependencyResolver"),
                        "Split graph traversal, cycle detection, and scoring into dedicated collaborators and reduce method sizes."
                ),
                new ViolationResponse(
                        "V-1108",
                        "CYCLIC_DEPENDENCY",
                        "HIGH",
                        "Cycle detected between rules and metrics components",
                        "RuleOrchestrator and ClassMetricsCalculator depend on each other indirectly through DependencyResolver, causing unstable architecture boundaries.",
                        List.of(
                                "com.devscope.engine.rulesengine.RuleOrchestrator",
                                "com.devscope.engine.graph.DependencyResolver",
                                "com.devscope.engine.metrics.ClassMetricsCalculator"
                        ),
                        "Break the cycle by introducing interfaces or an event-driven coordination boundary between metrics and rules modules."
                ),
                new ViolationResponse(
                        "V-1042",
                        "HIGH_COUPLING",
                        "MEDIUM",
                        "InsightAggregationService has high outgoing coupling",
                        "InsightAggregationService touches repository, rules orchestration, and scanner concerns which creates broad impact for small changes.",
                        List.of("com.devscope.service.InsightAggregationService"),
                        "Extract repository access and rules orchestration into separate services and keep aggregation focused on composition."
                ),
                new ViolationResponse(
                        "V-1068",
                        "LAYER_VIOLATION",
                        "MEDIUM",
                        "Service layer references scanner internals",
                        "StructureService references RepositoryScanner implementation details instead of an abstraction, reducing layer isolation.",
                        List.of(
                                "com.devscope.service.StructureService",
                                "com.devscope.engine.scanner.RepositoryScanner"
                        ),
                        "Introduce a scanner interface in the service boundary and inject only the abstraction into StructureService."
                ),
                new ViolationResponse(
                        "V-1117",
                        "HIGH_COUPLING",
                        "LOW",
                        "DashboardService depends on concrete repository class",
                        "DashboardService directly consumes InsightRepository concrete behavior, making tests and substitutions harder.",
                        List.of(
                                "com.devscope.service.DashboardService",
                                "com.devscope.repository.InsightRepository"
                        ),
                        "Depend on repository interface contracts and isolate transformation logic from data fetching concerns."
                )
        );
    }
}
