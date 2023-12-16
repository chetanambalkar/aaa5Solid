package com.smicc.aa5Solid;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationLoader {

    public static Configuration loadConfiguration() {
        Properties properties = new Properties();
        Configuration config = new Configuration();

        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);

            config.setRootPath(properties.getProperty("ROOT_PATH"));
            config.setTempPath(properties.getProperty("TEMP_PATH"));
            config.setDeletionFrequencyDays(Integer.parseInt(properties.getProperty("DELETION_FREQUENCY_DAYS")));
            config.setFolderNames(Arrays.asList(properties.getProperty("FOLDER_NAMES").split(",")));

        } catch (IOException | NumberFormatException e) {
            Logger.getLogger(ConfigurationLoader.class.getName()).log(Level.SEVERE, "Error loading configuration", e);
            System.exit(1);
        }

        return config;
    }
}
