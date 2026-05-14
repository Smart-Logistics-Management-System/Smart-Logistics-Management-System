package com.example.lojistik.network;

/**
 * Centralized API configuration constants.
 * Single source of truth for all endpoint URLs and timeouts.
 *
 * Follows SRP: Only responsible for API configuration.
 * Follows OCP: New endpoints can be added without modifying existing ones.
 */
public final class ApiConfig {

    private ApiConfig() {
        // Private constructor prevents instantiation (utility class)
    }

    /**
     * Base URL for the backend API.
     * For Android emulator, 10.0.2.2 maps to host machine's localhost.
     * For physical device, use your computer's local IP address.
     */
    public static final String BASE_URL = "http://10.0.2.2:8081";

    // ─── User Service Endpoints ─────────────────────────────────
    public static final String USERS_ENDPOINT = "/api/users";
    public static final String LOGIN_ENDPOINT = "/api/users/login";

    // ─── Timeouts (milliseconds) ────────────────────────────────
    public static final int CONNECT_TIMEOUT = 10_000;
    public static final int READ_TIMEOUT = 15_000;
}
