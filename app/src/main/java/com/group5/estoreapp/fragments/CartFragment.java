package com.group5.estoreapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.group5.estoreapp.R;
import com.group5.estoreapp.adapter.CartAdapter;
import com.group5.estoreapp.model.Cart;
import com.group5.estoreapp.model.CartItem;
import com.group5.estoreapp.model.OrderRequest;
import com.group5.estoreapp.services.CartService;
import com.group5.estoreapp.services.OrderService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private MaterialButton btnOrder;

    private CartAdapter cartAdapter;
    private CartService cartService;
    private OrderService orderService;
    private CartBadgeListener badgeListener;

    private int userId;
    private int cartId = 0;

    public interface CartBadgeListener {
        void updateCartBadge(int itemCount); // chỉ dùng hàm này

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CartBadgeListener) {
            badgeListener = (CartBadgeListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        btnOrder = view.findViewById(R.id.btn_custom);

        SharedPreferences pref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        userId = pref.getInt("userId", -1);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartService = new CartService();
        orderService = new OrderService();

        loadCartItems();

        btnOrder.setOnClickListener(v -> {
            if (cartId != 0) {
                OrderRequest orderRequest = new OrderRequest(
                        cartId,
                        userId,
                        "COD",
                        "123 Nguyễn Văn A"
                );

                orderService.createOrder(orderRequest, new OrderService.CreateOrderCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        loadCartItems(); // cập nhật lại sau khi đặt hàng
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getContext(), "Lỗi đặt hàng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Không tìm thấy giỏ hàng hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    public void onResume() {
        super.onResume();
        loadCartItems(); // Gọi lại khi fragment thực sự visible
    }

    private void loadCartItems() {
        cartService.getActiveCart(userId, new CartService.SingleCartCallback() {
            @Override
            public void onCartLoaded(Cart cart) {
                cartId = cart.getCartID();
                List<CartItem> cartItems = cart.getCartItems();

                cartAdapter = new CartAdapter(getContext(), cartItems);
                cartAdapter.setOnCartChangeListener(() -> {
                    if (badgeListener != null) {
                        // Tính lại số lượng sản phẩm duy nhất
                        int uniqueProductCount = 0;
                        if (cartItems != null) {
                            Set<Integer> productIds = new HashSet<>();
                            for (CartItem item : cartItems) {
                                productIds.add(item.getProductID());
                            }
                            uniqueProductCount = productIds.size();
                        }

                        badgeListener.updateCartBadge(uniqueProductCount);
                    }
                });
                recyclerView.setAdapter(cartAdapter);

                if (badgeListener != null) {
                    int uniqueProductCount;
                    if (cartItems != null) {
                        Set<Integer> productIds = new HashSet<>();
                        for (CartItem item : cartItems) {
                            productIds.add(item.getProductID());
                        }
                        uniqueProductCount = productIds.size();
                    } else {
                        uniqueProductCount = 0;
                    }

                    // ✅ Delay 1 giây (1000ms)
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        badgeListener.updateCartBadge(uniqueProductCount);
                    }, 500); // 1000ms = 1s
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Không có giỏ hàng active: " + t.getMessage(), Toast.LENGTH_LONG).show();
                if (badgeListener != null) {
                    badgeListener.updateCartBadge(0);
                }
            }
        });
    }
}
