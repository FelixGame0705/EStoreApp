package com.group5.estoreapp.services;

import android.util.Log;

import com.group5.estoreapp.api.CartApi;
import com.group5.estoreapp.model.Cart;
import com.group5.estoreapp.model.CartItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartService {



    public interface CartCallback {
        void onCartsLoaded(List<Cart> carts);
        void onError(Throwable t);
    }
    public interface ActiveCartsCallback {
        void onCartsLoaded(List<Cart> activeCarts);
        void onError(Throwable t);
    }
    public interface SingleCartCallback {
        void onCartLoaded(Cart cart);
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
                if (response.isSuccessful() && response.body() != null) {
                    List<Cart> carts = response.body();
                    Cart activeCart = null;
                    for (Cart cart : carts) {
                        if ("active".equalsIgnoreCase(cart.getStatus())) {
                            activeCart = cart;
                            break;
                        }
                    }

                    if (activeCart != null) {
                        postCartItem(activeCart.getCartID(), userId, productId, quantity, callback);
                    } else {
                        // ❗ Nếu chưa có cart active → tạo mới với status = "active"
                        Cart newCart = new Cart();
                        newCart.setUserID(userId);
                        newCart.setStatus("active");
                        newCart.setTotalPrice(0.0);

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
    public void getActiveCart(int userId, SingleCartCallback callback) {
        CartApi.getInstance().getCartByUserId(userId).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cart> carts = response.body();
                    // In ra toàn bộ JSON xem thử
                    Log.d("CartService", "Full carts JSON: " + carts);

                    for (Cart cart : carts) {
                        String status = cart.getStatus();
                        Log.d("CartService", "CartID=" + cart.getCartID() + " status=[" + status + "]");
                        if (status != null && status.trim().equalsIgnoreCase("active")) {
                            callback.onCartLoaded(cart);
                            return;
                        }
                    }

                    // Nếu loop hết mà không thấy cart active
                    callback.onError(new Exception("Không có giỏ hàng active"));
                } else {
                    callback.onError(new Exception("Không thể lấy danh sách giỏ hàng"));
                }
            }

            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    // ✅ Lấy danh sách CartItem từ cart có status = "active"
    public void getActiveCartItems(int userId, CartItemsCallback callback) {
        getActiveCart(userId, new SingleCartCallback() {
            @Override
            public void onCartLoaded(Cart cart) {
                List<CartItem> cartItems = cart.getCartItems();

                // ✅ Duy nhất theo productId
                Set<Integer> seenProductIds = new HashSet<>();
                for (CartItem item : cartItems) {
                    seenProductIds.add(item.getProductID());
                }

                // Truyền số lượng product duy nhất về
                List<CartItem> uniqueItems = new ArrayList<>();
                for (Integer productId : seenProductIds) {
                    for (CartItem item : cartItems) {
                        if (item.getProductID() == productId) {
                            uniqueItems.add(item);
                            break;
                        }
                    }
                }

                callback.onCartItemsLoaded(uniqueItems);
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }
    public void addOrUpdateCartItem(int cartId, int productId, int quantity, AddToCartCallback callback) {
        Call<CartItem> call =CartApi.getInstance().addOrUpdate(cartId, productId, quantity);
        call.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(new Exception("Thất bại: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
    public void deleteCartItem(int cartItemId, Callback<Void> callback) {
        CartApi.getInstance().deleteCartItem(cartItemId).enqueue(callback);
    }




}
