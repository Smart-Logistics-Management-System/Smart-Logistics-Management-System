package com.example.lojistik.repository;

import android.os.Handler;
import android.os.Looper;

import com.example.lojistik.callback.ApiCallback;
import com.example.lojistik.model.ApiResponse;
import com.example.lojistik.model.LoginRequest;
import com.example.lojistik.model.RegisterRequest;
import com.example.lojistik.model.UserData;
import com.example.lojistik.network.ApiConfig;
import com.example.lojistik.network.HttpClient;

import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Concrete implementation of IUserRepository.
 * Handles user-related API calls using HttpClient.
 *
 * Follows SRP: Only responsible for orchestrating user API calls.
 * Follows DIP: Depends on HttpClient abstraction for network operations.
 * Follows OCP: New user operations can be added without modifying existing methods.
 *
 * Uses ExecutorService for background threading and Handler for main-thread callbacks,
 * ensuring UI operations are always safe.
 */
public class UserRepository implements IUserRepository {

    private final HttpClient httpClient;
    private final ExecutorService executor;
    private final Handler mainHandler;

    public UserRepository() {
        this.httpClient = new HttpClient();
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Constructor for dependency injection (testability).
     *
     * @param httpClient Custom HTTP client instance
     */
    public UserRepository(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.executor = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void register(RegisterRequest request, ApiCallback<Void> callback) {
        executor.execute(() -> {
            String url = ApiConfig.BASE_URL + ApiConfig.USERS_ENDPOINT;
            String jsonBody = request.toJson();

            ApiResponse<Void> response = httpClient.post(url, jsonBody);

            // Deliver result on main thread (safe for UI updates)
            mainHandler.post(() -> {
                if (response.isSuccess()) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(response);
                }
            });
        });
    }

    @Override
    public void login(LoginRequest request, ApiCallback<UserData> callback) {
        executor.execute(() -> {
            String url = ApiConfig.BASE_URL + ApiConfig.LOGIN_ENDPOINT;
            String jsonBody = request.toJson();

            ApiResponse<String> response = httpClient.postWithResponse(url, jsonBody);

            mainHandler.post(() -> {
                if (response.isSuccess() && response.getData() != null) {
                    try {
                        UserData userData = parseUserData(response.getData());
                        callback.onSuccess(ApiResponse.success(userData, response.getStatusCode()));
                    } catch (Exception e) {
                        callback.onError(ApiResponse.error("Yanıt işlenirken hata oluştu"));
                    }
                } else {
                    // Forward the error with its message
                    callback.onError(ApiResponse.error(
                            response.getMessage() != null ? response.getMessage() : "Giriş başarısız",
                            response.getStatusCode()));
                }
            });
        });
    }

    /**
     * Parses the JSON response body into a UserData object.
     * Uses Android's built-in JSONObject (no external dependency needed).
     *
     * @param jsonString The JSON response from login endpoint
     * @return Parsed UserData object
     */
    private UserData parseUserData(String jsonString) throws Exception {
        JSONObject json = new JSONObject(jsonString);
        return new UserData(
                json.getLong("id"),
                json.getString("firstName"),
                json.getString("lastName"),
                json.getString("email"),
                json.getString("role")
        );
    }
}
