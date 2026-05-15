package com.example.lojistik.network;

import com.example.lojistik.model.ApiResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Low-level HTTP client responsible for making network requests.
 * Encapsulates all HttpURLConnection details.
 *
 * Follows SRP: Only responsible for HTTP communication mechanics.
 * Follows OCP: Can be extended with new HTTP methods without modifying existing ones.
 */
public class HttpClient {

    /**
     * Performs a POST request with a JSON body.
     *
     * @param urlString The full URL to send the request to
     * @param jsonBody  The JSON string to send as request body
     * @return ApiResponse containing success/failure status
     */
    public ApiResponse<Void> post(String urlString, String jsonBody) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            // Configure connection
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(ApiConfig.CONNECT_TIMEOUT);
            connection.setReadTimeout(ApiConfig.READ_TIMEOUT);
            connection.setDoOutput(true);

            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            connection.setFixedLengthStreamingMode(input.length);

            // Write request body
            try (OutputStream os = connection.getOutputStream()) {
                os.write(input, 0, input.length);
                os.flush();
            }

            // Read response
            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                try { connection.getInputStream().close(); } catch (Exception ignored) {}
                return ApiResponse.success(responseCode);
            } else {
                String errorBody = readErrorStream(connection);
                String errorMessage = parseErrorMessage(errorBody, responseCode);
                return ApiResponse.error(errorMessage, responseCode);
            }

        } catch (IOException e) {
            return ApiResponse.error("Bağlantı hatası: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Performs a POST request and returns the response body as String data.
     * Used for endpoints that return JSON data (e.g., login).
     *
     * @param urlString The full URL to send the request to
     * @param jsonBody  The JSON string to send as request body
     * @return ApiResponse containing the response body string on success
     */
    public ApiResponse<String> postWithResponse(String urlString, String jsonBody) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(ApiConfig.CONNECT_TIMEOUT);
            connection.setReadTimeout(ApiConfig.READ_TIMEOUT);
            connection.setDoOutput(true);

            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            connection.setFixedLengthStreamingMode(input.length);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(input, 0, input.length);
                os.flush();
            }

            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                String responseBody = readSuccessStream(connection);
                return ApiResponse.success(responseBody, responseCode);
            } else {
                String errorBody = readErrorStream(connection);
                String errorMessage = parseErrorMessage(errorBody, responseCode);
                return ApiResponse.error(errorMessage, responseCode);
            }

        } catch (IOException e) {
            return ApiResponse.error("Bağlantı hatası: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Performs a GET request and returns the response body as String data.
     * Used for fetching data like the user list.
     *
     * @param urlString The full URL to send the GET request to
     * @return ApiResponse containing the response body string on success
     */
    public ApiResponse<String> getWithResponse(String urlString) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(ApiConfig.CONNECT_TIMEOUT);
            connection.setReadTimeout(ApiConfig.READ_TIMEOUT);

            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                String responseBody = readSuccessStream(connection);
                return ApiResponse.success(responseBody, responseCode);
            } else {
                String errorBody = readErrorStream(connection);
                String errorMessage = parseErrorMessage(errorBody, responseCode);
                return ApiResponse.error(errorMessage, responseCode);
            }

        } catch (IOException e) {
            return ApiResponse.error("Bağlantı hatası: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Performs a PUT request.
     */
    public ApiResponse<Void> put(String urlString, String jsonBody) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(ApiConfig.CONNECT_TIMEOUT);
            connection.setReadTimeout(ApiConfig.READ_TIMEOUT);

            if (jsonBody != null && !jsonBody.isEmpty()) {
                connection.setDoOutput(true);
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(input, 0, input.length);
                    os.flush();
                }
            }

            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                try { connection.getInputStream().close(); } catch (Exception ignored) {}
                return ApiResponse.success(responseCode);
            } else {
                String errorBody = readErrorStream(connection);
                String errorMessage = parseErrorMessage(errorBody, responseCode);
                return ApiResponse.error(errorMessage, responseCode);
            }

        } catch (IOException e) {
            return ApiResponse.error("Bağlantı hatası: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Performs a DELETE request.
     */
    public ApiResponse<Void> delete(String urlString) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(ApiConfig.CONNECT_TIMEOUT);
            connection.setReadTimeout(ApiConfig.READ_TIMEOUT);

            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                try { connection.getInputStream().close(); } catch (Exception ignored) {}
                return ApiResponse.success(responseCode);
            } else {
                String errorBody = readErrorStream(connection);
                String errorMessage = parseErrorMessage(errorBody, responseCode);
                return ApiResponse.error(errorMessage, responseCode);
            }

        } catch (IOException e) {
            return ApiResponse.error("Bağlantı hatası: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Reads the success response body from the connection's input stream.
     */
    private String readSuccessStream(HttpURLConnection connection) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Reads the error stream from the connection.
     */
    private String readErrorStream(HttpURLConnection connection) {
        try {
            if (connection.getErrorStream() == null) return "";
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Parses error message from response body or generates a user-friendly message.
     */
    private String parseErrorMessage(String errorBody, int statusCode) {
        // Try to extract "message" field from JSON error body
        if (errorBody != null && errorBody.contains("\"message\"")) {
            try {
                int start = errorBody.indexOf("\"message\":\"") + 11;
                int end = errorBody.indexOf("\"", start);
                if (start > 10 && end > start) {
                    return errorBody.substring(start, end);
                }
            } catch (Exception ignored) {}
        }

        if (statusCode == 401) {
            return "Şifre hatalı";
        } else if (statusCode == 404) {
            return "İstenen kaynak bulunamadı (404)";
        } else if (statusCode == 409) {
            return "Bu e-posta adresi zaten kayıtlı";
        } else if (statusCode == 400) {
            return "Geçersiz bilgiler, lütfen tekrar kontrol edin";
        } else if (statusCode >= 500) {
            return "Sunucu hatası, lütfen daha sonra tekrar deneyin";
        }
        return "Bilinmeyen hata oluştu (Kod: " + statusCode + ")";
    }
}
