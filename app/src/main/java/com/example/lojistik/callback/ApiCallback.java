package com.example.lojistik.callback;

import com.example.lojistik.model.ApiResponse;

/**
 * Generic callback interface for asynchronous API operations.
 *
 * Follows ISP (Interface Segregation Principle):
 * Consumers only need to implement the methods relevant to their use case.
 *
 * @param <T> The type of data returned on success
 */
public interface ApiCallback<T> {

    /**
     * Called when the API operation completes successfully.
     *
     * @param response The successful API response
     */
    void onSuccess(ApiResponse<T> response);

    /**
     * Called when the API operation fails (network error, server error, etc).
     *
     * @param response The error API response with message
     */
    void onError(ApiResponse<T> response);
}
