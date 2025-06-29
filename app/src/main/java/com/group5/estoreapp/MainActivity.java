package com.group5.estoreapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group5.estoreapp.adapter.ProductAdapter;
import com.group5.estoreapp.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbarhome;
    private ViewFlipper viewFlipper;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabChat;
    private SearchView searchView;
    private List<Product> productList;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupToolbar();
        setupViewFlipper();
        loadProducts();
        setupBottomNavigation();
    }

    private void initViews() {
        toolbarhome = findViewById(R.id.toolbarhome);
        viewFlipper = findViewById(R.id.viewFlipper);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fabChat = findViewById(R.id.fab_chat);
        searchView = findViewById(R.id.searchView);

        // Set up FAB click listener
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                // TODO: Thêm chức năng Chat ở đây
                bottomNavigationView.setSelectedItemId(R.id.nav_chat); // Highlight Chat
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbarhome);
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // Handle search submission (e.g., filter products)
                    // TODO: Implement search logic
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Handle search text changes (e.g., real-time filtering)
                    // TODO: Implement search logic
                    return false;
                }
            });
        }
    }

    private void setupViewFlipper() {
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        productList.add(new Product("Banana", 4.99, R.drawable.ic_launcher_foreground));
        productList.add(new Product("Apple", 5.99, R.drawable.ic_launcher_foreground));
        productList.add(new Product("Orange", 3.99, R.drawable.ic_launcher_foreground));
        productList.add(new Product("Grapes", 6.49, R.drawable.ic_launcher_foreground));

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    recyclerView.setVisibility(View.VISIBLE);
                    return true;
                }  else if (itemId == R.id.nav_chat) {
                    recyclerView.setVisibility(View.GONE);
                    // TODO: Thêm chức năng Chat ở đây
                    return true;
                } else if (itemId == R.id.nav_cart) {
                    recyclerView.setVisibility(View.GONE);
                    // TODO: Thêm chức năng Giỏ hàng ở đây
                    return true;
                } else if (itemId == R.id.nav_notifications) {
                    recyclerView.setVisibility(View.GONE);
                    // TODO: Thêm chức năng Thông báo ở đây
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    recyclerView.setVisibility(View.GONE);
                    // TODO: Thêm chức năng Tài khoản ở đây
                    return true;
                } else {
                    return false;
                }
            }
        });
    }
}