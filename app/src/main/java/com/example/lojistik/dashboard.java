package com.example.lojistik;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lojistik.model.ApiResponse;
import com.example.lojistik.model.CargoData;
import com.example.lojistik.model.UserData;
import com.example.lojistik.network.ApiConfig;
import com.example.lojistik.network.HttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dashboard extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayout llActivePackages;
    private TextView tvTotalPackages;
    private TextView tvDeliveredPackages;

    private final HttpClient httpClient = new HttpClient();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private UserData currentUser;

    public dashboard() {
        // Required empty public constructor
    }

    public static dashboard newInstance(String param1, String param2) {
        dashboard fragment = new dashboard();
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        llActivePackages = view.findViewById(R.id.llActivePackages);
        tvTotalPackages = view.findViewById(R.id.tvTotalPackages);
        tvDeliveredPackages = view.findViewById(R.id.tvDeliveredPackages);

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            currentUser = mainActivity.getCurrentUser();

            if (currentUser != null) {
                // Update greeting name
                TextView tvGreeting = view.findViewById(R.id.tvGreeting);
                if (tvGreeting != null) {
                    tvGreeting.setText(currentUser.getFirstName() + "!");
                }

                // Show Admin Add Cargo Button if role is ADMIN
                View fabAddCargo = view.findViewById(R.id.fabAddCargo);
                if (fabAddCargo != null) {
                    if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                        fabAddCargo.setVisibility(View.VISIBLE);
                        fabAddCargo.setOnClickListener(v -> {
                            // Navigate to Add Cargo screen
                            mainActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragmentContainer, new AdminAddCargoFragment())
                                    .addToBackStack(null)
                                    .commit();
                        });
                    } else {
                        fabAddCargo.setVisibility(View.GONE);
                    }
                }

                // Fetch Cargos for the current user
                fetchCargos();
            }
        }

        return view;
    }

    private void fetchCargos() {
        llActivePackages.removeAllViews();

        executor.execute(() -> {
            String url = ApiConfig.CARGO_BASE_URL + ApiConfig.CARGOS_ENDPOINT;
            ApiResponse<String> response = httpClient.getWithResponse(url);

            mainHandler.post(() -> {
                if (!isAdded() || getContext() == null) {
                    return; // Fragment is not attached to context anymore
                }
                
                if (response.isSuccess() && response.getData() != null) {
                    try {
                        List<CargoData> myCargos = parseAndFilterCargos(response.getData());
                        updateDashboardUI(myCargos);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Hata: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Bağlantı hatası: " + response.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private List<CargoData> parseAndFilterCargos(String jsonString) throws Exception {
        List<CargoData> cargos = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            
            // Assuming API returns senderId, receiverId, weight, id
            long id = obj.optLong("id", 0);
            String trackingNumber = obj.optString("trackingNumber", "TRK" + id);
            long senderId = obj.optLong("senderId", 0);
            long receiverId = obj.optLong("receiverId", 0);
            double weight = obj.optDouble("weight", 0.0);
            String status = obj.optString("status", "Bekliyor");

            // Filter for only cargos assigned to current user
            if (currentUser != null && (receiverId == currentUser.getId() || senderId == currentUser.getId() || "ADMIN".equalsIgnoreCase(currentUser.getRole()))) {
                cargos.add(new CargoData(id, trackingNumber, senderId, receiverId, weight, status));
            }
        }
        return cargos;
    }

    private void updateDashboardUI(List<CargoData> myCargos) {
        if (tvTotalPackages != null) {
            tvTotalPackages.setText(String.valueOf(myCargos.size()));
        }

        // Dummy delivered count for now
        int deliveredCount = 0;
        for (CargoData cargo : myCargos) {
            if ("Teslim Edildi".equalsIgnoreCase(cargo.getStatus())) {
                deliveredCount++;
            }
        }
        if (tvDeliveredPackages != null) {
            tvDeliveredPackages.setText(String.valueOf(deliveredCount));
        }

        if (myCargos.isEmpty()) {
            TextView emptyText = new TextView(getContext());
            emptyText.setText("Hiç kargonuz bulunmamaktadır.");
            emptyText.setTextColor(Color.parseColor("#6b7280"));
            emptyText.setPadding(0, 32, 0, 32);
            llActivePackages.addView(emptyText);
            return;
        }

        for (CargoData cargo : myCargos) {
            addCargoCard(cargo);
        }
    }

    private void addCargoCard(CargoData cargo) {
        if (getContext() == null) return;

        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setBackgroundResource(R.drawable.bg_package_card);
        card.setGravity(Gravity.CENTER_VERTICAL);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.spacing_m), 0, 0);
        card.setLayoutParams(params);
        int padding = getResources().getDimensionPixelSize(R.dimen.spacing_m);
        card.setPadding(padding, padding, padding, padding);
        card.setClickable(true);
        card.setFocusable(true);

        // Icon Box
        FrameLayout iconBox = new FrameLayout(getContext());
        iconBox.setBackgroundResource(R.drawable.bg_icon_box_blue);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(120, 120);
        iconParams.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.spacing_m), 0);
        iconBox.setLayoutParams(iconParams);

        ImageView icon = new ImageView(getContext());
        icon.setImageResource(android.R.drawable.ic_menu_send);
        icon.setColorFilter(Color.parseColor("#1d4ed8"));
        FrameLayout.LayoutParams imgParams = new FrameLayout.LayoutParams(60, 60, Gravity.CENTER);
        iconBox.addView(icon, imgParams);
        card.addView(iconBox);

        // Info Text
        LinearLayout infoLayout = new LinearLayout(getContext());
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        infoLayout.setLayoutParams(infoParams);

        TextView tvTitle = new TextView(getContext());
        tvTitle.setText("Kargo #" + cargo.getId());
        tvTitle.setTextColor(Color.parseColor("#1f2937"));
        tvTitle.setTextSize(16);
        tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
        infoLayout.addView(tvTitle);

        TextView tvSubtitle = new TextView(getContext());
        tvSubtitle.setText("Ağırlık: " + cargo.getWeight() + " kg · " + cargo.getStatus());
        tvSubtitle.setTextColor(Color.parseColor("#6b7280"));
        tvSubtitle.setTextSize(12);
        infoLayout.addView(tvSubtitle);

        card.addView(infoLayout);

        // Play Icon
        ImageView rightIcon = new ImageView(getContext());
        rightIcon.setImageResource(android.R.drawable.ic_media_play);
        rightIcon.setColorFilter(Color.parseColor("#d1d5db"));
        LinearLayout.LayoutParams rightIconParams = new LinearLayout.LayoutParams(48, 48);
        card.addView(rightIcon, rightIconParams);

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

        llActivePackages.addView(card);
    }
}