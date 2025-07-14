package com.group5.estoreapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.estoreapp.R;
import com.group5.estoreapp.adapter.OrderHistoryAdapter;
import com.group5.estoreapp.model.Order;
import com.group5.estoreapp.services.OrderService;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private static final String TAG = "OrderHistoryActivity";

    private RecyclerView recyclerViewOrders;
    private TextView textEmptyState;
    private ProgressBar progressBar;
    private ImageView backButton;

    private OrderHistoryAdapter adapter;
    private List<Order> orderList;
    private OrderService orderService;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        initViews();
        initData();
        loadOrderHistory();
    }

    private void initViews() {
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
//        textEmptyState = findViewById(R.id.textEmptyState);
        progressBar = findViewById(R.id.progressBar);
        backButton = findViewById(R.id.backButton);

        // Setup RecyclerView
        orderList = new ArrayList<>();
        adapter = new OrderHistoryAdapter(this, orderList);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setAdapter(adapter);

        // Back button click listener
        backButton.setOnClickListener(v -> finish());
    }

    private void initData() {
        // Lấy userId từ SharedPreferences
        SharedPreferences pref = getSharedPreferences("auth", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);

        orderService = new OrderService();

        if (userId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void loadOrderHistory() {
        // ✅ Check if activity is still valid
        if (isFinishing() || isDestroyed()) {
            return;
        }

        showLoading(true);

        orderService.getOrdersByUserId(userId, new OrderService.OrderCallback() {
            @Override
            public void onOrdersLoaded(List<Order> orders) {
                // ✅ Check if activity is still valid before updating UI
                if (isFinishing() || isDestroyed()) {
                    return;
                }

                // ✅ Run on UI thread to ensure thread safety
                runOnUiThread(() -> {
                    try {
                        showLoading(false);

                        if (orders != null && !orders.isEmpty()) {
                            orderList.clear();
                            orderList.addAll(orders);

                            // ✅ Check if adapter is not null before notifying
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                            showEmptyState(false);

                            Log.d(TAG, "Loaded " + orders.size() + " orders");
                        } else {
                            showEmptyState(true);
                            Log.d(TAG, "No orders found");
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error updating UI", e);
                        showError("Lỗi khi hiển thị dữ liệu");
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                // ✅ Check if activity is still valid before updating UI
                if (isFinishing() || isDestroyed()) {
                    return;
                }

                // ✅ Run on UI thread to ensure thread safety
                runOnUiThread(() -> {
                    try {
                        showLoading(false);
                        showEmptyState(true);

                        String errorMessage = "Lỗi khi tải lịch sử đơn hàng: " + t.getMessage();
                        showError(errorMessage);
                        Log.e(TAG, "Error loading orders", t);
                    } catch (Exception e) {
                        Log.e(TAG, "Error handling error state", e);
                    }
                });
            }
        });
    }

    // ✅ Helper method to safely show error messages
    private void showError(String message) {
        try {
            if (!isFinishing() && !isDestroyed()) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error showing toast", e);
        }
    }

    // ✅ Updated showLoading method with safety checks
    private void showLoading(boolean show) {
        try {
            if (isFinishing() || isDestroyed()) {
                return;
            }

            if (show) {
                if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
                if (recyclerViewOrders != null) recyclerViewOrders.setVisibility(View.GONE);
                if (textEmptyState != null) textEmptyState.setVisibility(View.GONE);
            } else {
                if (progressBar != null) progressBar.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating loading state", e);
        }
    }

    // ✅ Updated showEmptyState method with safety checks
    private void showEmptyState(boolean show) {
        try {
            if (isFinishing() || isDestroyed()) {
                return;
            }

            if (show) {
                if (textEmptyState != null) textEmptyState.setVisibility(View.VISIBLE);
                if (recyclerViewOrders != null) recyclerViewOrders.setVisibility(View.GONE);
            } else {
                if (textEmptyState != null) textEmptyState.setVisibility(View.GONE);
                if (recyclerViewOrders != null) recyclerViewOrders.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error updating empty state", e);
        }
    }
}