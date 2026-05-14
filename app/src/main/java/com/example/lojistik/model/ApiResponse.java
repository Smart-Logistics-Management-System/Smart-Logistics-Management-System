package com.example.lojistik.model;

/**
 * Generic wrapper for API responses.
 * Encapsulates success/failure state with an optional message.
 *
 * Follows SRP: Only responsible for representing an API result.
 * Follows OCP: Can be extended for specific response types without modification.
 *
 * @param <T> The type of data returned on success (use Void for no-body responses)
 */
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private final String message;
    private final int statusCode;

    private ApiResponse(boolean success, T data, String message, int statusCode) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.statusCode = statusCode;
    }

    /**
     * Factory method for successful responses.
     */
    public static <T> ApiResponse<T> success(T data, int statusCode) {
        return new ApiResponse<>(true, data, null, statusCode);
    }

    /**
     * Factory method for successful responses without data.
     */
    public static <T> ApiResponse<T> success(int statusCode) {
        return new ApiResponse<>(true, null, null, statusCode);
    }

    /**
     * Factory method for error responses.
     */
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return new ApiResponse<>(false, null, message, statusCode);
    }

    /**
     * Factory method for network/exception errors.
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message, -1);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
