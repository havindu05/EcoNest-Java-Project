package com.econest.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;
    private final String URL = "jdbc:mysql://localhost:3306/econest_db";
    private final String USER = "root";
    private final String PASSWORD = "12345678";

    private DBConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("MySQL JDBC Driver not found.", ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DBConnection();
        }
        return instance;
    }
}
