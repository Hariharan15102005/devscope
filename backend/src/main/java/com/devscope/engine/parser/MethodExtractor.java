package com.devscope.engine.parser;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class MethodExtractor {

    public List<String> extractMethods(Path javaFile) throws IOException {
        return Files.lines(javaFile)
                .map(String::trim)
                .filter(line -> line.contains("(") && line.contains(")") && line.endsWith("{"))
                .filter(line -> !line.startsWith("if") && !line.startsWith("for") && !line.startsWith("while"))
                .toList();
    }
}
