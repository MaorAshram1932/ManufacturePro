package com.suppliq.manufacturepro.Utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Globals {
    // App details
    public static final String APP_NAME = "ManufacturePro";
    public static final String COMPANY_NAME = "SuppliQ";
    public static final String VERSION = "1.0.0";

    // File paths
    public static final String LOGS_DIR = "logs";
    public static final String CONFIG_PATH = "config/customer.properties";

    // Logging levels
    public static final String DEFAULT_LOG_LEVEL = "INFO";

    private Globals() {
        throw new UnsupportedOperationException("GlobalConstants is a utility class and cannot be instantiated");
    }

    /**
     * Gets the project's root directory dynamically.
     *
     * @return Absolute path of the project root.
     */
    public static String getProjectPath() {
        Path projectPath = Paths.get("").toAbsolutePath(); // Gets current working directory
        return projectPath.toString();
    }
}
