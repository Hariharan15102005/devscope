package com.devscope.engine.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.devscope.util.PathUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@SuppressWarnings("unused")
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

        CompilationUnit compilationUnit = StaticJavaParser.parse(javaFile);
        Optional<ClassOrInterfaceDeclaration> primaryClass = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class);
        if (primaryClass.isEmpty()) {
            return null;
        }

        String className = primaryClass.get().getNameAsString();
        String packageName = compilationUnit.getPackageDeclaration()
                .map(pkg -> pkg.getName().asString())
                .orElseGet(() -> PathUtils.normalizePackage(sourceRoot, javaFile));
        String fullName = packageName == null || packageName.isBlank() ? className : packageName + "." + className;

        List<String> imports = compilationUnit.getImports().stream()
                .map(item -> item.getName().asString())
                .toList();

        List<String> annotations = primaryClass.get().getAnnotations().stream()
                .map(annotationExpr -> annotationExpr.getName().asString())
                .toList();

        List<String> methods = primaryClass.get().findAll(MethodDeclaration.class).stream()
                .map(MethodDeclaration::getDeclarationAsString)
                .toList();

        int lineCount;
        try (var lines = Files.lines(javaFile)) {
            lineCount = (int) lines.count();
        }

        ParsedJavaClass parsed = new ParsedJavaClass();
        parsed.setFilePath(javaFile);
        parsed.setClassName(className);
        parsed.setPackageName(packageName);
        parsed.setFullName(fullName);
        parsed.setAnnotations(annotations);
        parsed.setMethods(methods);
        parsed.setImports(imports);
        parsed.setLinesOfCode(lineCount);

        return parsed;
    }
}
