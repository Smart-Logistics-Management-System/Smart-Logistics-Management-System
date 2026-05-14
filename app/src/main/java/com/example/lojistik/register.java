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
import android.widget.Toast;

import com.example.lojistik.callback.ApiCallback;
import com.example.lojistik.model.ApiResponse;
import com.example.lojistik.model.RegisterRequest;
import com.example.lojistik.repository.IUserRepository;
import com.example.lojistik.repository.UserRepository;

/**
 * Registration Fragment - handles user registration form, validation, and API call.
 *
 * Follows SRP: Only responsible for UI interaction and form validation.
 * Delegates data operations to IUserRepository (DIP).
 *
 * Follows DIP (Dependency Inversion Principle):
 * Depends on the IUserRepository interface, not the concrete UserRepository.
 * This makes the fragment testable with mock repositories.
 */
public class register extends Fragment {

    private EditText etFirstName, etLastName, etEmail, etPassword, etPasswordConfirm;
    private FrameLayout btnRegister;
    private TextView tvError, tvRegisterBtnText, tvGoToLogin;
    private ProgressBar pbLoading;

    /**
     * Repository for user operations.
     * Declared as interface type (DIP) - can be swapped for testing.
     */
    private IUserRepository userRepository;

    public register() {
        // Required empty public constructor
    }

    public static register newInstance() {
        return new register();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize repository (can be replaced with DI framework later)
        userRepository = new UserRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        bindViews(view);
        setupListeners();

        return view;
    }

    /**
     * Binds all UI views from the layout.
     * Separated from onCreateView for readability (SRP at method level).
     */
    private void bindViews(View view) {
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etEmail = view.findViewById(R.id.etRegEmail);
        etPassword = view.findViewById(R.id.etRegPassword);
        etPasswordConfirm = view.findViewById(R.id.etRegPasswordConfirm);
        btnRegister = view.findViewById(R.id.btnRegisterContainer);
        tvError = view.findViewById(R.id.tvRegisterError);
        tvRegisterBtnText = view.findViewById(R.id.tvRegisterBtnText);
        pbLoading = view.findViewById(R.id.pbRegisterLoading);
        tvGoToLogin = view.findViewById(R.id.tvGoToLogin);
    }

    /**
     * Sets up all click listeners.
     * Separated for clarity and maintainability.
     */
    private void setupListeners() {
        // Handle register button click
        btnRegister.setOnClickListener(v -> {
            if (validateForm()) {
                performRegistration();
            }
        });

        // Handle "Already have an account? Login" click
        tvGoToLogin.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        });
    }

    /**
     * Validates all form fields before submission.
     * @return true if all fields are valid
     */
    private boolean validateForm() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordConfirm = etPasswordConfirm.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            showError("Lütfen adınızı girin");
            etFirstName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            showError("Lütfen soyadınızı girin");
            etLastName.requestFocus();
            return false;
        }

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

        if (password.length() < 6) {
            showError("Şifre en az 6 karakter olmalıdır");
            etPassword.requestFocus();
            return false;
        }

        if (!password.equals(passwordConfirm)) {
            showError("Şifreler eşleşmiyor");
            etPasswordConfirm.requestFocus();
            return false;
        }

        hideError();
        return true;
    }

    /**
     * Performs the actual registration via the repository.
     * Creates an immutable RegisterRequest DTO and delegates to IUserRepository.
     */
    private void performRegistration() {
        setLoading(true);
        hideError();

        // Build immutable request DTO
        RegisterRequest request = new RegisterRequest(
                etFirstName.getText().toString().trim(),
                etLastName.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etPassword.getText().toString().trim()
        );

        // Delegate to repository (DIP - using interface, not concrete class)
        userRepository.register(request, new ApiCallback<Void>() {
            @Override
            public void onSuccess(ApiResponse<Void> response) {
                setLoading(false);

                // Show success message
                if (getContext() != null) {
                    Toast.makeText(getContext(),
                            "Kayıt başarılı! Giriş yapabilirsiniz.",
                            Toast.LENGTH_LONG).show();
                }

                // Navigate to login screen
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToLogin();
                }
            }

            @Override
            public void onError(ApiResponse<Void> response) {
                setLoading(false);
                showError(response.getMessage() != null
                        ? response.getMessage()
                        : "Kayıt işlemi başarısız oldu");
            }
        });
    }

    /**
     * Shows an error message on the form.
     */
    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the error message.
     */
    private void hideError() {
        tvError.setVisibility(View.GONE);
    }

    /**
     * Toggles loading state on the register button.
     * Disables the button to prevent double-submit.
     */
    private void setLoading(boolean loading) {
        if (loading) {
            tvRegisterBtnText.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
        } else {
            tvRegisterBtnText.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
        }
    }
}
