package com.example.lojistik;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        android.widget.TextView tvName = view.findViewById(R.id.tvProfileName);
        android.widget.TextView tvRole = view.findViewById(R.id.tvProfileRole);
        View btnLogout = view.findViewById(R.id.btnLogout);
        View btnDeleteProfile = view.findViewById(R.id.btnDeleteProfile);

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            com.example.lojistik.model.UserData user = mainActivity.getCurrentUser();
            if (user != null) {
                if (tvName != null) tvName.setText(user.getFirstName() + " " + user.getLastName());
                if (tvRole != null) tvRole.setText(user.getRole() + " · " + user.getEmail());
            }

            if (btnLogout != null) {
                btnLogout.setOnClickListener(v -> mainActivity.logout());
            }

            if (btnDeleteProfile != null) {
                btnDeleteProfile.setOnClickListener(v -> {
                    if (user == null) return;
                    
                    new android.app.AlertDialog.Builder(getContext())
                        .setTitle("Hesabı Sil")
                        .setMessage("Hesabınızı silmek istediğinizden emin misiniz? Bu işlem geri alınamaz.")
                        .setPositiveButton("Evet, Sil", (dialog, which) -> deleteAccount(user.getId()))
                        .setNegativeButton("Vazgeç", null)
                        .show();
                });
            }
        }

        return view;
    }

    private void deleteAccount(long userId) {
        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
        android.os.Handler mainHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        com.example.lojistik.network.HttpClient httpClient = new com.example.lojistik.network.HttpClient();

        executor.execute(() -> {
            String url = com.example.lojistik.network.ApiConfig.BASE_URL + "/api/users/" + userId;
            com.example.lojistik.model.ApiResponse<Void> response = httpClient.delete(url);

            mainHandler.post(() -> {
                if (response.isSuccess()) {
                    android.widget.Toast.makeText(getContext(), "Hesabınız silindi.", android.widget.Toast.LENGTH_SHORT).show();
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).logout();
                    }
                } else {
                    android.widget.Toast.makeText(getContext(), "Hata: " + response.getMessage(), android.widget.Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}