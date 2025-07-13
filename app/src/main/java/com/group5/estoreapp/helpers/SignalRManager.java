package com.group5.estoreapp.helpers;


import android.util.Log;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

public class SignalRManager {
    private HubConnection hubConnection;
    private static SignalRManager instance;

    public static SignalRManager getInstance() {
        if (instance == null) {
            instance = new SignalRManager();
        }
        return instance;
    }

    public void startConnection(String url, int userId, MessageListener listener) {
        hubConnection = HubConnectionBuilder.create(url).build();

        // Nhận tin nhắn từ server
        hubConnection.on("ReceiveMessage", (senderId, message) -> {
            listener.onNewMessage(senderId, message);
        }, Integer.class, String.class);

        hubConnection.on("SystemMessage", message -> {
            listener.onSystemMessage(message);
        }, String.class);

        hubConnection.start().doOnComplete(() -> {
            Log.d("SignalR", "Connected");
        }).doOnError(error -> {
            Log.e("SignalR", "Error: " + error.getMessage());
        }).subscribe();
    }

    public void joinGroup(String groupName, String username) {
        hubConnection.send("JoinGroup", groupName, username);
    }

    public void leaveGroup(String groupName) {
        hubConnection.send("LeaveGroup", groupName);
    }

    public void sendMessage(String groupName, String message, int userId) {
        hubConnection.send("SendMessageToGroup", groupName, message, userId);
    }

    public interface MessageListener {
        void onNewMessage(int senderId, String message);
        void onSystemMessage(String message);
    }
}

