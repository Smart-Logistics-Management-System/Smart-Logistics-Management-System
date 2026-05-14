package com.example.lojistik.model;

/**
 * Immutable DTO representing the logged-in user's data.
 * Returned from the login API on successful authentication.
 *
 * Follows SRP: Only responsible for holding user profile data.
 * Note: Password is intentionally excluded for security.
 */
public class UserData {
    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String role;

    public UserData(long id, String firstName, String lastName, String email, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public long getId() {
        return id;
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

    public String getRole() {
        return role;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
