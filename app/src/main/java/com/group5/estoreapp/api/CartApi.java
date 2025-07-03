package com.group5.estoreapp.api;

import com.group5.estoreapp.model.Cart;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class CartApi {
    private static final String BASE_URL = "https://prmbe.felixtien.dev/api/";
    private static CartApi instance;

    // Interface Retrofit
    private interface API {
        @GET("Carts/user/{id}")
        Call<List<Cart>> getCartByUserId(@Path("id") int userId);
    }

    private final API api;

    private CartApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);
    }

    public static synchronized CartApi getInstance() {
        if (instance == null) instance = new CartApi();
        return instance;
    }

    public Call<List<Cart>> getCartByUserId(int userId) {
        return api.getCartByUserId(userId);
    }
}
