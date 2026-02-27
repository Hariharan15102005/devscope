package com.devscope.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GraphResponse {
    private List<GraphNode> nodes;
    private List<GraphEdge> edges;

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    public List<GraphEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<GraphEdge> edges) {
        this.edges = edges;
    }

    public static class GraphNode {
        private String id;
        private String name;
        private String type;
        private int dependencyCount;
        private int dependentCount;
        @JsonProperty("isHighCoupling")
        private boolean highCoupling;

        public GraphNode() {
        }

        public GraphNode(String id, String name, String type) {
            this.id = id;
            this.name = name;
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getDependencyCount() {
            return dependencyCount;
        }

        public void setDependencyCount(int dependencyCount) {
            this.dependencyCount = dependencyCount;
        }

        public int getDependentCount() {
            return dependentCount;
        }

        public void setDependentCount(int dependentCount) {
            this.dependentCount = dependentCount;
        }

        public boolean isHighCoupling() {
            return highCoupling;
        }

        public void setHighCoupling(boolean highCoupling) {
            this.highCoupling = highCoupling;
        }
    }

    public static class GraphEdge {
        private String source;
        private String target;
        @JsonProperty("isCycle")
        private boolean cycle;

        public GraphEdge() {
        }

        public GraphEdge(String source, String target, boolean cycle) {
            this.source = source;
            this.target = target;
            this.cycle = cycle;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public boolean isCycle() {
            return cycle;
        }

        public void setCycle(boolean cycle) {
            this.cycle = cycle;
        }
    }
}