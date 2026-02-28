package com.devscope.service;

import com.devscope.dto.response.GraphNodeDetailResponse;
import com.devscope.dto.response.GraphResponse;
import com.devscope.mapper.GraphMapper;
import com.devscope.repository.DependencyRepository;
import com.devscope.repository.JavaClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GraphService {

    private final JavaClassRepository javaClassRepository;
    private final DependencyRepository dependencyRepository;
    private final GraphMapper graphMapper;

    public GraphService(
            JavaClassRepository javaClassRepository,
            DependencyRepository dependencyRepository,
            GraphMapper graphMapper
    ) {
        this.javaClassRepository = javaClassRepository;
        this.dependencyRepository = dependencyRepository;
        this.graphMapper = graphMapper;
    }

    public GraphResponse getGraph(Long analysisId) {
        var classes = javaClassRepository.findByAnalysisRun_Id(analysisId);
        var dependencies = dependencyRepository.findByAnalysisRun_Id(analysisId);
        return graphMapper.toGraphResponse(classes, dependencies);
    }

    public GraphNodeDetailResponse getNodeDetails(Long analysisId, String nodeId) {
        if (nodeId == null || nodeId.isBlank()) {
            return null;
        }

        GraphResponse graph = getGraph(analysisId);
        Map<String, GraphResponse.GraphNode> nodeById = graph.getNodes().stream()
                .collect(Collectors.toMap(GraphResponse.GraphNode::getId, node -> node));

        GraphResponse.GraphNode selected = nodeById.get(nodeId);
        if (selected == null) {
            String normalized = normalize(nodeId);
            selected = graph.getNodes().stream()
                    .filter(node -> normalize(node.getName()).equals(normalized))
                    .findFirst()
                    .orElse(null);
        }

        if (selected == null) {
            return null;
        }

        GraphResponse.GraphNode selectedNode = selected;

        List<String> outgoingDependencies = graph.getEdges().stream()
            .filter(edge -> edge.getSource().equals(selectedNode.getId()))
                .map(GraphResponse.GraphEdge::getTarget)
                .map(nodeById::get)
                .filter(node -> node != null)
                .map(GraphResponse.GraphNode::getName)
                .sorted()
                .toList();

        List<String> incomingDependents = graph.getEdges().stream()
            .filter(edge -> edge.getTarget().equals(selectedNode.getId()))
                .map(GraphResponse.GraphEdge::getSource)
                .map(nodeById::get)
                .filter(node -> node != null)
                .map(GraphResponse.GraphNode::getName)
                .sorted()
                .toList();

        GraphNodeDetailResponse detail = new GraphNodeDetailResponse();
        detail.setId(selectedNode.getId());
        detail.setName(selectedNode.getName());
        detail.setFullName(selectedNode.getId());
        detail.setType(selectedNode.getType());
        detail.setDependencyCount(selectedNode.getDependencyCount());
        detail.setDependentCount(selectedNode.getDependentCount());
        detail.setOutgoingDependencies(outgoingDependencies);
        detail.setIncomingDependents(incomingDependents);
        return detail;
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
