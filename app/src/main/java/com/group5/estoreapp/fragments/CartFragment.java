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

import com.group5.estoreapp.R;
import com.group5.estoreapp.adapter.CartAdapter;
import com.group5.estoreapp.model.CartItem;
import com.group5.estoreapp.services.CartService;

import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private CartService cartService;
    private CartBadgeListener badgeListener;

    private int userId = 1; // có thể lấy từ SharedPreferences hoặc Session sau này

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartService = new CartService();
        loadCartItems();

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
