package com.group5.estoreapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.group5.estoreapp.R;
import com.group5.estoreapp.model.Product;
import com.group5.estoreapp.services.CartService;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPrice, tvDescription, tvSpecs;
    private ImageView imgDetail, imgAdd, backButton;
    private int userId;
    private Product product;
    private CartService cartService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // SharedPreferences để lấy userId
        SharedPreferences pref = getSharedPreferences("auth", MODE_PRIVATE);
        userId = pref.getInt("userId", -1);

        // Ánh xạ view
        imgDetail = findViewById(R.id.imgDetail);
        imgAdd = findViewById(R.id.imgAdd);
        tvName = findViewById(R.id.tvNameDetail);
        tvPrice = findViewById(R.id.tvPriceDetail);
        tvDescription = findViewById(R.id.tvDescription);
        tvSpecs = findViewById(R.id.tvSpecs);
        backButton = findViewById(R.id.backButton);

        // Nhận Product từ Intent
        product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            tvName.setText(product.getProductName());
            tvPrice.setText(String.format("%,.0f đ", product.getPrice()));
            Glide.with(this)
                    .load(product.getImageURL())
                    .into(imgDetail);
        }

        cartService = new CartService();

        imgAdd.setOnClickListener(v -> {
            if (userId != -1 && product != null) {
                cartService.addProductToCart(userId, product.getProductID(), 1, new CartService.AddToCartCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();

                        // Gọi lại badge từ CartFragment nếu đang ở MainActivity
                        if (MainActivity.instance != null) {
                            MainActivity.instance.updateCartBadge(userId);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(ProductDetailActivity.this, "Thêm vào giỏ hàng thất bại: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        });
        backButton.setOnClickListener(v -> finish());
    }
}
