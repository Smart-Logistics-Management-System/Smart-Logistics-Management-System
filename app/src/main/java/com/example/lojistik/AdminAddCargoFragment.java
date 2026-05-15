package com.example.lojistik;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lojistik.model.ApiResponse;
import com.example.lojistik.model.CreateCargoRequest;
import com.example.lojistik.network.ApiConfig;
import com.example.lojistik.network.HttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fragment for Admin to create a new cargo.
 */
public class AdminAddCargoFragment extends Fragment {

    private EditText etSenderId, etReceiverId, etWeight;
    private FrameLayout btnSaveCargo;
    private TextView tvError, tvSaveText;
    private ProgressBar pbLoading;

    private final HttpClient httpClient = new HttpClient();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public AdminAddCargoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_add_cargo, container, false);

        bindViews(view);
        setupListeners();

        // If a courier ID was passed, pre-fill the receiver ID
        if (getArguments() != null && getArguments().containsKey("COURIER_ID")) {
            long courierId = getArguments().getLong("COURIER_ID");
            etReceiverId.setText(String.valueOf(courierId));
            // Disable editing since we are assigning specifically to this courier
            etReceiverId.setEnabled(false);
            etReceiverId.setTextColor(view.getResources().getColor(android.R.color.darker_gray));
        }

        return view;
    }

    private void bindViews(View view) {
        etSenderId = view.findViewById(R.id.etSenderId);
        etReceiverId = view.findViewById(R.id.etReceiverId);
        etWeight = view.findViewById(R.id.etWeight);
        btnSaveCargo = view.findViewById(R.id.btnSaveCargo);
        tvError = view.findViewById(R.id.tvError);
        tvSaveText = view.findViewById(R.id.tvSaveText);
        pbLoading = view.findViewById(R.id.pbLoading);

        View btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setupListeners() {
        btnSaveCargo.setOnClickListener(v -> {
            if (validateForm()) {
                saveCargo();
            }
        });
    }

    private boolean validateForm() {
        if (TextUtils.isEmpty(etSenderId.getText().toString().trim())) {
            showError("Gönderici ID boş olamaz");
            return false;
        }
        if (TextUtils.isEmpty(etReceiverId.getText().toString().trim())) {
            showError("Alıcı ID boş olamaz");
            return false;
        }
        if (TextUtils.isEmpty(etWeight.getText().toString().trim())) {
            showError("Ağırlık boş olamaz");
            return false;
        }
        hideError();
        return true;
    }

    private void saveCargo() {
        setLoading(true);

        try {
            long senderId = Long.parseLong(etSenderId.getText().toString().trim());
            long receiverId = Long.parseLong(etReceiverId.getText().toString().trim());
            double weight = Double.parseDouble(etWeight.getText().toString().trim());

            CreateCargoRequest request = new CreateCargoRequest(senderId, receiverId, weight);

            executor.execute(() -> {
                String url = ApiConfig.CARGO_BASE_URL + ApiConfig.CARGOS_ENDPOINT;
                ApiResponse<Void> response = httpClient.post(url, request.toJson());

                mainHandler.post(() -> {
                    setLoading(false);
                    if (response.isSuccess()) {
                        Toast.makeText(getContext(), "Kargo başarıyla oluşturuldu!", Toast.LENGTH_SHORT).show();
                        if (getActivity() != null) {
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    } else {
                        showError(response.getMessage() != null ? response.getMessage() : "Kargo oluşturulamadı.");
                    }
                });
            });

        } catch (NumberFormatException e) {
            setLoading(false);
            showError("Geçerli sayılar giriniz");
        }
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
            tvSaveText.setVisibility(View.GONE);
            pbLoading.setVisibility(View.VISIBLE);
            btnSaveCargo.setEnabled(false);
        } else {
            tvSaveText.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
            btnSaveCargo.setEnabled(true);
        }
    }
}
