package com.group5.estoreapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group5.estoreapp.R;
import com.group5.estoreapp.adapter.ProductAdapter;
import com.group5.estoreapp.model.Product;
import com.group5.estoreapp.services.ProductService;

import java.util.List;

public class ProductFragment extends Fragment {
    private RecyclerView recyclerView;
    private SearchView searchView;
    private ViewFlipper viewFlipper;
    private ProductAdapter productAdapter;

    public ProductFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        viewFlipper = view.findViewById(R.id.viewFlipper);

        setupViewFlipper();
        loadProductsFromApi();

        return view;
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
}
