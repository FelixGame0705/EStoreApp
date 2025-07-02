package com.group5.estoreapp.services;

import com.group5.estoreapp.api.ProductApi;
import com.group5.estoreapp.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductService {

    // Interface để callback kết quả
    public interface ProductCallback {
        void onSuccess(List<Product> productList);
        void onError(Throwable t);
    }
    public interface SingleProductCallback {
        void onSuccess(Product product);
        void onError(Throwable t);
    }

    // Gọi API và trả kết quả qua callback
    public void getAllProducts(ProductCallback callback) {
        ProductApi.getInstance().getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Lỗi lấy danh sách sản phẩm: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
    public void getProductById(int productId, SingleProductCallback callback) {
        ProductApi.getInstance().getProductById(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Không tìm thấy sản phẩm ID = " + productId));
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

}
