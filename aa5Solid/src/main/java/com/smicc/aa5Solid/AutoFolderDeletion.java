package com.smicc.aa5Solid;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoFolderDeletion {

    public static void main(String[] args) {
        Configuration config = ConfigurationLoader.loadConfiguration();

        FolderProcessor folderProcessor = new FolderProcessor(config);
        FileProcessor fileProcessor = new FileProcessor(config);

        while (true) {
            try {
                folderProcessor.processFolders();
                fileProcessor.processFiles();
            } catch (IOException e) {
                Logger.getLogger(AutoFolderDeletion.class.getName()).log(Level.SEVERE, "Error during file and folder processing", e);
            }

            // Sleep for the specified frequency
            try {
                Thread.sleep(TimeUnit.DAYS.toMillis(config.getDeletionFrequencyDays()));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                Logger.getLogger(AutoFolderDeletion.class.getName()).log(Level.WARNING, "Thread interrupted", e);
            }
        }
    }
}
