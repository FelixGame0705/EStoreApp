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

    public interface AddToCartCallback {
        void onSuccess();
        void onError(Throwable t);
    }

    // Lấy giỏ hàng
    public void getCartsByUserId(int userId, CartItemsCallback callback) {
        CartApi.getInstance().getCartByUserId(userId).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Cart> carts = response.body();

                    // Tìm cart có status = "active"
                    for (Cart cart : carts) {
                        if ("active".equalsIgnoreCase(cart.getStatus())) {
                            callback.onCartItemsLoaded(cart.getCartItems());
                            return;
                        }
                    }

                    // Không có cart nào active
                    callback.onError(new Exception("Không có giỏ hàng đang hoạt động."));
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


    // ✅ Thêm sản phẩm vào giỏ hàng
    public void addProductToCart(int userId, int productId, int quantity, AddToCartCallback callback) {
        CartApi.getInstance().getCartByUserId(userId).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                    List<Cart> carts = response.body();
                    if (carts != null && !carts.isEmpty()) {
                        // Đã có cart → thêm CartItem
                        postCartItem(carts.get(0).getCartID(), userId, productId, quantity, callback);
                    } else {
                        // Chưa có cart → tạo mới
                        Cart newCart = new Cart();
                        newCart.setUserID(userId);
                        newCart.setStatus("Pending");
                        newCart.setTotalPrice(0);

                        CartApi.getInstance().createCart(newCart).enqueue(new Callback<Cart>() {
                            @Override
                            public void onResponse(Call<Cart> call, Response<Cart> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    int cartId = response.body().getCartID();
                                    postCartItem(cartId, userId, productId, quantity, callback);
                                } else {
                                    callback.onError(new Exception("Tạo cart thất bại"));
                                }
                            }

                            @Override
                            public void onFailure(Call<Cart> call, Throwable t) {
                                callback.onError(t);
                            }
                        });
                    }
                } else {
                    callback.onError(new Exception("Không thể kiểm tra cart."));
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // Gửi POST CartItem
    private void postCartItem(int cartId, int userId, int productId, int quantity, AddToCartCallback callback) {
        CartItem item = new CartItem();
        item.setCartID(cartId);
        item.setUserID(userId); // nếu server cần
        item.setProductID(productId);
        item.setQuantity(quantity);

        CartApi.getInstance().addCartItem(item).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(new Exception("Thêm sản phẩm thất bại."));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
