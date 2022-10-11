package com.nfcs.singularity.core.code;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Compiler {
    public static String compileFile(@NotNull String filename) throws IOException {
        StringBuilder results = new StringBuilder();
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<File> files = Files.list(Paths.get(filename)).map(Path::toFile).collect(Collectors.toList());

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, fileManager.getJavaFileObjectsFromFiles(files));
            task.call();

            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                results.append(new Formatter().format("Error at line %d in %s", diagnostic.getLineNumber(), diagnostic.getSource()));
                log.error("Error at line {} in {}", diagnostic.getLineNumber(), diagnostic.getSource());
            }
        }

        return results.toString();
    }
}