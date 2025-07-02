package com.group5.estoreapp.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group5.estoreapp.R;
import com.group5.estoreapp.fragments.ProductFragment;


import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load ProductFragment khi mở app
        loadFragment(new ProductFragment());

        // Set selected mặc định là "Home"
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // Xử lý khi chọn các tab
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new ProductFragment());
                return true;
            } else if (id == R.id.nav_cart) {
                // loadFragment(new CartFragment());
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
}
