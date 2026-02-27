package com.devscope.service;

import com.devscope.dto.response.InsightResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsightsService {

    public List<InsightResponse> getInsights(String projectId) {
        return List.of(
                new InsightResponse(
                        "I-301",
                        "Break cyclic dependency across rules and metrics",
                        "Rules orchestration and metrics calculation currently form a cycle through shared graph resolution logic.",
                        "HIGH",
                        "CYCLE",
                        List.of(
                                "com.devscope.engine.rulesengine.RuleOrchestrator",
                                "com.devscope.engine.graph.DependencyResolver",
                                "com.devscope.engine.metrics.ClassMetricsCalculator"
                        ),
                        "Mutual dependencies between these components increase regression risk and make isolated testing difficult.",
                        List.of(
                                "Extract a read-only dependency query interface from DependencyResolver.",
                                "Inject the interface into RuleOrchestrator and ClassMetricsCalculator instead of concrete class references.",
                                "Move orchestration coordination to an outer service boundary."
                        )
                ),
                new InsightResponse(
                        "I-302",
                        "Reduce coupling in InsightAggregationService",
                        "InsightAggregationService orchestrates repositories, scanners, and rules in the same execution path.",
                        "MEDIUM",
                        "COUPLING",
                        List.of("com.devscope.service.InsightAggregationService"),
                        "Too many direct collaborators make change impact hard to predict and testing expensive.",
                        List.of(
                                "Split persistence operations into a dedicated gateway service.",
                                "Move scoring logic to an isolated calculator component.",
                                "Keep aggregation service focused on composition and response assembly."
                        )
                ),
                new InsightResponse(
                        "I-303",
                        "Harden layering boundaries for structure analysis",
                        "StructureService consumes scanner internals directly, leaking implementation concerns across layers.",
                        "MEDIUM",
                        "LAYERING",
                        List.of(
                                "com.devscope.service.StructureService",
                                "com.devscope.engine.scanner.RepositoryScanner"
                        ),
                        "Direct use of internals weakens boundaries and increases ripple effects for scanner changes.",
                        List.of(
                                "Define a structure-scan interface in service layer package.",
                                "Adapt RepositoryScanner to implement only that interface.",
                                "Inject interface into StructureService through constructor."
                        )
                ),
                new InsightResponse(
                        "I-304",
                        "Decompose DependencyResolver into focused units",
                        "DependencyResolver handles parsing, edge construction, cycle detection, and scoring in one class.",
                        "HIGH",
                        "COMPLEXITY",
                        List.of("com.devscope.engine.graph.DependencyResolver"),
                        "Large multi-purpose classes are harder to maintain and usually produce unstable hotspots.",
                        List.of(
                                "Extract cycle analysis into a standalone CycleDetector.",
                                "Move scoring into a dedicated DependencyScoreCalculator.",
                                "Retain DependencyResolver only as orchestration shell."
                        )
                ),
                new InsightResponse(
                        "I-305",
                        "Stabilize DashboardService dependency direction",
                        "DashboardService depends on repository structures and transformation details in the same module.",
                        "LOW",
                        "LAYERING",
                        List.of(
                                "com.devscope.service.DashboardService",
                                "com.devscope.repository.InsightRepository"
                        ),
                        "Mixing fetch and transform concerns in one class complicates extension and test substitution.",
                        List.of(
                                "Introduce a dashboard projection mapper.",
                                "Move raw repository mapping to mapper component.",
                                "Keep DashboardService as use-case coordinator."
                        )
                )
        );
    }
}
