package com.group5.estoreapp.services;

import android.content.Context;

import com.group5.estoreapp.api.ChatApi;
import com.group5.estoreapp.model.ApiResponse;
import com.group5.estoreapp.model.ChatHub;
import com.group5.estoreapp.model.ChatMessageRequest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public class ChatService {

    private final ChatApi.API api;

    public ChatService(Context context) {
        this.api = ChatApi.getInstance(context).api; // ✅ dùng context
    }

    public void createChatHub(int secondUserId, Callback<ApiResponse<ChatHub>> callback) {
        api.createChatHub(secondUserId).enqueue(callback);
    }

    public void getChatHubsByUser(int userId, Callback<List<ChatHub>> callback) {
        api.getChatHubsByUser(userId).enqueue(callback);
    }

    public void getChatHubById(String chatHubId, Callback<ChatHub> callback) {
        api.getChatHubById(chatHubId).enqueue(callback);
    }

    public void sendMessage(String chatHubId, String content, int type, Callback<ResponseBody> callback) {
        ChatMessageRequest request = new ChatMessageRequest(chatHubId, content, type);
        api.sendMessage(request).enqueue(callback);
    }
}
