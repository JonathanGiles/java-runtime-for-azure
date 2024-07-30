package com.azure.runtime.host.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtilities {
    // FIXME eventually we should have a global context or similar, rather than smuggling through ThreadLocal
    private static final ThreadLocal<Path> OUTPUT_PATH_THREAD_LOCAL = new ThreadLocal<>();

    private static final String EXECUTION_DIR = System.getProperty("user.dir");

    private static Path outputRelativePath;

    private FileUtilities() {  }

    public static void setOutputPath(Path outputPath) {
        OUTPUT_PATH_THREAD_LOCAL.set(outputPath);

//        // when the output path is set, we calculate the relative path to the execution directory
//        outputRelativePath = Paths.get(EXECUTION_DIR).relativize(outputPath);

        // Ensure outputPath is absolute
        Path absoluteOutputPath = outputPath.toAbsolutePath();

        // Ensure executionDirPath is absolute
        Path executionDirPath = Paths.get(EXECUTION_DIR).toAbsolutePath();

        // Check if both paths share the same root
        if (!absoluteOutputPath.getRoot().equals(executionDirPath.getRoot())) {
            throw new IllegalArgumentException("Output path and execution directory do not share the same root");
        }

        // Calculate the relative path to the execution directory
        outputRelativePath = executionDirPath.relativize(absoluteOutputPath);
    }

    public static Path getOutputPath() {
        return OUTPUT_PATH_THREAD_LOCAL.get();
    }

    /**
     * Prepends the output relative path to the given path.
     * @param path
     * @return
     */
    public static Path convertOutputPathToRootRelative(String path) {
        Objects.requireNonNull(path, "Path cannot be null");

        // Retrieve the output path
        Path outputPath = getOutputPath();
        if (outputPath == null) {
            throw new IllegalStateException("Output path has not been set");
        }

        // Resolve the given path against the output path and then convert to absolute path
        Path absolutePath = outputPath.resolve(path).toAbsolutePath();
        Path absoluteOutputPath = outputRelativePath.toAbsolutePath();

        // Ensure both paths share the same root
        if (!absolutePath.getRoot().equals(absoluteOutputPath.getRoot())) {
            throw new IllegalArgumentException("Paths do not share the same root");
        }

        // Proceed with relativization
        Path relativePath = absoluteOutputPath.relativize(absolutePath);

        return Paths.get(outputRelativePath.toString(), relativePath.toString());
    }

    /**
     * Converts a path that is relative to the root directory to a path that is relative to the output directory.
     * @param value
     * @return
     */
    public static Path convertRootRelativePathToOutputPath(String value) {
        Objects.requireNonNull(value, "Path cannot be null");
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Path cannot be empty");
        }

        Path executionDirPath = Paths.get(EXECUTION_DIR);
        Path outputPath = getOutputPath(); // Assuming getOutputPath() retrieves the current output path
        if (outputPath == null) {
            throw new IllegalStateException("Output path has not been set");
        }

        Path absoluteInputPath = executionDirPath.resolve(value).toAbsolutePath();
        Path absoluteOutputPath = outputPath.toAbsolutePath();

        return absoluteOutputPath.relativize(absoluteInputPath);
    }
}
