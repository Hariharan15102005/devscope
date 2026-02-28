package com.devscope.engine.graph;

import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GraphAnalyzer {

    public List<List<String>> detectCycles(DependencyGraph graph) {
        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();
        ArrayDeque<String> path = new ArrayDeque<>();
        List<List<String>> cycles = new ArrayList<>();

        for (String node : graph.getAdjacency().keySet()) {
            dfs(node, graph, visited, stack, path, cycles);
        }
        return cycles;
    }

    public Set<String> findHighCouplingNodes(DependencyGraph graph, int threshold) {
        Set<String> results = new HashSet<>();
        for (String node : graph.getAdjacency().keySet()) {
            int outgoing = graph.dependenciesOf(node).size();
            int incoming = (int) graph.getAdjacency().values().stream().filter(targets -> targets.contains(node)).count();
            if (outgoing + incoming >= threshold) {
                results.add(node);
            }
        }
        return results;
    }

    private void dfs(
            String node,
            DependencyGraph graph,
            Set<String> visited,
            Set<String> stack,
            ArrayDeque<String> path,
            List<List<String>> cycles
    ) {
        if (stack.contains(node)) {
            List<String> cycle = new ArrayList<>();
            boolean inCycle = false;
            for (String pathNode : path) {
                if (pathNode.equals(node)) {
                    inCycle = true;
                }
                if (inCycle) {
                    cycle.add(pathNode);
                }
            }
            cycle.add(node);
            cycles.add(cycle);
            return;
        }

        if (visited.contains(node)) {
            return;
        }

        visited.add(node);
        stack.add(node);
        path.addLast(node);

        for (String next : graph.dependenciesOf(node)) {
            if (graph.getAdjacency().containsKey(next)) {
                dfs(next, graph, visited, stack, path, cycles);
            }
        }

        path.removeLast();
        stack.remove(node);
    }
}
