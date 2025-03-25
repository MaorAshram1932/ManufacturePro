package com.suppliq.manufacturepro.Exceptions;

import com.suppliq.manufacturepro.Utils.LoggerManager;

public class ExceptionHandler {
    /**
     * Handles exceptions by logging and optionally stopping execution.
     *
     * @param message   The error message.
     * @param e         The exception instance.
     * @param critical  If true, performs cleanup and exits.
     */
    public static void handleException(String message, Exception e, boolean critical) {
        LoggerManager.logError(message, e);

        if (critical) {
            gracefulShutdown();
            throw new CriticalAppException(message, e); // Throwing ensures stack trace is preserved
        }
    }

    /**
     * Gracefully shuts down the application before exit.
     */
    private static void gracefulShutdown() {
        System.err.println("A critical error occurred. Attempting to shut down gracefully...");

        try {


            System.err.println("Cleanup completed. Exiting application.");
        } catch (Exception cleanupError) {
            System.err.println("Error during cleanup: " + cleanupError.getMessage());
            LoggerManager.logError("Error during cleanup", cleanupError);
        }

        System.exit(1); // Exit only after cleaning up
    }

    /**
     * Sets a global exception handler for catching uncaught exceptions in any thread.
     */
    public static void setupGlobalExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((thread, e) -> {
            LoggerManager.logError("Uncaught exception in thread: " + thread.getName(), (Exception) e);
            gracefulShutdown();
        });
    }
}
