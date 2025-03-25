package com.suppliq.manufacturepro.Utils;

import java.io.IOException;

import static com.suppliq.manufacturepro.Utils.Globals.getProjectPath;

public class LogViewerLauncher {
    private static Process logViewerProcess; // Store the process reference
    private static final String batFilePath = getProjectPath() + "/scripts/ManufactureLogs.bat";

    public static void startLogViewer() {
        try {
            logViewerProcess = new ProcessBuilder("cmd.exe", "/c", "start", "\"\"", "\"" + batFilePath + "\"").start();
            System.out.println("✅ Log viewer started.");

            // 🔹 Register shutdown hook to stop the log viewer when the app exits
            //Runtime.getRuntime().addShutdownHook(new Thread(LogViewerLauncher::stopLogViewer));

        } catch (IOException e) {
            System.err.println("❌ Failed to start log viewer: " + e.getMessage());
        }
    }

    public static void stopLogViewer() {
        if (logViewerProcess != null && logViewerProcess.isAlive()) {
            logViewerProcess.destroy();
            System.out.println("✅ Log viewer stopped.");
        } else {
            System.err.println("⚠ No log viewer process running.");
        }
    }
}
