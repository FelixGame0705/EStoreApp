package com.group5.estoreapp.api;

import android.content.Context;

import com.group5.estoreapp.helpers.AuthInterceptor;
import com.group5.estoreapp.model.ApiResponse;
import com.group5.estoreapp.model.ChatHub;
import com.group5.estoreapp.model.ChatHubRequest;
import com.group5.estoreapp.model.ChatMessageRequest;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
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
    private static ChatApi instance;

    public interface API {

        @POST("chat/hub")
        Call<ApiResponse<ChatHub>> createChatHub(@Query("secondUserId") int secondUserId);


        @GET("chat/hub/{id}")
        Call<ChatHub> getChatHubById(@Path("id") String chatHubId);

        @GET("chat/hubs/user/{userId}")
        Call<List<ChatHub>> getChatHubsByUser(@Path("userId") int userId);

        @POST("chat/message")
        Call<ResponseBody> sendMessage(@Body ChatMessageRequest request);


    }

    public final API api;

    private ChatApi(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context)) // ✅ dùng interceptor
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) // 👈 sửa lại URL của bạn
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        api = retrofit.create(API.class);
    }

    public static ChatApi getInstance(Context context) {
        if (instance == null) {
            instance = new ChatApi(context);
        }
        return instance;
    }
}
