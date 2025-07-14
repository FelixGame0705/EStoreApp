package com.group5.estoreapp.activities;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.group5.estoreapp.R;

public class WebViewActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.webView);
        backButton = findViewById(R.id.backButton);

        String url = getIntent().getStringExtra("url");

        if (url != null) {
            webView.getSettings().setJavaScriptEnabled(true); // bắt buộc cho VNPay
            webView.setWebViewClient(new WebViewClient());    // để không mở trình duyệt ngoài
            webView.loadUrl(url);
        }
        backButton.setOnClickListener(v -> finish());
    }

}
