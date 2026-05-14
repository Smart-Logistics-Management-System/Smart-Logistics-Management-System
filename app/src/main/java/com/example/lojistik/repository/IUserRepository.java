package com.example.lojistik.repository;

import com.example.lojistik.callback.ApiCallback;
import com.example.lojistik.model.LoginRequest;
import com.example.lojistik.model.RegisterRequest;
import com.example.lojistik.model.UserData;

/**
 * Interface for user-related data operations.
 *
 * Follows DIP (Dependency Inversion Principle):
 * High-level modules (Fragments) depend on this abstraction,
 * not on the concrete implementation.
 *
 * Follows ISP (Interface Segregation Principle):
 * Only declares methods relevant to user operations.
 */
public interface IUserRepository {

    /**
     * Registers a new user with the backend service.
     * Runs asynchronously - result delivered via callback on the main thread.
     *
     * @param request  The registration data
     * @param callback Callback for success/error handling
     */
    void register(RegisterRequest request, ApiCallback<Void> callback);

    /**
     * Authenticates a user with email and password.
     * On success, returns UserData containing the user's profile info.
     *
     * @param request  The login credentials
     * @param callback Callback delivering UserData on success
     */
    void login(LoginRequest request, ApiCallback<UserData> callback);
}
