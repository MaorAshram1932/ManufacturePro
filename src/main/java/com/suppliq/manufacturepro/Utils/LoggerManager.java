package com.suppliq.manufacturepro.Utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.logging.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static com.suppliq.manufacturepro.Utils.Globals.getProjectPath;

public class LoggerManager {
    // Main logger instance
    public static final Logger LOGGER = Logger.getLogger(LoggerManager.class.getName());
    private static final String LOGGING_CONFIG_PATH = "com/suppliq/manufacturepro/logging.properties";
    private static final String LOGS_DIR = "logs"; // Logs directory
    private static final String LOG_WATCH_SCRIPT = getProjectPath() + "/scripts/ManufactureLogs.bat";

    static {
        //LogViewerLauncher.startLogViewer();
        setupLogger();
        redirectSystemStreams();
        Runtime.getRuntime().addShutdownHook(new Thread(LoggerManager::clearOldLogs)); // Delete logs on exit
    }

    // Private constructor to prevent instantiation
    private LoggerManager() {
        throw new UnsupportedOperationException("LoggerManager is a utility class and cannot be instantiated");
    }

    private static void setupLogger() {
        try {
            Files.createDirectories(Paths.get(LOGS_DIR)); // Ensure "logs" directory exists

            // Load logging configuration from logging.properties
            LogManager.getLogManager().readConfiguration(
                    LoggerManager.class.getClassLoader().getResourceAsStream(LOGGING_CONFIG_PATH)
            );

            // Get log level from properties
            String logLevel = LogManager.getLogManager().getProperty(LoggerManager.class.getName() + ".level");
            if (logLevel != null) {
                LOGGER.setLevel(Level.parse(logLevel.toUpperCase()));
            }

        } catch (IOException e) {
            System.err.println("Could not load logging configuration: " + e.getMessage());
        }
    }

    /**
     * Clears old log files on app shutdown.
     * Ensures logs directory exists before deletion and handles locked files.
     */
    private static void clearOldLogs() {
        Path logsPath = Paths.get(LOGS_DIR);

        if (!Files.exists(logsPath)) {
            return; // No logs to delete
        }

        try (Stream<Path> logFiles = Files.list(logsPath)) {
            logFiles
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file); // Delete each log file
                        } catch (IOException e) {
                            System.err.println("Warning: Could not delete log file (possibly in use): " + file);
                        }
                    });

            System.out.println("Logs deleted successfully on app exit.");
        } catch (IOException e) {
            System.err.println("Error clearing logs directory: " + e.getMessage());
        }
    }

    // Function to redirect System.out and System.err to the logger
    public static void redirectSystemStreams() {
        // Custom PrintStream for System.out (INFO level)
        PrintStream customOut = new PrintStream(System.out) {
            @Override
            public void println(String x) {
                LOGGER.log(Level.INFO, x);
            }

            @Override
            public void println(Object x) {
                LOGGER.log(Level.INFO, String.valueOf(x));
            }
        };

        // Custom PrintStream for System.err (SEVERE level)
        PrintStream customErr = new PrintStream(System.err) {
            @Override
            public void println(String x) {
                LOGGER.log(Level.SEVERE, x);
            }

            @Override
            public void println(Object x) {
                LOGGER.log(Level.SEVERE, String.valueOf(x));
            }
        };

        // Redirect the streams
        System.setOut(customOut);
        System.setErr(customErr);
    }




    /**
     * Logs an informational message.
     *
     * @param message The message to log.
     */
    public static void logInfo(String message) {
        LOGGER.info(message);
    }

    /**
     * Logs a warning message.
     *
     * @param message The warning message to log.
     */
    public static void logWarning(String message) {
        LOGGER.warning(message);
    }

    /**
     * Logs an error message along with an exception stack trace.
     *
     * @param message The error message to log.
     * @param e       The exception to log.
     */
    public static void logError(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message, e);
    }

    /**
     * Logs a debug message (useful for development).
     * Note: Debug messages are usually not shown unless logging level is set to `FINE`.
     *
     * @param message The debug message to log.
     */
    public static void logDebug(String message) {
        LOGGER.fine(message);
    }





}
