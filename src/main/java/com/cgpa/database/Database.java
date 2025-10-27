package com.cgpa.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Centralized JDBC connection factory that reads properties from classpath file `db.properties`.
 */
public final class Database {
    private static final String PROPERTIES_PATH = "/db.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Load properties
            try (InputStream in = Database.class.getResourceAsStream(PROPERTIES_PATH)) {
                if (in == null) {
                    throw new IllegalStateException("Missing db.properties on classpath at " + PROPERTIES_PATH);
                }
                PROPERTIES.load(in);
            }
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC driver not found: " + e.getMessage());
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to load database properties: " + e.getMessage());
        }
    }

    private Database() {}

    public static Connection getConnection() throws SQLException {
        String url = PROPERTIES.getProperty("db.url");
        String user = PROPERTIES.getProperty("db.user");
        String pass = PROPERTIES.getProperty("db.password");
        return DriverManager.getConnection(url, user, pass);
    }
} 