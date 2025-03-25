package com.suppliq.manufacturepro.Database;

import com.suppliq.manufacturepro.Exceptions.ExceptionHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    //String url = "jdbc:mysql://localhost:3306/app_data?useUnicode=true&characterEncoding=utf8mb4";
    private static final String URL = "jdbc:mysql://localhost:3306/app_data";
    private static final String USER = "root";
    private static final String PASSWORD = "mysql";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            ExceptionHandler.handleException("Database connection failed", e, true);
            return null;
        }
    }
}
