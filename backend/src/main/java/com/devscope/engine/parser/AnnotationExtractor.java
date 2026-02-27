package com.devscope.engine.parser;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class AnnotationExtractor {

    public List<String> extractAnnotations(Path javaFile) throws IOException {
        return Files.lines(javaFile)
                .map(String::trim)
                .filter(line -> line.startsWith("@"))
                .map(line -> line.substring(1).split("\\(")[0])
                .toList();
    }
}
