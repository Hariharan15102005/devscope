package com.devscope.dto.response;

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
        private String fullName;
        private String layer;

        public GraphNode() {
        }

        public GraphNode(String id, String name, String fullName, String layer) {
            this.id = id;
            this.name = name;
            this.fullName = fullName;
            this.layer = layer;
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

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getLayer() {
            return layer;
        }

        public void setLayer(String layer) {
            this.layer = layer;
        }
    }

    public static class GraphEdge {
        private String source;
        private String target;
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