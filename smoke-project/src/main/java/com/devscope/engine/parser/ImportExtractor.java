package com.devscope.engine.parser;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class ImportExtractor {

    public List<String> extractImports(Path javaFile) throws IOException {
        return Files.lines(javaFile)
                .map(String::trim)
                .filter(line -> line.startsWith("import "))
                .map(line -> line.replace("import", "").replace(";", "").trim())
                .toList();
    }
}
