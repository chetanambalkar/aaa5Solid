package com.smicc.aa5Solid;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHandler {

    private Configuration config;
    private static final Logger logger = Logger.getLogger(FileHandler.class.getName());

    public FileHandler(Configuration config) {
        this.config = config;
    }

    public void handleFile(Path filePath) throws IOException {
        Date lastModifiedDate = new Date(Files.getLastModifiedTime(filePath).toMillis());
        if (isOlderThanThreshold(lastModifiedDate)) {
            deleteFileFromTemp(filePath.toFile());
            moveFileToTemp(filePath, Paths.get(config.getTempPath()));
        }
    }

    private void moveFileToTemp(Path source, Path destination) {
        try {
            Files.move(source, destination.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            logger.info("File moved to temp folder: " + source.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error moving file to temp folder", e);
        }
    }

    private void deleteFileFromTemp(File file) {
        Path tempFilePath = Paths.get(config.getTempPath(), file.getName());
        try {
            Files.deleteIfExists(tempFilePath);
            logger.info("File deleted from temp folder: " + tempFilePath.toString());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error deleting file from temp folder", e);
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

