package com.suppliq.manufacturepro.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import static com.suppliq.manufacturepro.Utils.LoggerManager.*;

public class ConfigManager {
    private static final String APP_CONFIG_PATH = "/com/suppliq/manufacturepro/app-config.properties";  // Inside JAR
    private static final String CUSTOMER_CONFIG_PATH = "config/customer-config.properties";  // External file

    private static final Properties appProperties = new Properties();
    private static final Properties customerProperties = new Properties();

    static {
        loadProperties(appProperties, APP_CONFIG_PATH, true);  // Load from JAR
        loadProperties(customerProperties, CUSTOMER_CONFIG_PATH, false); // Load from external folder
        logInfo("ConfigManager Loaded");
    }

//    private static void loadProperties(Properties properties, String path, boolean fromResources) {
//        try (InputStream input = fromResources ?
//                ConfigManager.class.getResourceAsStream(path) :
//                new FileInputStream(path)) {
//
//            if (input != null) {
//                properties.load(input);
//            } else {
//                System.err.println("Warning: Could not load properties file: " + path);
//            }
//        } catch (IOException e) {
//            System.err.println("Error loading config file: " + path);
//            e.printStackTrace();
//        }
//    }


    private static void loadProperties(Properties properties, String path, boolean fromResources) {
        try (InputStream input = fromResources ?
                ConfigManager.class.getResourceAsStream(path) :
                new FileInputStream(path);
             Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(reader)) {

            properties.load(br);

            // Debugging: Print raw bytes
            logInfo("Loaded config: " + properties.toString());

        } catch (IOException e) {
            logError("Error loading config file: " + path,e);
            //throw new RuntimeException("Error loading config file: " + path,e);
            //e.printStackTrace();
        }
    }


    // Get app config (fixed settings)
    public static String getAppConfig(String key) {
        return appProperties.getProperty(key, "Not Found");
    }

    // Get customer config (user settings)
    public static String getCustomerConfig(String key) {
        return customerProperties.getProperty(key, "Not Found");
    }
}
