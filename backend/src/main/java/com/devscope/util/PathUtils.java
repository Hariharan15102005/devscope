package com.devscope.util;

import java.nio.file.Path;

public final class PathUtils {

    private PathUtils() {
    }

    public static boolean isJavaSource(Path path) {
        if (path == null || path.getFileName() == null) {
            return false;
        }
        return path.getFileName().toString().endsWith(".java");
    }

    public static String normalizePackage(Path sourceRoot, Path javaFile) {
        if (sourceRoot == null || javaFile == null) {
            return "";
        }

        Path relative = sourceRoot.relativize(javaFile.getParent());
        String packagePath = relative.toString().replace('\\', '.').replace('/', '.');
        if (packagePath.startsWith(".")) {
            return packagePath.substring(1);
        }
        return packagePath;
    }
}
