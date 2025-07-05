package com.group5.estoreapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group5.estoreapp.R;
import com.group5.estoreapp.fragments.CartFragment;
import com.group5.estoreapp.fragments.ProductFragment;
import com.group5.estoreapp.model.CartItem;
import com.group5.estoreapp.services.CartService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CartFragment.CartBadgeListener {

    private BottomNavigationView bottomNavigationView;
    private final int userId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        updateCartBadge(userId);

        // Load ProductFragment khi mở app
        loadFragment(new ProductFragment());

        // Set selected mặc định là "Home"
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new ProductFragment());
                return true;
            } else if (id == R.id.nav_cart) {
                loadFragment(new CartFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                // loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void updateCartBadge(int userId) {
        CartService cartService = new CartService();
        cartService.getCartsByUserId(userId, new CartService.CartItemsCallback() {
            @Override
            public void onCartItemsLoaded(List<CartItem> cartItems) {
                int totalItems = 0;
                for (CartItem item : cartItems) {
                    totalItems += item.getQuantity();
                }

                BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.nav_cart);
                if (totalItems > 0) {
                    badge.setVisible(true);
                    badge.setNumber(totalItems);
                    badge.setBackgroundColor(getColor(R.color.red)); // make sure R.color.red is defined
                } else {
                    bottomNavigationView.removeBadge(R.id.nav_cart);
                }
            }

            @Override
            public void onError(Throwable t) {
                bottomNavigationView.removeBadge(R.id.nav_cart);
            }
        });
    }


    @Override
    public void updateCartBadge() {
        updateCartBadge(userId); // gọi lại khi Fragment yêu cầu
    }
}
