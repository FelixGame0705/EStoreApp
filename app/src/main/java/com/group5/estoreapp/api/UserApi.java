package com.group5.estoreapp.api;

import com.google.gson.JsonObject;
import com.group5.estoreapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class UserApi {

    private static final String BASE_URL = "https://prmbe.felixtien.dev/api/";
    private static UserApi instance;

    // Interface định nghĩa tất cả endpoint từ Swagger
    private interface API {
        @POST("Users/login")
        Call<JsonObject> login(@Body JsonObject body);

        @POST("Users")
        Call<JsonObject> register(@Body JsonObject body);

        @GET("Users")
        Call<List<User>> getAllUsers();

        @GET("Users/{id}")
        Call<User> getUserById(@Path("id") int id);

        @PUT("Users/{id}")
        Call<Void> updateUser(@Path("id") int id, @Body JsonObject body);

        @DELETE("Users/{id}")
        Call<Void> deleteUser(@Path("id") int id);

        @GET("Users/username/{username}")
        Call<User> getUserByUsername(@Path("username") String username);

        @POST("Users/validate")
        Call<JsonObject> validateUser(@Body JsonObject body);
    }

    private final API api;

    private UserApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(API.class);
    }

    public static synchronized UserApi getInstance() {
        if (instance == null) instance = new UserApi();
        return instance;
    }

    // Các phương thức public để sử dụng
    public Call<JsonObject> login(JsonObject body) {
        return api.login(body);
    }

    public Call<JsonObject> register(JsonObject body) {
        return api.register(body);
    }

    public Call<List<User>> getAllUsers() {
        return api.getAllUsers();
    }

    public Call<User> getUserById(int id) {
        return api.getUserById(id);
    }

    public Call<Void> updateUser(int id, JsonObject body) {
        return api.updateUser(id, body);
    }

    public Call<Void> deleteUser(int id) {
        return api.deleteUser(id);
    }

    public Call<User> getUserByUsername(String username) {
        return api.getUserByUsername(username);
    }

    public Call<JsonObject> validateUser(JsonObject body) {
        return api.validateUser(body);
    }
}
