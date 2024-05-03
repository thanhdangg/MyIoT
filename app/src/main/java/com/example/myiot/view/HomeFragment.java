package com.example.myiot.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

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
    private int sensor = 0;
    private int motor = 0;
    private int threshold = 0;
    private FirebaseDatabase database;
    DatabaseReference motorRef;
    DatabaseReference thresholdRef;
    DatabaseReference sensorRef;


    public HomeFragment() {
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
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        thresholdRef = database.getReference("/moisture/threshold");
        sensorRef = database.getReference("/moisture/sensor");
        motorRef = database.getReference("/moisture/motor");

        binding.seekBarThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                thresholdRef.setValue(progress);
                if (sensor < progress) {
                    motorRef.setValue(1);
                    binding.switchMotor.setChecked(true);
                } else {
                    motorRef.setValue(0);
                    binding.switchMotor.setChecked(false);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        motorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer value = dataSnapshot.getValue(Integer.class);
                    if (value == 1) {
                        binding.switchMotor.setChecked(true);
                    } else {
                        binding.switchMotor.setChecked(false);
                    }
                    Log.d("Firebase", "Motor value is: " + value);
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

        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    sensor = dataSnapshot.getValue(Integer.class);
                    Log.d("Firebase", "Value is: " + sensor);
                    if (sensor != 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.circleProgressBar.setProgress(sensor);
                            }
                        });
                    }
                    if (sensor < threshold) {
                        motorRef.setValue(1);
                    } else {
                        motorRef.setValue(0);
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

        thresholdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    threshold = dataSnapshot.getValue(Integer.class);
                    Log.d("Firebase", "Threshold value is: " + threshold);
                    binding.tvThreshold.setText(threshold + "%");
                    if (sensor < threshold) {
                        motorRef.setValue(1);
                    } else {
                        motorRef.setValue(0);
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