package com.devscope.engine.scanner;

import com.devscope.util.FileWalker;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class ProjectScanner {

    private final JavaFileScanner javaFileScanner;

    public ProjectScanner(JavaFileScanner javaFileScanner) {
        this.javaFileScanner = javaFileScanner;
    }

    public List<Path> scanProject(Path extractedProjectRoot) throws IOException {
        return javaFileScanner.scanJavaFiles(extractedProjectRoot);
    }

    public List<Path> scanSourceFolder(Path sourceFolder) throws IOException {
        return FileWalker.listJavaFiles(sourceFolder);
    }
}
