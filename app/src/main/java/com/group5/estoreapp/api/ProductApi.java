package com.group5.estoreapp.api;

import com.group5.estoreapp.model.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductApi {
    @GET("products")
    Call<List<Product>> getProducts();

    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") int id);

    @GET("products/categories")
    Call<List<String>> getCategories();

    @GET("products/category/{category}")
    Call<List<Product>> getProductsByCategory(@Path("category") String category);
}