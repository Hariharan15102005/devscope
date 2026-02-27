package com.devscope.engine.parser;

import com.devscope.util.PathUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class JavaParserEngine {

    private final ClassExtractor classExtractor;
    private final MethodExtractor methodExtractor;
    private final ImportExtractor importExtractor;
    private final AnnotationExtractor annotationExtractor;

    public JavaParserEngine(
            ClassExtractor classExtractor,
            MethodExtractor methodExtractor,
            ImportExtractor importExtractor,
            AnnotationExtractor annotationExtractor
    ) {
        this.classExtractor = classExtractor;
        this.methodExtractor = methodExtractor;
        this.importExtractor = importExtractor;
        this.annotationExtractor = annotationExtractor;
    }

    public List<ParsedJavaClass> parseFiles(Path sourceRoot, List<Path> javaFiles) throws IOException {
        List<ParsedJavaClass> parsedClasses = new ArrayList<>();
        for (Path javaFile : javaFiles) {
            ParsedJavaClass parsed = parseFile(sourceRoot, javaFile);
            if (parsed != null) {
                parsedClasses.add(parsed);
            }
        }
        return parsedClasses;
    }

    public ParsedJavaClass parseFile(Path sourceRoot, Path javaFile) throws IOException {
        if (!PathUtils.isJavaSource(javaFile)) {
            return null;
        }

        String className = classExtractor.extractClassName(javaFile).orElse(null);
        if (className == null || className.isBlank()) {
            return null;
        }

        ParsedJavaClass parsed = new ParsedJavaClass();
        parsed.setFilePath(javaFile);
        parsed.setClassName(className);
        parsed.setPackageName(PathUtils.normalizePackage(sourceRoot, javaFile));
        parsed.setFullName(classExtractor.buildFullName(sourceRoot, javaFile, className));
        parsed.setAnnotations(annotationExtractor.extractAnnotations(javaFile));
        parsed.setMethods(methodExtractor.extractMethods(javaFile));
        parsed.setImports(importExtractor.extractImports(javaFile));
        parsed.setLinesOfCode((int) Files.lines(javaFile).count());

        return parsed;
    }
}
