package com.group5.estoreapp.api;

import com.group5.estoreapp.model.Cart;
import com.group5.estoreapp.model.CartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class CartApi {
    private static final String BASE_URL = "https://prmbe.felixtien.dev/api/";
    private static CartApi instance;

    // Interface Retrofit
    interface API {
        @GET("Carts/user/{id}")
        Call<List<Cart>> getCartByUserId(@Path("id") int userId);

        // ✅ POST Cart
        @POST("CartItems")
        Call<Void> addCartItem(@Body CartItem item);

        @POST("Carts")
        Call<Cart> createCart(@Body Cart cart);
        @POST("CartItems/add-or-update")
        Call<CartItem> addOrUpdate(
                @Query("cartId") int cartId,
                @Query("productId") int productId,
                @Query("quantity") int quantity
        );
        @DELETE("CartItems/{id}")
        Call<Void> deleteCartItem(@Path("id") int cartItemId);
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

    public Call<Cart> createCart(Cart cart) {
        return api.createCart(cart);
    }
    public Call<CartItem> addOrUpdate(int cartId, int productId, int quantity) {
        return api.addOrUpdate(cartId, productId, quantity);
    }

    public Call<Void> addCartItem(CartItem item) {
        return api.addCartItem(item);
    }
    public Call<Void> deleteCartItem(int cartItemId) {
        return api.deleteCartItem(cartItemId);
    }

}
