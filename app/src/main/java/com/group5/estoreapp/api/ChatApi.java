package com.group5.estoreapp.api;

import com.group5.estoreapp.helpers.OkHttpProvider;
import com.group5.estoreapp.model.ApiResponse;
import com.group5.estoreapp.model.ChatHub;
import com.group5.estoreapp.model.ChatHubRequest;
import com.group5.estoreapp.model.ChatMessageRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ChatApi {
    private static final String BASE_URL = "https://prmbe.felixtien.dev/api/";

    public interface API {

        @POST("chat/hub")
        Call<ApiResponse<ChatHub>> createChatHub(@Query("secondUserId") int secondUserId);


        @GET("chat/hub/{id}")
        Call<ApiResponse<ChatHub>> getChatHubById(@Path("id") int id);

        @GET("chat/hubs/user/{id}")
        Call<ApiResponse<List<ChatHub>>> getChatHubsByUser(@Path("id") int userId);

        @POST("chat/message")
        Call<ApiResponse<Void>> sendMessage(@Body ChatMessageRequest request);
    }

    public final API api;

    private ChatApi(String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpProvider.getClientWithToken(token)) // 👈 đính kèm header
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);
    }

    public static ChatApi getInstance(String token) {
        return new ChatApi(token);
    }
}
