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

/**
 * Registration Fragment - handles user registration form and validation.
 * Sends registration data to the backend user-service API.
 */
public class register extends Fragment {

    private EditText etFirstName, etLastName, etEmail, etPassword, etPasswordConfirm;
    private FrameLayout btnRegister;
    private TextView tvError, tvRegisterBtnText, tvGoToLogin;
    private ProgressBar pbLoading;

    public register() {
        // Required empty public constructor
    }

    public static register newInstance() {
        return new register();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        // Bind views
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

        return view;
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
     * Performs the registration API call.
     * For now, navigates to login on success.
     * TODO: Connect to actual backend API when available.
     */
    private void performRegistration() {
        setLoading(true);

        // For now, simulate a successful registration and go to login
        // TODO: Replace with actual API call to user-service POST /api/users
        btnRegister.postDelayed(() -> {
            setLoading(false);
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToLogin();
            }
        }, 1000);
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
