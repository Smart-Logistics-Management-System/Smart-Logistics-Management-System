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

    public list() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment list.
     */
    // TODO: Rename and change types and number of parameters
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
        
        View card1 = view.findViewById(R.id.cardPackage1);
        View card2 = view.findViewById(R.id.cardPackage2);
        View card3 = view.findViewById(R.id.cardPackage3);
        
        android.view.View.OnClickListener listener = v -> {
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                String trackingNumber = "";
                String status = "";
                
                if (v.getId() == R.id.cardPackage1) {
                    trackingNumber = "TRK9921";
                    status = "IN TRANSIT";
                } else if (v.getId() == R.id.cardPackage2) {
                    trackingNumber = "TRK8812";
                    status = "DELIVERED";
                } else if (v.getId() == R.id.cardPackage3) {
                    trackingNumber = "TRK7734";
                    status = "PENDING";
                }
                
                detail detailFragment = detail.newInstance(trackingNumber, status);
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        };
        
        if (card1 != null) card1.setOnClickListener(listener);
        if (card2 != null) card2.setOnClickListener(listener);
        if (card3 != null) card3.setOnClickListener(listener);
        
        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }
        
        return view;
    }
}