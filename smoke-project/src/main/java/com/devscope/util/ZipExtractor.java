package com.devscope.util;

import com.devscope.exception.AnalysisException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipExtractor {

    private ZipExtractor() {
    }

    public static Path extractToTemp(Path zipPath) {
        if (zipPath == null) {
            throw new AnalysisException("Zip path is required");
        }

        try {
            Path targetDirectory = Files.createTempDirectory("devscope-extracted-");
            return extract(zipPath, targetDirectory);
        } catch (IOException exception) {
            throw new AnalysisException("Failed to prepare temp extraction directory", exception);
        }
    }

    public static Path extract(Path zipPath, Path targetDirectory) {
        if (zipPath == null || targetDirectory == null) {
            throw new AnalysisException("Zip path and target directory are required");
        }

        try {
            Files.createDirectories(targetDirectory);
            try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipPath))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    Path outputPath = targetDirectory.resolve(entry.getName()).normalize();
                    if (!outputPath.startsWith(targetDirectory)) {
                        throw new AnalysisException("Invalid zip entry path");
                    }

                    if (entry.isDirectory()) {
                        Files.createDirectories(outputPath);
                    } else {
                        Path parent = outputPath.getParent();
                        if (parent != null) {
                            Files.createDirectories(parent);
                        }
                        Files.copy(zis, outputPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                    zis.closeEntry();
                }
            }
            return targetDirectory;
        } catch (IOException exception) {
            throw new AnalysisException("Failed to extract project archive", exception);
        }
    }
}
