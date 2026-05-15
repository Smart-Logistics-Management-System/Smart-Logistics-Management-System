package com.example.lojistik;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link detail#newInstance} factory method to
 * create an instance of this fragment.
 */
public class detail extends Fragment {

    private static final String ARG_ID = "cargo_id";
    private static final String ARG_TRACKING = "tracking_number";
    private static final String ARG_STATUS = "status";

    private long cargoId;
    private String trackingNumber;
    private String status;

    private final com.example.lojistik.network.HttpClient httpClient = new com.example.lojistik.network.HttpClient();
    private final java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());

    public detail() {
        // Required empty public constructor
    }

    public static detail newInstance(long id, String trackingNumber, String status) {
        detail fragment = new detail();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putString(ARG_TRACKING, trackingNumber);
        args.putString(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cargoId = getArguments().getLong(ARG_ID);
            trackingNumber = getArguments().getString(ARG_TRACKING);
            status = getArguments().getString(ARG_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        
        android.widget.TextView tvPackageId = view.findViewById(R.id.tvPackageId);
        android.widget.TextView tvStatus = view.findViewById(R.id.tvStatus);
        View btnBack = view.findViewById(R.id.btnBack);
        View btnConfirm = view.findViewById(R.id.btnConfirmDelivery);
        View btnCancel = view.findViewById(R.id.btnCancelDelivery);
        
        if (tvPackageId != null) {
            tvPackageId.setText("Kargo #" + cargoId);
        }
        if (status != null && tvStatus != null) {
            tvStatus.setText(status);
        }
        
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> updateStatus("DELIVERED", "Teslim Edildi", "Kargo #" + cargoId + " başarıyla teslim edildi."));
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> updateStatus("CANCELLED", "İptal Edildi", "Kargo #" + cargoId + " iptal edildi."));
        }
        
        return view;
    }

    private void updateStatus(String newStatus, String title, String body) {
        executor.execute(() -> {
            String url = com.example.lojistik.network.ApiConfig.CARGO_BASE_URL + "/api/cargo/status-update";
            
            // Construct JSON body
            String jsonBody = "{\"trackingNumber\":\"" + trackingNumber + "\", \"status\":\"" + newStatus + "\"}";
            
            com.example.lojistik.model.ApiResponse<Void> response = httpClient.post(url, jsonBody);

            mainHandler.post(() -> {
                if (!isAdded()) return;

                if (response.isSuccess()) {
                    if (getActivity() instanceof MainActivity) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.addNotification(title, body);
                    }
                    android.widget.Toast.makeText(getContext(), "Durum güncellendi", android.widget.Toast.LENGTH_SHORT).show();
                    if (getActivity() != null) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                } else {
                    android.widget.Toast.makeText(getContext(), "Hata: " + response.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}