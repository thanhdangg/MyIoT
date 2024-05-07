package com.example.myiot.view;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
    private int min_threshold = 0;
    private int max_threshold = 100;
    private FirebaseDatabase database;
    DatabaseReference motorRef;
    DatabaseReference sensorRef;
    DatabaseReference maxThresholdRef;
    DatabaseReference minThresholdRef;


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
        webSocketClient = new WebSocketClient(this);
        webSocketClient.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        sensorRef = database.getReference("/moisture/sensor");
        motorRef = database.getReference("/moisture/motor");
        maxThresholdRef = database.getReference("/moisture/max_threshold");
        minThresholdRef = database.getReference("/moisture/min_threshold");

        binding.seekBarMaxThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    maxThresholdRef.setValue(progress);
                }
                binding.tvMaxThreshold.setText(progress + "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.seekBarMinThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    minThresholdRef.setValue(progress);
                }
                binding.tvMinThreshold.setText(progress + "%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        minThresholdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    min_threshold = dataSnapshot.getValue(Integer.class);
                    binding.seekBarMinThreshold.setProgress(min_threshold);
                    binding.tvMinThreshold.setText(min_threshold + "%");
                    Log.d("Firebase", "Min threshold is: " + min_threshold);
                } else {
                    Log.d("Firebase", "No data at this location.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });

        maxThresholdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    max_threshold = dataSnapshot.getValue(Integer.class);
                    binding.seekBarMaxThreshold.setProgress(max_threshold);
                    binding.tvMaxThreshold.setText(max_threshold + "%");
                    Log.d("Firebase", "Max threshold is: " + max_threshold);
                } else {
                    Log.d("Firebase", "No data at this location.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Firebase", "Failed to read value.", error.toException());
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

                    // Kiểm tra giá trị cảm biến với ngưỡng và điều khiển motor
                    if (sensor <= min_threshold) {
                        motorRef.setValue(1); // Bật motor
                    } else if (sensor >= max_threshold) {
                        motorRef.setValue(0); // Tắt motor
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
        binding.switchMotor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Bật motor
                    motorRef.setValue(1);
                } else {
                    // Tắt motor
                    motorRef.setValue(0);
                }
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