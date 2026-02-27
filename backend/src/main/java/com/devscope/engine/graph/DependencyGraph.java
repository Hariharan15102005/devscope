package com.devscope.engine.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DependencyGraph {
    private final Map<String, Set<String>> adjacency = new HashMap<>();

    public Map<String, Set<String>> getAdjacency() {
        return adjacency;
    }

    public void addNode(String className) {
        adjacency.computeIfAbsent(className, ignored -> new HashSet<>());
    }

    public void addEdge(String source, String target) {
        adjacency.computeIfAbsent(source, ignored -> new HashSet<>()).add(target);
        adjacency.computeIfAbsent(target, ignored -> new HashSet<>());
    }

    public Set<String> dependenciesOf(String className) {
        return adjacency.getOrDefault(className, Set.of());
    }
}
