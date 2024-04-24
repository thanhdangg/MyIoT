package com.example.myiot.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myiot.R;
import com.example.myiot.databinding.FragmentHomeBinding;
import com.example.myiot.model.ProgressListener;
import com.example.myiot.ui.CircleProgressBar;
import com.example.myiot.viewmodel.WebSocketClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment implements ProgressListener {
    private FragmentHomeBinding binding;
    private WebSocketClient webSocketClient;
    private CircleProgressBar circleProgressBar;

    public HomeFragment() {
        // Required empty public constructor
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomeBinding.inflate(getLayoutInflater());
        webSocketClient = new WebSocketClient();
        circleProgressBar = binding.circleProgressBar;
        webSocketClient = new WebSocketClient(this); // Modify this line
        webSocketClient.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("/moisture/sensor");

//        circleProgressBar.setProgress(30);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer value = dataSnapshot.getValue(Integer.class);
                    Log.d("Firebase", "Value is: " + value);
                    if (value != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                circleProgressBar.setProgress(value);
                                Log.d("progess bar", "value : "+ circleProgressBar.getProgress());
                            }
                        });
                    }
                } else {
                    Log.d("Firebase", "No data at this location.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webSocketClient.stop();
    }
    @Override
    public void onProgressUpdate(int progress) { // Add this method
        circleProgressBar.setProgress(progress);
    }
}