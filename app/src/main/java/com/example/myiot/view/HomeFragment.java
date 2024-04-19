package com.example.myiot.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myiot.R;
import com.example.myiot.databinding.FragmentHomeBinding;
import com.example.myiot.model.ProgressListener;
import com.example.myiot.ui.CircleProgressBar;
import com.example.myiot.viewmodel.WebSocketClient;


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