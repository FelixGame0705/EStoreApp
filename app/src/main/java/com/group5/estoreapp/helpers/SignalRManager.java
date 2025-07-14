package com.group5.estoreapp.helpers;

import android.util.Log;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

public class SignalRManager {

    private HubConnection hubConnection;
    private static SignalRManager instance;

    public interface MessageListener {
        void onNewMessage(int senderId, String message);
        void onSystemMessage(String message);
    }

    public interface ConnectionListener {
        void onConnected();
        void onError(Throwable t);
    }

    public static SignalRManager getInstance() {
        if (instance == null) {
            instance = new SignalRManager();
        }
        return instance;
    }

    public void startConnection(String url, int userId, MessageListener listener, ConnectionListener connListener) {
        hubConnection = HubConnectionBuilder.create(url).build();

        hubConnection.on("ReceiveMessage", (senderId, message) -> {
            listener.onNewMessage(senderId, message);
        }, Integer.class, String.class);

        hubConnection.on("SystemMessage", message -> {
            listener.onSystemMessage(message);
        }, String.class);

        hubConnection.start().doOnComplete(() -> {
            Log.d("SignalR", "Connected to Hub");
            connListener.onConnected();
        }).doOnError(error -> {
            Log.e("SignalR", "Connection error: " + error.getMessage());
            connListener.onError(error);
        }).subscribe();
    }

    public void joinGroup(String groupName, String username) {
        if (hubConnection != null) {
            hubConnection.send("JoinGroup", groupName, username);
        }
    }

    public void sendMessage(String groupName, String message, int userId) {
        if (hubConnection != null) {
            hubConnection.send("SendMessageToGroup", groupName, message, userId);
        }
    }
}
