package com.devscope.mapper;

import com.devscope.dto.response.GraphResponse;
import com.devscope.model.DependencyEntity;
import com.devscope.model.JavaClassEntity;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GraphMapper {

    public GraphResponse toGraphResponse(List<JavaClassEntity> classes, List<DependencyEntity> dependencies) {
        Map<String, Integer> outgoing = new HashMap<>();
        Map<String, Integer> incoming = new HashMap<>();

        for (DependencyEntity dependency : dependencies) {
            outgoing.merge(dependency.getSourceClass(), 1, Integer::sum);
            incoming.merge(dependency.getTargetClass(), 1, Integer::sum);
        }

        GraphResponse response = new GraphResponse();
        response.setNodes(classes.stream().map(item -> {
            GraphResponse.GraphNode node = new GraphResponse.GraphNode();
            node.setId(item.getFullName());
            node.setName(item.getClassName());
            node.setType(resolveType(item.getPackageName()));
            node.setDependencyCount(outgoing.getOrDefault(item.getFullName(), 0));
            node.setDependentCount(incoming.getOrDefault(item.getFullName(), 0));
            node.setHighCoupling(node.getDependencyCount() + node.getDependentCount() >= 8);
            return node;
        }).toList());

        response.setEdges(dependencies.stream().map(item -> {
            GraphResponse.GraphEdge edge = new GraphResponse.GraphEdge();
            edge.setSource(item.getSourceClass());
            edge.setTarget(item.getTargetClass());
            edge.setCycle(item.isCycle());
            return edge;
        }).toList());

        return response;
    }

    private String resolveType(String packageName) {
        if (packageName.contains("controller")) return "CONTROLLER";
        if (packageName.contains("service")) return "SERVICE";
        if (packageName.contains("repository")) return "REPOSITORY";
        return "OTHER";
    }
}
