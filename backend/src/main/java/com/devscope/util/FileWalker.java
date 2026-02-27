package com.devscope.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public final class FileWalker {

    private FileWalker() {
    }

    public static List<Path> listJavaFiles(Path root) throws IOException {
        if (root == null || !Files.exists(root)) {
            return List.of();
        }

        try (Stream<Path> stream = Files.walk(root)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(PathUtils::isJavaSource)
                    .toList();
        }
    }
}
