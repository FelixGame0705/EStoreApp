package com.group5.estoreapp.api;

import com.group5.estoreapp.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ProductApi {
    private static final String BASE_URL = "https://prmbe.felixtien.dev/api/";
    private static ProductApi instance;

    // Interface ẩn bên trong
    private interface API {
        @GET("Products")
        Call<List<Product>> getProducts();

        @GET("Products/{id}")
        Call<Product> getProductById(@Path("id") int id);

//        @GET("/Products/categories")
//        Call<List<String>> getCategories();
//
//        @GET("/Products/category/{category}")
//        Call<List<Product>> getProductsByCategory(@Path("category") String category);
    }

    private final API api;

    private ProductApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(API.class);
    }

    public static synchronized ProductApi getInstance() {
        if (instance == null) instance = new ProductApi();
        return instance;
    }

    // Các method public để gọi API
    public Call<List<Product>> getProducts() {
        return api.getProducts();
    }

    public Call<Product> getProductById(int id) {
        return api.getProductById(id);
    }

//    public Call<List<String>> getCategories() {
//        return api.getCategories();
//    }
//
//    public Call<List<Product>> getProductsByCategory(String category) {
//        return api.getProductsByCategory(category);
//    }
}
