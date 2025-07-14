package com.group5.estoreapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.estoreapp.R;
import com.group5.estoreapp.adapter.OrderCartAdapter;
import com.group5.estoreapp.model.CartItem;
import com.group5.estoreapp.model.OrderRequest;
import com.group5.estoreapp.model.PaymentRequest;
import com.group5.estoreapp.services.OrderService;
import com.group5.estoreapp.services.PaymentService;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CartItem> cartItems;
    private Button btnSubmit;
    private ImageView backButton;

    private OrderService orderService;
    private int userId; // lấy từ SharedPreferences
    private int cartId; // truyền từ CartFragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        recyclerView = findViewById(R.id.recyclerViewOrderItems);
        btnSubmit = findViewById(R.id.btnSubmitBilling);
        backButton = findViewById(R.id.backButton);

        cartItems = (ArrayList<CartItem>) getIntent().getSerializableExtra("cartItems");
        cartId = getIntent().getIntExtra("cartId", 0);

        SharedPreferences pref = getSharedPreferences("auth", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);

        orderService = new OrderService();

        if (cartItems != null && !cartItems.isEmpty()) {
            OrderCartAdapter adapter = new OrderCartAdapter(this, cartItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }

        btnSubmit.setOnClickListener(v -> handleSubmitOrder());
        backButton.setOnClickListener(v -> finish());
    }

    private void handleSubmitOrder() {
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm trong giỏ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cartId == 0 || userId == -1) {
            Toast.makeText(this, "Thiếu thông tin giỏ hàng hoặc người dùng", Toast.LENGTH_LONG).show();
            return;
        }

        OrderRequest orderRequest = new OrderRequest(
                cartId,
                userId,
                "VNPay",        // phương thức thanh toán
                "123 ABC"       // địa chỉ (sau này nên để người dùng nhập)
        );

        orderService.createOrder(orderRequest, new OrderService.CreateOrderCallback() {
            @Override
            public void onSuccess(int orderId) {
                Toast.makeText(OrderDetailActivity.this, "Đơn hàng đã tạo! ID: " + orderId, Toast.LENGTH_SHORT).show();

                // ✅ Sau khi có orderId thì gọi thanh toán
                createPayment(orderId);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(OrderDetailActivity.this, "Lỗi tạo đơn hàng: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void createPayment(int orderId) {
        PaymentRequest paymentRequest = new PaymentRequest(
                orderId,
                "VNPay",
                "estoreapp://payment-return",  // returnUrl
                "estoreapp://payment-return",  // cancelUrl
                "VN"
        );

        new Handler().postDelayed(() -> {
            PaymentService paymentService = new PaymentService();
            paymentService.createPayment(paymentRequest, new PaymentService.PaymentCallback() {
                @Override
                public void onSuccess(String paymentUrl) {
                    Intent intent = new Intent(OrderDetailActivity.this, WebViewActivity.class);
                    intent.putExtra("url", paymentUrl);
                    startActivity(intent);
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(OrderDetailActivity.this, "Lỗi tạo link thanh toán: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }, 1000); // Delay 1 giây để chắc chắn order đã commit xong server
    }
}
