package com.group5.estoreapp.services;

import com.group5.estoreapp.api.ChatApi;
import com.group5.estoreapp.model.ChatMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class ChatService {
    private final ChatApi chatApi;

    public ChatService() {
        chatApi = ChatApi.getInstance();
    }

    public void sendMessage(ChatMessage message, Callback<Void> callback) {
        Call<Void> call = chatApi.sendMessage(message);
        call.enqueue(callback);
    }

    public void getChatHistory(Callback<List<ChatMessage>> callback) {
        Call<List<ChatMessage>> call = chatApi.getChatHistory();
        call.enqueue(callback);
    }
}
