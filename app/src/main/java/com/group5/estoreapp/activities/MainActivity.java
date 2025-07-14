package com.group5.estoreapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group5.estoreapp.R;
import com.group5.estoreapp.fragments.AdminChatFragment;
import com.group5.estoreapp.fragments.CartFragment;
import com.group5.estoreapp.fragments.ChatFragment;
import com.group5.estoreapp.fragments.ProductFragment;
import com.group5.estoreapp.fragments.UserDetailFragment;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements CartFragment.CartBadgeListener {

    public static MainActivity instance;

    private BottomNavigationView bottomNavigationView;
    private int userId;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION_PERMISSION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SearchView searchView = findViewById(R.id.searchView);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        instance = this;
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        updateCartBadge(userId);
        String role = prefs.getString("role", "User");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sendSearchQueryToProductFragment(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sendSearchQueryToProductFragment(newText);
                return true;
            }
        });

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
            if ("Admin".equalsIgnoreCase(role)) {
                loadFragment(new AdminChatFragment()); // 👈 admin dùng fragment khác
            } else {
                loadFragment(new ChatFragment());
            }
            return true;
        }
        else if (id == R.id.nav_notifications) {
//                loadFragment(new ChatFragment());
                return true;
            } else if (id == R.id.nav_profile) {
                loadFragment(new UserDetailFragment());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
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

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_find_nearest) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            } else {
                findNearestStore();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void sendSearchQueryToProductFragment(String query) {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (current instanceof ProductFragment) {
            ((ProductFragment) current).onSearchQuery(query);
        }
    }

    private void findNearestStore() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        String url = "https://prmbe.felixtien.dev/api/StoreLocations/nearest"
                                + "?userLatitude=" + lat + "&userLongitude=" + lon;

                        RequestQueue queue = Volley.newRequestQueue(this);

                        JsonObjectRequest request = new JsonObjectRequest(
                                Request.Method.GET,
                                url,
                                null,
                                response -> {
                                    try {
                                        double storeLat = response.getDouble("latitude");
                                        double storeLon = response.getDouble("longitude");

                                        Intent intent = new Intent(this, MapActivity.class);
                                        intent.putExtra("userLat", lat);
                                        intent.putExtra("userLon", lon);
                                        intent.putExtra("storeLat", storeLat);
                                        intent.putExtra("storeLon", storeLon);
                                        startActivity(intent);

                                    } catch (Exception e) {
                                        Toast.makeText(this, "Lỗi xử lý phản hồi", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                },
                                error -> {
                                    Toast.makeText(this, "Không gọi được API", Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();
                                }
                        );

                        queue.add(request);
                    } else {
                        Toast.makeText(this, "Không thể lấy vị trí hiện tại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findNearestStore();
            } else {
                Toast.makeText(this, "Ứng dụng cần quyền truy cập vị trí!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
