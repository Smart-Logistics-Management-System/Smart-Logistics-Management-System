package com.example.lojistik;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link notifications#newInstance} factory method to
 * create an instance of this fragment.
 */
public class notifications extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public notifications() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment notifications.
     */
    // TODO: Rename and change types and number of parameters
    public static notifications newInstance(String param1, String param2) {
        notifications fragment = new notifications();
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
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        
        android.widget.LinearLayout llNotificationsList = view.findViewById(R.id.llNotificationsList);
        View btnBack = view.findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }

        if (getActivity() instanceof MainActivity && llNotificationsList != null) {
            MainActivity mainActivity = (MainActivity) getActivity();
            java.util.List<java.util.Map<String, String>> notifs = mainActivity.getNotificationsList();

            if (notifs.isEmpty()) {
                android.widget.TextView emptyText = new android.widget.TextView(getContext());
                emptyText.setText("Henüz bildiriminiz yok.");
                emptyText.setTextColor(android.graphics.Color.parseColor("#6b7280"));
                emptyText.setPadding(0, 32, 0, 32);
                llNotificationsList.addView(emptyText);
            } else {
                for (java.util.Map<String, String> notif : notifs) {
                    View card = inflater.inflate(R.layout.item_notification_card, llNotificationsList, false);
                    
                    android.widget.TextView tvTitle = card.findViewById(R.id.tvNotifTitle);
                    android.widget.TextView tvBody = card.findViewById(R.id.tvNotifBody);
                    android.widget.TextView tvTime = card.findViewById(R.id.tvNotifTime);
                    
                    if (tvTitle != null) tvTitle.setText(notif.get("title"));
                    if (tvBody != null) tvBody.setText(notif.get("body"));
                    if (tvTime != null) tvTime.setText(notif.get("time"));
                    
                    llNotificationsList.addView(card);
                }
            }
        }
        
        return view;
    }
}