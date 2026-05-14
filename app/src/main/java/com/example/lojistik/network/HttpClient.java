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

            // Write request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
                os.flush();
            }

            // Read response
            int responseCode = connection.getResponseCode();

            if (responseCode >= 200 && responseCode < 300) {
                return ApiResponse.success(responseCode);
            } else {
                String errorBody = readStream(connection);
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
     * Reads the error stream from the connection.
     */
    private String readStream(HttpURLConnection connection) {
        try {
            BufferedReader reader;
            if (connection.getErrorStream() != null) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            } else if (connection.getInputStream() != null) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                return "";
            }

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
        if (statusCode == 409) {
            return "Bu e-posta adresi zaten kayıtlı";
        } else if (statusCode == 400) {
            return "Geçersiz bilgiler, lütfen tekrar kontrol edin";
        } else if (statusCode >= 500) {
            return "Sunucu hatası, lütfen daha sonra tekrar deneyin";
        } else if (errorBody != null && !errorBody.isEmpty()) {
            return errorBody;
        }
        return "Bilinmeyen hata oluştu (Kod: " + statusCode + ")";
    }
}
