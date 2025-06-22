package com.group5.estoreapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.group5.estoreapp.api.ProductApiService;
import com.group5.estoreapp.model.Product;
import java.text.NumberFormat;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    private static final String TAG = "ProductDetailActivity";
    public static final String EXTRA_PRODUCT_ID = "product_id";

    private ImageView imageProduct;
    private TextView textTitle, textPrice, textDescription, textCategory;
    private MaterialButton buttonAddToCart;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get product ID from intent
        productId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
        if (productId == -1) {
            Toast.makeText(this, "Invalid product", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        loadProductDetail();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        imageProduct = findViewById(R.id.imageProduct);
        textTitle = findViewById(R.id.textTitle);
        textPrice = findViewById(R.id.textPrice);
        textDescription = findViewById(R.id.textDescription);
        textCategory = findViewById(R.id.textCategory);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);
        progressBar = findViewById(R.id.progressBar);

        buttonAddToCart.setOnClickListener(v -> addToCart());
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Product Detail");
        }
    }

    private void loadProductDetail() {
        showLoading(true);

        ProductApiService.getInstance().getProductById(productId)
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        showLoading(false);

                        if (response.isSuccessful() && response.body() != null) {
                            displayProduct(response.body());
                        } else {
                            showError("Failed to load product details");
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        showLoading(false);
                        showError("Network error: " + t.getMessage());
                        Log.e(TAG, "Error loading product", t);
                    }
                });
    }

    private void displayProduct(Product product) {
        textTitle.setText(product.getTitle());
        textDescription.setText(product.getDescription());
        textCategory.setText(product.getCategory().toUpperCase());

        // Format and display price
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);
        textPrice.setText(formatter.format(product.getPrice()));

        // Load product image
        Glide.with(this)
                .load(product.getImage())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(imageProduct);

        // Update toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(product.getTitle());
        }
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        // Hide/show content views when loading
        findViewById(R.id.contentLayout).setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void addToCart() {
        // TODO: Implement add to cart functionality
        Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}