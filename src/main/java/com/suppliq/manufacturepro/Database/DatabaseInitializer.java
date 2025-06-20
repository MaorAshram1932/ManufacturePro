package com.suppliq.manufacturepro.Database;

import com.suppliq.manufacturepro.Exceptions.ExceptionHandler;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String SQL_RESOURCE_PATH = "/com/suppliq/manufacturepro/init_app_data_db.sql";

    public static void initialize() {
        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement()) {

            // טען את קובץ ה-SQL מה-resources
            InputStream in = DatabaseInitializer.class.getResourceAsStream(SQL_RESOURCE_PATH);
            if (in == null) {
                throw new IllegalStateException("קובץ init_app_data_db.sql לא נמצא ב-resources במסלול: " + SQL_RESOURCE_PATH);
            }

            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            // פיצול לפקודות נפרדות (לפי ';') והרצה אחת אחת
            for (String command : sql.split(";")) {
                if (!command.trim().isEmpty()) {
                    statement.execute(command.trim());
                }
            }

        } catch (Exception e) {
            ExceptionHandler.handleException("⚠️ שגיאה באתחול מסד הנתונים", e, true);
        }
    }
}
