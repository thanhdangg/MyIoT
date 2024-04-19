package com.example.myiot.viewmodel;

import com.example.myiot.model.ProgressListener;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketClient {
    private WebSocket webSocket;
    private OkHttpClient client;
    private ProgressListener progressListener;

    public WebSocketClient() {
        client = new OkHttpClient();
    }
    public WebSocketClient(ProgressListener progressListener) { // Modify this line
        client = new OkHttpClient();
        this.progressListener = progressListener; // Add this line
    }

    public void start() {
        Request request = new Request.Builder().url("ws://192.168.2.131:8989").build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                super.onOpen(webSocket, response);
                // Handle when the connection is established
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                int progress = Integer.parseInt(text);
                if (progressListener != null) { // Add this line
                    progressListener.onProgressUpdate(progress); // Add this line
                }
                // Handle when a message is received
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                // Handle when the connection is closed
            }
        });
    }

    public void stop() {
        if (webSocket != null) {
            webSocket.close(1000, "Goodbye");
        }
    }

}