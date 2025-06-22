package com.group5.estoreapp.api;

import com.group5.estoreapp.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductApiService {
    private static final String BASE_URL = "https://fakestoreapi.com/";
    private static ProductApiService instance;
    private ProductApi productApi;

    private ProductApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        productApi = retrofit.create(ProductApi.class);
    }

    public static synchronized ProductApiService getInstance() {
        if (instance == null) {
            instance = new ProductApiService();
        }
        return instance;
    }

    public Call<List<Product>> getProducts() {
        return productApi.getProducts();
    }

    public Call<Product> getProductById(int id) {
        return productApi.getProductById(id);
    }

    public Call<List<String>> getCategories() {
        return productApi.getCategories();
    }

    public Call<List<Product>> getProductsByCategory(String category) {
        return productApi.getProductsByCategory(category);
    }
}