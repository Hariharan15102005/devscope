package com.devscope.engine.graph;

import com.devscope.engine.parser.ParsedJavaClass;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DependencyGraphBuilder {

    public DependencyGraph buildGraph(java.util.List<ParsedJavaClass> classes) {
        DependencyGraph graph = new DependencyGraph();
        Map<String, String> classesBySimpleName = classes.stream()
                .collect(Collectors.toMap(ParsedJavaClass::getClassName, ParsedJavaClass::getFullName, (first, second) -> first));

        for (ParsedJavaClass parsedClass : classes) {
            graph.addNode(parsedClass.getFullName());
            for (String importedType : parsedClass.getImports()) {
                String candidateSimpleName = importedType.substring(importedType.lastIndexOf('.') + 1);
                String resolvedTarget = classesBySimpleName.getOrDefault(candidateSimpleName, importedType);
                if (!resolvedTarget.equals(parsedClass.getFullName())) {
                    graph.addEdge(parsedClass.getFullName(), resolvedTarget);
                }
            }
        }

        return graph;
    }

    public Set<String> classNames(DependencyGraph graph) {
        return graph.getAdjacency().keySet();
    }
}
