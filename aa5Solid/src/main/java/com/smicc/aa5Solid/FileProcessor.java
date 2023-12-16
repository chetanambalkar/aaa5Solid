package com.smicc.aa5Solid;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileProcessor {

    private Configuration config;

    public FileProcessor(Configuration config) {
        this.config = config;
    }

    public void processFiles() throws IOException {
        for (String folderName : config.getFolderNames()) {
            Path folderPath = Paths.get(config.getRootPath(), folderName);
            try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folderPath)) {
                for (Path fileOrFolderPath : directoryStream) {
                    if (!Files.isDirectory(fileOrFolderPath)) {
                        new FileHandler(config).handleFile(fileOrFolderPath);
                    }
                }
            }
        }
    }
}
