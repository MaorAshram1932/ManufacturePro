package com.suppliq.manufacturepro.Utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.*;

public class FileFormatter extends Formatter {


    @Override
    public String format(LogRecord record) {
        String level = record.getLevel().getName();

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("[%s] %tF %tT %s%n",
                 level, record.getMillis(), record.getMillis(), record.getMessage()));

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
