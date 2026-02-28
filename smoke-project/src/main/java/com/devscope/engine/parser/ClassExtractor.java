package com.devscope.engine.parser;

import com.devscope.util.PathUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Component
public class ClassExtractor {

    public Optional<String> extractClassName(Path javaFile) throws IOException {
        return Files.lines(javaFile)
                .map(String::trim)
                .filter(line -> line.startsWith("class ")
                        || line.contains(" class ")
                        || line.startsWith("public class "))
                .map(this::resolveClassToken)
                .filter(token -> !token.isBlank())
                .findFirst();
    }

    public String buildFullName(Path sourceRoot, Path javaFile, String className) {
        String packageName = PathUtils.normalizePackage(sourceRoot, javaFile);
        if (packageName == null || packageName.isBlank()) {
            return className;
        }
        return packageName + "." + className;
    }

    private String resolveClassToken(String declarationLine) {
        String normalized = declarationLine.replace("{", " ").replace("implements", " ").replace("extends", " ");
        String[] tokens = normalized.trim().split("\\s+");
        for (int index = 0; index < tokens.length - 1; index++) {
            if ("class".equals(tokens[index])) {
                return tokens[index + 1];
            }
        }
        return "";
    }
}
