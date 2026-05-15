package com.example.lojistik;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link list#newInstance} factory method to
 * create an instance of this fragment.
 */
public class list extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private android.widget.LinearLayout llPackageList;
    private final com.example.lojistik.network.HttpClient httpClient = new com.example.lojistik.network.HttpClient();
    private final java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
    private final android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private com.example.lojistik.model.UserData currentUser;

    public list() {
        // Required empty public constructor
    }

    public static list newInstance(String param1, String param2) {
        list fragment = new list();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        
        llPackageList = view.findViewById(R.id.llPackageList);
        
        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }
        
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            currentUser = mainActivity.getCurrentUser();

            if (currentUser != null) {
                fetchCargos();
            }
        }
        
        return view;
    }
    
    private void fetchCargos() {
        if (llPackageList != null) llPackageList.removeAllViews();

        executor.execute(() -> {
            String url = com.example.lojistik.network.ApiConfig.CARGO_BASE_URL + com.example.lojistik.network.ApiConfig.CARGOS_ENDPOINT;
            com.example.lojistik.model.ApiResponse<String> response = httpClient.getWithResponse(url);

            mainHandler.post(() -> {
                if (!isAdded() || getContext() == null) {
                    return; 
                }
                
                if (response.isSuccess() && response.getData() != null) {
                    try {
                        java.util.List<com.example.lojistik.model.CargoData> myCargos = parseAndFilterCargos(response.getData());
                        updateListUI(myCargos);
                    } catch (Exception e) {
                        android.widget.Toast.makeText(getContext(), "Hata: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                    }
                } else {
                    android.widget.Toast.makeText(getContext(), "Bağlantı hatası: " + response.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private java.util.List<com.example.lojistik.model.CargoData> parseAndFilterCargos(String jsonString) throws Exception {
        java.util.List<com.example.lojistik.model.CargoData> cargos = new java.util.ArrayList<>();
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return cargos;
        }
        org.json.JSONArray jsonArray = new org.json.JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            org.json.JSONObject obj = jsonArray.getJSONObject(i);
            
            long id = obj.optLong("id", 0);
            String trackingNumber = obj.optString("trackingNumber", "TRK" + id);
            long senderId = obj.optLong("senderId", 0);
            long receiverId = obj.optLong("receiverId", 0);
            double weight = obj.optDouble("weight", 0.0);
            String status = obj.optString("status", "Bekliyor");

            if (currentUser != null && (receiverId == currentUser.getId() || senderId == currentUser.getId() || "ADMIN".equalsIgnoreCase(currentUser.getRole()))) {
                cargos.add(new com.example.lojistik.model.CargoData(id, trackingNumber, senderId, receiverId, weight, status));
            }
        }
        return cargos;
    }

    private void updateListUI(java.util.List<com.example.lojistik.model.CargoData> myCargos) {
        if (llPackageList == null) return;
        
        if (myCargos.isEmpty()) {
            android.widget.TextView emptyText = new android.widget.TextView(getContext());
            emptyText.setText("Hiç kargonuz bulunmamaktadır.");
            emptyText.setTextColor(android.graphics.Color.parseColor("#6b7280"));
            emptyText.setPadding(0, 32, 0, 32);
            llPackageList.addView(emptyText);
            return;
        }

        for (com.example.lojistik.model.CargoData cargo : myCargos) {
            View card = LayoutInflater.from(getContext()).inflate(R.layout.item_package_card, llPackageList, false);
            
            android.widget.TextView tvCardTracking = card.findViewById(R.id.tvCardTracking);
            android.widget.TextView tvCardStatus = card.findViewById(R.id.tvCardStatus);
            android.widget.TextView tvCardWeight = card.findViewById(R.id.tvCardWeight);
            
            if (tvCardTracking != null) tvCardTracking.setText(cargo.getTrackingNumber());
            if (tvCardStatus != null) tvCardStatus.setText(cargo.getStatus().toUpperCase());
            if (tvCardWeight != null) tvCardWeight.setText(cargo.getWeight() + "kg");
            
            card.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    detail detailFragment = detail.newInstance(cargo.getTrackingNumber(), cargo.getStatus());
                    mainActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            
            llPackageList.addView(card);
        }
    }
}