package com.group5.estoreapp.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.group5.estoreapp.api.ChatApi;
import com.group5.estoreapp.model.ApiResponse;
import com.group5.estoreapp.model.ChatHub;
import com.group5.estoreapp.model.ChatMessageRequest;

import java.util.List;

import retrofit2.Callback;

public class ChatService {

    private final ChatApi.API api;

    public ChatService(Context context) {
        SharedPreferences pref = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = pref.getString("accessToken", null);
        this.api = ChatApi.getInstance(token).api;
    }

    public void createChatHub(int secondUserId, Callback<ApiResponse<ChatHub>> callback) {
        api.createChatHub(secondUserId).enqueue(callback);
    }

    public void getChatHubsByUser(int userId, Callback<ApiResponse<List<ChatHub>>> callback) {
        api.getChatHubsByUser(userId).enqueue(callback);
    }

    public void getChatHubById(int chatHubId, Callback<ApiResponse<ChatHub>> callback) {
        api.getChatHubById(chatHubId).enqueue(callback);
    }

    public void sendMessage(int chatHubId, int senderId, String message, Callback<ApiResponse<Void>> callback) {
        ChatMessageRequest request = new ChatMessageRequest(chatHubId, senderId, message);
        api.sendMessage(request).enqueue(callback);
    }
}
