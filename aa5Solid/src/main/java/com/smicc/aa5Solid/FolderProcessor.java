package com.smicc.aa5Solid;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderProcessor {

    private Configuration config;

    public FolderProcessor(Configuration config) {
        this.config = config;
    }

    public void processFolders() throws IOException {
        for (String folderName : config.getFolderNames()) {
            Path folderPath = Paths.get(config.getRootPath(), folderName);
            FolderHandler folderHandler = new FolderHandler(config);
            folderHandler.handleFolder(folderPath);
        }
    }
}
