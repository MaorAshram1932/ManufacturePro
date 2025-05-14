package com.suppliq.manufacturepro.Utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.*;

public class ConsoleFormatter extends Formatter {
    // ANSI color codes for console formatting
    private static final String RESET = "\u001B[0m";  // Reset color
    private static final String RED = "\u001B[31m";   // Red (ERROR)
    private static final String YELLOW = "\u001B[33m"; // Yellow (WARNING)
    private static final String BLUE = "\u001B[34m";  // Blue (INFO)
    private static final String CYAN = "\u001B[36m";  // Cyan (DEBUG)

    @Override
    public String format(LogRecord record) {
        String level = record.getLevel().getName();

        String color;

        // Assign color based on log level
        switch (level) {
            case "SEVERE":
                level = "ERROR";
                color = RED;
                break;
            case "WARNING":
                color = YELLOW;
                break;
            case "INFO":
                color = BLUE;
                break;
            case "FINE":
            case "DEBUG":
                color = CYAN;
                break;
            default:
                color = RESET;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s[%s] %tF %tT %s%s%n",
                color, level, record.getMillis(), record.getMillis(), record.getMessage(), RESET));

        // Include stack trace if exception is present
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            builder.append(sw).append("\n");
        }

        return builder.toString();
    }
}
