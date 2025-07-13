package com.group5.estoreapp.api.userApi;

import com.group5.estoreapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {
    @GET("Users/{id}")
    Call<User> getUserById(@Path("id") int id);


}
