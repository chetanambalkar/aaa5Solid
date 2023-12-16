package com.smicc.aa5Solid;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FolderHandler {

    private Configuration config;
    private static final Logger logger = Logger.getLogger(FolderHandler.class.getName());

    public FolderHandler(Configuration config) {
        this.config = config;
    }

    public void handleFolder(Path folderPath) throws IOException {
        Date lastModifiedDate = new Date(Files.getLastModifiedTime(folderPath).toMillis());

        if (isOlderThanThreshold(lastModifiedDate)) {
            deleteFolderFromTemp(folderPath.toFile());
            moveFolderToTemp(folderPath, Paths.get(config.getTempPath()));
        } else {
            handleSubfolder(folderPath);
        }
    }

    private void handleSubfolder(Path subfolderPath) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(subfolderPath)) {
            for (Path fileOrFolderPath : directoryStream) {
                if (Files.isDirectory(fileOrFolderPath)) {
                    handleSubfolder(fileOrFolderPath);
                } else {
                    new FileHandler(config).handleFile(fileOrFolderPath);
                }
            }
        }
    }

    private void moveFolderToTemp(Path source, Path destination) {
        try {
            Files.move(source, destination.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            logger.info("Folder moved to temp folder: " + source.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error moving folder to temp folder", e);
        }
    }

    private void deleteFolderFromTemp(File folder) {
        Path tempFolderPath = Paths.get(config.getTempPath(), folder.getName());
        try {
            Files.walk(tempFolderPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            logger.info("Folder deleted from temp folder: " + tempFolderPath.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error deleting folder from temp folder", e);
        }
    }

    private boolean isOlderThanThreshold(Date lastModifiedDate) {
        Date deletionThreshold = calculateDeletionThreshold();
        return lastModifiedDate.before(deletionThreshold);
    }

    private Date calculateDeletionThreshold() {
        long currentMillis = System.currentTimeMillis();
        long thresholdMillis = currentMillis - TimeUnit.DAYS.toMillis(config.getDeletionFrequencyDays());
        return new Date(thresholdMillis);
    }
}
