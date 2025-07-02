package com.group5.estoreapp.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.group5.estoreapp.R;
import com.group5.estoreapp.model.Product;
import com.group5.estoreapp.services.ProductService;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView tvName, tvPrice, tvDescription, tvSpecs;
    private ProductService productService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imgProduct = findViewById(R.id.imgDetail);
        tvName = findViewById(R.id.tvNameDetail);
        tvPrice = findViewById(R.id.tvPriceDetail);
        tvDescription = findViewById(R.id.tvDescription);
        tvSpecs = findViewById(R.id.tvSpecs);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        productService = new ProductService();

        int productId = getIntent().getIntExtra("productId", -1);
        if (productId != -1) {
            loadProductById(productId);
        } else {
            Toast.makeText(this, "Không tìm thấy ID sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadProductById(int productId) {
        productService.getProductById(productId, new ProductService.SingleProductCallback() {
            @Override
            public void onSuccess(Product product) {
                tvName.setText(product.getProductName());
                tvPrice.setText(product.getPrice() + "₫");
                tvDescription.setText(product.getFullDescription());
                tvSpecs.setText(product.getTechnicalSpecifications());
                Glide.with(ProductDetailActivity.this).load(product.getImageURL()).into(imgProduct);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi khi lấy sản phẩm: " + t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}

