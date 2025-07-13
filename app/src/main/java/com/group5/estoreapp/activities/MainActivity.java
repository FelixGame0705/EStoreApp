package com.group5.estoreapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group5.estoreapp.R;
import com.group5.estoreapp.fragments.CartFragment;
import com.group5.estoreapp.fragments.ChatFragment;
import com.group5.estoreapp.fragments.ProductFragment;
import com.group5.estoreapp.model.CartItem;
import com.group5.estoreapp.services.CartService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CartFragment.CartBadgeListener {
    public static MainActivity instance;

    private BottomNavigationView bottomNavigationView;
    private int userId; // ✅ Đưa userId ra toàn cục

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        instance = this; // ✅ Khởi tạo instance
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);

        userId = prefs.getInt("userId", -1); // ✅ Lấy trực tiếp từ SharedPreferences
        updateCartBadge(userId);

        // Load ProductFragment khi mở app
        loadFragment(new ProductFragment());

        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new ProductFragment());
                return true;
            } else if (id == R.id.nav_cart) {
                loadFragment(new CartFragment());
                return true;
            } else if (id == R.id.nav_chat) {
                loadFragment(new ChatFragment());
                return true;
            } else if (id == R.id.nav_notifications) {
//                loadFragment(new ChatFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                // loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null; // ✅ clear instance để tránh memory leak
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

    }


    @Override
    public void updateCartBadge(int itemCount) {
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.nav_cart);
        if (itemCount > 0) {
            badge.setVisible(true);
            badge.setNumber(itemCount);
            badge.setBackgroundColor(getColor(R.color.red));
        } else {
            bottomNavigationView.removeBadge(R.id.nav_cart);
        }
    }

}
