package com.cgpa.backend.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Simple username/password store for faculty accounts.
 * Credentials are loaded from classpath resource `/faculties.properties` if present,
 * using the format: username=password per line. Falls back to a small default set.
 */
public class FacultyAuthService {
    private final Map<String, String> usernameToPassword = new HashMap<>();

    public FacultyAuthService() {
        loadFromProperties();
    }

    public boolean authenticate(String username, char[] password) {
        if (username == null || password == null) return false;
        String expected = usernameToPassword.get(username.trim());
        return expected != null && expected.contentEquals(new String(password));
    }

    private void loadFromProperties() {
        Properties props = new Properties();
        try (InputStream in = FacultyAuthService.class.getResourceAsStream("/faculties.properties")) {
            if (in != null) {
                props.load(new java.io.InputStreamReader(in, StandardCharsets.UTF_8));
            }
        } catch (IOException ignored) {}

        if (props.isEmpty()) {
            // Default credentials (edit as needed)
            usernameToPassword.put("admin", "admin123");
            usernameToPassword.put("faculty1", "pass123");
            return;
        }

        boolean usedNumbered = false;
        // First, support numbered pairs: username1/password1, username2/password2, ...
        for (int i = 1; ; i++) {
            String u = props.getProperty("username" + i);
            String p = props.getProperty("password" + i);
            if (u == null && p == null) {
                break;
            }
            if (u != null && p != null) {
                usernameToPassword.put(u.trim(), p);
                usedNumbered = true;
            }
        }

        // Fallback: legacy format username=password lines
        if (!usedNumbered) {
            for (String name : props.stringPropertyNames()) {
                usernameToPassword.put(name.trim(), props.getProperty(name));
            }
        }
    }
}

