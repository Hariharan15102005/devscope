package com.devscope.engine.scanner;

import com.devscope.util.FileWalker;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Component
public class JavaFileScanner {

    public List<Path> scanJavaFiles(Path rootDirectory) throws IOException {
        return FileWalker.listJavaFiles(rootDirectory);
    }
}
