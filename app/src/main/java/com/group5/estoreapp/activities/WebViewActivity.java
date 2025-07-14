package com.group5.estoreapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.group5.estoreapp.R;
import com.group5.estoreapp.services.PaymentService;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private int orderId;
    private boolean alreadyChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        String paymentUrl = getIntent().getStringExtra("url");
        orderId = getIntent().getIntExtra("orderId", -1);

        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if (paymentUrl != null) {
            webView.loadUrl(paymentUrl);
        }

        // 👉 Xử lý nút quay lại
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (!alreadyChecked) {
                checkPaymentStatus(); // 🟢 Gọi check trước khi back
            } else {
                goBackToMain();
            }
        });
    }


    private void checkPaymentStatus() {
        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy orderId", Toast.LENGTH_SHORT).show();
            goBackToMain();
            return;
        }

        PaymentService paymentService = new PaymentService();
        paymentService.checkPaymentStatus(orderId, new PaymentService.PaymentStatusCallback() {
            @Override
            public void onResult(boolean isSuccess) {
                if (isSuccess) {
                    Toast.makeText(WebViewActivity.this, "✅ Thanh toán thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WebViewActivity.this, "❌ Thanh toán thất bại hoặc chưa hoàn tất!", Toast.LENGTH_SHORT).show();
                }
                goBackToMain();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(WebViewActivity.this, "⚠️ Lỗi kiểm tra thanh toán: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                goBackToMain();
            }
        });
    }


    private void goBackToMain() {
        Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
