package com.group5.estoreapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.estoreapp.R;
import com.group5.estoreapp.adapter.ProductAdapter;
import com.group5.estoreapp.model.Product;
import com.group5.estoreapp.services.ProductService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private ViewFlipper viewFlipper;
    private ProductAdapter productAdapter;
    private List<Product> allProducts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
//        searchView = view.findViewById(R.id.searchView);
        viewFlipper = view.findViewById(R.id.viewFlipper);

        setupViewFlipper();
        loadProductsFromApi();

        setupMenu(); // <-- Gắn menu

        return view;
    }

    private void setupMenu() {
        MenuHost menuHost = requireActivity();

        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_sort, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.sort_name) {
                    sortProductsByName();
                    return true;
                } else if (id == R.id.sort_price) {
                    sortProductsByPrice();
                    return true;
                } else if (id == R.id.sort_category) {
                    sortProductsByCategory();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void setupViewFlipper() {
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();
    }

    private void loadProductsFromApi() {
        ProductService service = new ProductService();
        service.getAllProducts(new ProductService.ProductCallback() {
            @Override
            public void onSuccess(List<Product> productList) {
                allProducts = productList;
                productAdapter = new ProductAdapter(requireContext(), productList);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Lỗi API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sortProductsByName() {
        if (allProducts != null) {
            Collections.sort(allProducts, Comparator.comparing(Product::getProductName, String::compareToIgnoreCase));
            productAdapter.setProductList(allProducts);
        }
    }

    private void sortProductsByPrice() {
        if (allProducts != null) {
            Collections.sort(allProducts, Comparator.comparing(Product::getPrice));
            productAdapter.setProductList(allProducts);
        }
    }

    private void sortProductsByCategory() {
        if (allProducts != null) {
            Collections.sort(allProducts, Comparator.comparing(Product::getCategoryName, String::compareToIgnoreCase));
            productAdapter.setProductList(allProducts);
        }
    }
}
