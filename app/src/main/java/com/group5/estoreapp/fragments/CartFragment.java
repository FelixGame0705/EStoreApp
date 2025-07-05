package com.group5.estoreapp.fragments;

import android.content.Context;
import android.os.Bundle;
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

import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private MaterialButton btnOrder;

    private CartAdapter cartAdapter;
    private CartService cartService;
    private OrderService orderService;
    private CartBadgeListener badgeListener;

    private int userId = 1;
    private int cartId = 0; // để lưu cart active

    public interface CartBadgeListener {
        void updateCartBadge();
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
                        "123 Nguyễn Văn A" // bạn có thể sửa chỗ này thành địa chỉ người dùng nhập
                );

                orderService.createOrder(orderRequest, new OrderService.CreateOrderCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        // TODO: clear giỏ hàng / chuyển màn hình nếu cần
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

    private void loadCartItems() {
        cartService.getCartsByUserId(userId, new CartService.CartItemsCallback() {
            @Override
            public void onCartItemsLoaded(List<CartItem> cartItems) {
                cartAdapter = new CartAdapter(getContext(), cartItems);
                recyclerView.setAdapter(cartAdapter);

                if (badgeListener != null) {
                    badgeListener.updateCartBadge(); // cập nhật badge khi dữ liệu giỏ hàng mới được load
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi tải giỏ hàng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

