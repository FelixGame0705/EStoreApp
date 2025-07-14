package com.group5.estoreapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.group5.estoreapp.R;

public class PaymentResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);

        TextView resultText = findViewById(R.id.resultText);

        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (uri != null && "estoreapp".equals(uri.getScheme()) && "payment-return".equals(uri.getHost())) {
            String responseCode = uri.getQueryParameter("vnp_ResponseCode"); // 00 = success
            if ("00".equals(responseCode)) {
                resultText.setText("Thanh toán thành công");
            } else {
                resultText.setText("Thanh toán thất bại hoặc bị hủy");
            }
        } else {
            resultText.setText("Không nhận được dữ liệu thanh toán");
        }
    }
}