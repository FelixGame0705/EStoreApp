package com.group5.estoreapp.api.userApi;

import com.google.gson.JsonObject;
import com.group5.estoreapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi {
    @GET("Users/{id}")
    Call<User> getUserById(@Path("id") int id);
    @PUT("Users/{id}")
    Call<JsonObject> updateUser(@Path("id") int id, @Body JsonObject userBody);

}
