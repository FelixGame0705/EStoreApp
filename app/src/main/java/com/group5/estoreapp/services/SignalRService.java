package com.group5.estoreapp.services;

import android.util.Log;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

public class SignalRService {
    private HubConnection hubConnection;
    private static SignalRService instance;

    public static SignalRService getInstance() {
        if (instance == null) instance = new SignalRService();
        return instance;
    }

    public void connect(String baseUrl) {
        if (hubConnection == null || hubConnection.getConnectionState() != HubConnectionState.CONNECTED) {
            hubConnection = HubConnectionBuilder.create(baseUrl + "/chatHub").build();

            hubConnection.start()
                    .doOnComplete(() -> Log.d("SignalR", "Connected"))
                    .doOnError(error -> Log.e("SignalR", "Error: " + error.getMessage()))
                    .subscribe();
        }
    }

    public void joinGroup(String groupName, String username) {
        hubConnection.send("JoinGroup", groupName, username);
    }

    public void leaveGroup(String groupName) {
        hubConnection.send("LeaveGroup", groupName);
    }

    public void sendMessageToGroup(String groupName, String message, int userId) {
        hubConnection.send("SendMessageToGroup", groupName, message, userId);
    }

    public void setOnMessageReceived(MessageListener listener) {
        hubConnection.on("ReceiveMessage", (userId, message) -> {
            listener.onNewMessage(userId, message);
        }, Integer.class, String.class);
    }

    public interface MessageListener {
        void onNewMessage(int userId, String message);
    }
}
