package com.devscope.service;

import com.devscope.dto.response.GraphNodeDetailResponse;
import com.devscope.dto.response.GraphResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class GraphService {

    private static final String CONTROLLER = "CONTROLLER";
    private static final String SERVICE = "SERVICE";
    private static final String REPOSITORY = "REPOSITORY";
    private static final String OTHER = "OTHER";

    private final Map<String, GraphResponse.GraphNode> nodeIndex;
    private final List<GraphResponse.GraphEdge> edges;

    public GraphService() {
        this.nodeIndex = new LinkedHashMap<>();
        this.edges = new ArrayList<>();
        seedGraph();
    }

    public GraphResponse getGraph(String projectId) {
        GraphResponse response = new GraphResponse();
        List<GraphResponse.GraphNode> nodes = new ArrayList<>(nodeIndex.values());
        for (GraphResponse.GraphNode node : nodes) {
            int dependencyCount = (int) edges.stream().filter(edge -> edge.getSource().equals(node.getId())).count();
            int dependentCount = (int) edges.stream().filter(edge -> edge.getTarget().equals(node.getId())).count();
            node.setDependencyCount(dependencyCount);
            node.setDependentCount(dependentCount);
            node.setHighCoupling(dependencyCount + dependentCount >= 4);
        }

        response.setNodes(nodes);
        response.setEdges(edges);
        return response;
    }

    public GraphNodeDetailResponse getNodeDetails(String projectId, String nodeId) {
        if (nodeId == null || nodeId.isBlank()) {
            return null;
        }

        GraphResponse.GraphNode node = findNode(nodeId);
        if (node == null) {
            return null;
        }

        List<String> outgoing = edges.stream()
                .filter(edge -> edge.getSource().equals(node.getId()))
                .map(GraphResponse.GraphEdge::getTarget)
                .map(targetId -> nodeIndex.get(targetId))
                .filter(item -> item != null)
                .map(GraphResponse.GraphNode::getName)
                .sorted()
                .toList();

        List<String> incoming = edges.stream()
                .filter(edge -> edge.getTarget().equals(node.getId()))
                .map(GraphResponse.GraphEdge::getSource)
                .map(sourceId -> nodeIndex.get(sourceId))
                .filter(item -> item != null)
                .map(GraphResponse.GraphNode::getName)
                .sorted()
                .toList();

        GraphNodeDetailResponse detail = new GraphNodeDetailResponse();
        detail.setId(node.getId());
        detail.setName(node.getName());
        detail.setFullName(node.getId());
        detail.setType(node.getType());
        detail.setDependencyCount(outgoing.size());
        detail.setDependentCount(incoming.size());
        detail.setOutgoingDependencies(outgoing);
        detail.setIncomingDependents(incoming);

        return detail;
    }

    private GraphResponse.GraphNode findNode(String nodeId) {
        GraphResponse.GraphNode exact = nodeIndex.get(nodeId);
        if (exact != null) {
            return exact;
        }

        String normalizedNodeId = normalize(nodeId);
        return nodeIndex.values().stream()
                .filter(item -> normalize(item.getName()).equals(normalizedNodeId))
                .findFirst()
                .orElse(null);
    }

    private void seedGraph() {
        addNode("com.devscope.controller.DashboardController", CONTROLLER);
        addNode("com.devscope.controller.StructureController", CONTROLLER);
        addNode("com.devscope.controller.AnalysisController", CONTROLLER);
        addNode("com.devscope.service.DashboardService", SERVICE);
        addNode("com.devscope.service.StructureService", SERVICE);
        addNode("com.devscope.service.InsightAggregationService", SERVICE);
        addNode("com.devscope.repository.InsightRepository", REPOSITORY);
        addNode("com.devscope.repository.RuleViolationRepository", REPOSITORY);
        addNode("com.devscope.engine.graph.DependencyResolver", OTHER);
        addNode("com.devscope.engine.rulesengine.RuleOrchestrator", OTHER);
        addNode("com.devscope.engine.scanner.RepositoryScanner", OTHER);
        addNode("com.devscope.engine.metrics.ClassMetricsCalculator", OTHER);

        addEdge("com.devscope.controller.DashboardController", "com.devscope.service.DashboardService", false);
        addEdge("com.devscope.controller.StructureController", "com.devscope.service.StructureService", false);
        addEdge("com.devscope.controller.AnalysisController", "com.devscope.service.InsightAggregationService", false);

        addEdge("com.devscope.service.DashboardService", "com.devscope.repository.InsightRepository", false);
        addEdge("com.devscope.service.StructureService", "com.devscope.engine.graph.DependencyResolver", false);
        addEdge("com.devscope.service.StructureService", "com.devscope.engine.scanner.RepositoryScanner", false);
        addEdge("com.devscope.service.InsightAggregationService", "com.devscope.repository.RuleViolationRepository", false);
        addEdge("com.devscope.service.InsightAggregationService", "com.devscope.engine.rulesengine.RuleOrchestrator", false);

        addEdge("com.devscope.engine.rulesengine.RuleOrchestrator", "com.devscope.engine.graph.DependencyResolver", true);
        addEdge("com.devscope.engine.graph.DependencyResolver", "com.devscope.engine.metrics.ClassMetricsCalculator", true);
        addEdge("com.devscope.engine.metrics.ClassMetricsCalculator", "com.devscope.engine.rulesengine.RuleOrchestrator", true);
    }

    private void addNode(String fullName, String layer) {
        int lastDot = fullName.lastIndexOf('.');
        String name = lastDot > -1 ? fullName.substring(lastDot + 1) : fullName;
        nodeIndex.put(fullName, new GraphResponse.GraphNode(fullName, name, layer));
    }

    private void addEdge(String source, String target, boolean cycle) {
        edges.add(new GraphResponse.GraphEdge(source, target, cycle));
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}