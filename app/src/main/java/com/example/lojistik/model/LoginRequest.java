package com.example.lojistik.model;

/**
 * Immutable DTO for login request data.
 * Maps to backend LoginRequest DTO (email + password).
 *
 * Follows SRP: Only responsible for holding login credentials.
 */
public class LoginRequest {
    private final String email;
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Converts this object to a JSON string for API transmission.
     * @return JSON string representation
     */
    public String toJson() {
        return "{"
                + "\"email\":\"" + escapeJson(email) + "\","
                + "\"password\":\"" + escapeJson(password) + "\""
                + "}";
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
