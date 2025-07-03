package com.group5.estoreapp.services;

import com.group5.estoreapp.api.CartApi;
import com.group5.estoreapp.model.Cart;
import com.group5.estoreapp.model.CartItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartService {

    public interface CartCallback {
        void onCartsLoaded(List<Cart> carts);
        void onError(Throwable t);
    }

    public interface CartItemsCallback {
        void onCartItemsLoaded(List<CartItem> cartItems);
        void onError(Throwable t);
    }

    public void getCartsByUserId(int userId, CartItemsCallback callback) {
        CartApi.getInstance().getCartByUserId(userId).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<CartItem> items = response.body().get(0).getCartItems();
                    callback.onCartItemsLoaded(items);
                } else {
                    callback.onError(new Exception("Không có dữ liệu giỏ hàng."));
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}

