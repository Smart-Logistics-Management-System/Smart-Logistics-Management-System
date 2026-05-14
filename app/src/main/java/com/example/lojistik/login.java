package com.example.lojistik;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lojistik.callback.ApiCallback;
import com.example.lojistik.model.ApiResponse;
import com.example.lojistik.model.LoginRequest;
import com.example.lojistik.model.UserData;
import com.example.lojistik.repository.IUserRepository;
import com.example.lojistik.repository.UserRepository;

/**
 * Login Fragment - handles user authentication form, validation, and API call.
 *
 * Follows SRP: Only responsible for UI interaction and form validation.
 * Delegates authentication to IUserRepository (DIP).
 */
public class login extends Fragment {

    private EditText etEmail, etPassword;
    private FrameLayout btnLogin;
    private TextView tvError, tvLoginBtnText, tvGoToRegister;
    private ProgressBar pbLoading;

    /**
     * Repository for user operations.
     * Declared as interface type (DIP) - can be swapped for testing.
     */
    private IUserRepository userRepository;

    public login() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userRepository = new UserRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        bindViews(view);
        setupListeners();

        return view;
    }

    /**
     * Binds all UI views from the layout.
     */
    private void bindViews(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLoginContainer);
        tvError = view.findViewById(R.id.tvLoginError);
        tvLoginBtnText = view.findViewById(R.id.tvLoginBtnText);
        pbLoading = view.findViewById(R.id.pbLoginLoading);
        tvGoToRegister = view.findViewById(R.id.tvGoToRegister);
    }

    /**
     * Sets up all click listeners.
     */
    private void setupListeners() {
        // Handle login button click
        btnLogin.setOnClickListener(v -> {
            if (validateForm()) {
                performLogin();
            }
        });

        // Handle "Don't have an account? Register" click
        tvGoToRegister.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToRegister();
            }
        });
    }

    /**
     * Validates email and password fields before submission.
     * @return true if all fields are valid
     */
    private boolean validateForm() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            showError("Lütfen e-posta adresinizi girin");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("Geçerli bir e-posta adresi girin");
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            showError("Lütfen şifrenizi girin");
            etPassword.requestFocus();
            return false;
        }

        hideError();
        return true;
    }

    /**
     * Performs the actual login via the repository.
     * Creates an immutable LoginRequest DTO and delegates to IUserRepository.
     */
    private void performLogin() {
        setLoading(true);
        hideError();

        LoginRequest request = new LoginRequest(
                etEmail.getText().toString().trim(),
                etPassword.getText().toString().trim()
        );

        userRepository.login(request, new ApiCallback<UserData>() {
            @Override
            public void onSuccess(ApiResponse<UserData> response) {
                setLoading(false);

                if (getActivity() instanceof MainActivity) {
                    // Pass user data to MainActivity and navigate to main screen
                    ((MainActivity) getActivity()).onLoginSuccess(response.getData());
                }
            }

            @Override
            public void onError(ApiResponse<UserData> response) {
                setLoading(false);
                showError(response.getMessage() != null
                        ? response.getMessage()
                        : "Giriş başarısız oldu");
            }
        });
    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        tvError.setVisibility(View.GONE);
    }

    private void setLoading(boolean loading) {
        if (loading) {
            tvLoginBtnText.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        } else {
            tvLoginBtnText.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
        }
    }
}