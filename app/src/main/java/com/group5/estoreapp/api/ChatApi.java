package com.group5.estoreapp.api;

import com.group5.estoreapp.model.ChatMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class ChatApi {
    private static final String BASE_URL = "https://prmbe.felixtien.dev/api/";
    private static ChatApi instance;

    public interface API {
        @POST("Chat/send")
        Call<Void> sendMessage(@Body ChatMessage message);

        @GET("Chat/history")
        Call<List<ChatMessage>> getChatHistory();
    }

    private final API api;

    private ChatApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);
    }

    public static synchronized ChatApi getInstance() {
        if (instance == null) instance = new ChatApi();
        return instance;
    }

    public Call<Void> sendMessage(ChatMessage message) {
        return api.sendMessage(message);
    }

    public Call<List<ChatMessage>> getChatHistory() {
        return api.getChatHistory();
    }
}
