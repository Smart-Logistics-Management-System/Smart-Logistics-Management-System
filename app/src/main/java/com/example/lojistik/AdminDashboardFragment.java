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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lojistik.model.ApiResponse;
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
 * Admin Dashboard - Shows a list of couriers and allows assigning tasks.
 */
public class AdminDashboardFragment extends Fragment {

    private LinearLayout llCouriers;
    private ProgressBar pbLoading;
    private TextView tvEmptyState;
    private FrameLayout btnLogout;

    private final HttpClient httpClient = new HttpClient();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public AdminDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);

        bindViews(view);
        setupListeners();
        fetchCouriers();

        return view;
    }

    private void bindViews(View view) {
        llCouriers = view.findViewById(R.id.llCouriers);
        pbLoading = view.findViewById(R.id.pbLoading);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    private void setupListeners() {
        btnLogout.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).logout();
            }
        });
    }

    private void fetchCouriers() {
        pbLoading.setVisibility(View.VISIBLE);
        llCouriers.removeAllViews();
        tvEmptyState.setVisibility(View.GONE);

        executor.execute(() -> {
            String url = ApiConfig.BASE_URL + ApiConfig.USERS_ENDPOINT;
            ApiResponse<String> response = httpClient.getWithResponse(url);

            mainHandler.post(() -> {
                pbLoading.setVisibility(View.GONE);

                if (response.isSuccess() && response.getData() != null) {
                    try {
                        List<UserData> couriers = parseCouriers(response.getData());
                        if (couriers.isEmpty()) {
                            tvEmptyState.setVisibility(View.VISIBLE);
                        } else {
                            for (UserData courier : couriers) {
                                addCourierCard(courier);
                            }
                        }
                    } catch (Exception e) {
                        tvEmptyState.setText("Veriler okunurken hata oluştu.");
                        tvEmptyState.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvEmptyState.setText("Bağlantı hatası: " + response.getMessage());
                    tvEmptyState.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    private List<UserData> parseCouriers(String jsonString) throws Exception {
        List<UserData> couriers = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String role = obj.optString("role", "");

            if ("USER".equalsIgnoreCase(role)) {
                UserData user = new UserData(
                        obj.getLong("id"),
                        obj.getString("firstName"),
                        obj.getString("lastName"),
                        obj.getString("email"),
                        role
                );
                couriers.add(user);
            }
        }
        return couriers;
    }

    private void addCourierCard(UserData courier) {
        // Container
        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setBackgroundResource(R.drawable.bg_package_card);
        card.setGravity(Gravity.CENTER_VERTICAL);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.spacing_m));
        card.setLayoutParams(params);
        int padding = getResources().getDimensionPixelSize(R.dimen.spacing_m);
        card.setPadding(padding, padding, padding, padding);

        // Icon Box
        FrameLayout iconBox = new FrameLayout(getContext());
        iconBox.setBackgroundResource(R.drawable.bg_icon_box_blue);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(120, 120);
        iconParams.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.spacing_m), 0);
        iconBox.setLayoutParams(iconParams);

        ImageView icon = new ImageView(getContext());
        icon.setImageResource(android.R.drawable.ic_menu_directions);
        icon.setColorFilter(Color.parseColor("#1d4ed8")); // blue_600
        FrameLayout.LayoutParams imgParams = new FrameLayout.LayoutParams(60, 60, Gravity.CENTER);
        iconBox.addView(icon, imgParams);
        card.addView(iconBox);

        // Info Text
        LinearLayout infoLayout = new LinearLayout(getContext());
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        infoLayout.setLayoutParams(infoParams);

        TextView tvName = new TextView(getContext());
        tvName.setText(courier.getFullName());
        tvName.setTextColor(Color.parseColor("#1f2937")); // gray_800
        tvName.setTextSize(16);
        tvName.setTypeface(null, android.graphics.Typeface.BOLD);
        infoLayout.addView(tvName);

        TextView tvEmail = new TextView(getContext());
        tvEmail.setText(courier.getEmail());
        tvEmail.setTextColor(Color.parseColor("#6b7280")); // gray_500
        tvEmail.setTextSize(12);
        infoLayout.addView(tvEmail);

        card.addView(infoLayout);

        // Assign Task Button (Right side)
        TextView btnAssign = new TextView(getContext());
        btnAssign.setText("Görev Ata");
        btnAssign.setTextColor(Color.parseColor("#ffffff"));
        btnAssign.setBackgroundResource(R.drawable.bg_button_blue);
        btnAssign.setPadding(32, 16, 32, 16);
        btnAssign.setTextSize(12);
        btnAssign.setTypeface(null, android.graphics.Typeface.BOLD);
        
        btnAssign.setOnClickListener(v -> {
            AdminAddCargoFragment addCargoFragment = new AdminAddCargoFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("COURIER_ID", courier.getId());
            addCargoFragment.setArguments(bundle);

            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, addCargoFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        card.addView(btnAssign);

        llCouriers.addView(card);
    }
}
