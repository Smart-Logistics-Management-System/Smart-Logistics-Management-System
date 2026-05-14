package com.example.lojistik.model;

/**
 * Data Transfer Object for user registration request.
 * Maps to the backend CreateUserRequest DTO.
 *
 * Follows SRP: Only responsible for holding registration data.
 */
public class RegisterRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

    public RegisterRequest(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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
                + "\"firstName\":\"" + escapeJson(firstName) + "\","
                + "\"lastName\":\"" + escapeJson(lastName) + "\","
                + "\"email\":\"" + escapeJson(email) + "\","
                + "\"password\":\"" + escapeJson(password) + "\""
                + "}";
    }

    /**
     * Escapes special characters for JSON safety.
     */
    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
