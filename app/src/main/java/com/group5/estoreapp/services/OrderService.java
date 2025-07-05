package com.group5.estoreapp.services;

import com.group5.estoreapp.api.OrderApi;
import com.group5.estoreapp.model.Order;
import com.group5.estoreapp.model.OrderRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService {

    public interface OrderCallback {
        void onOrdersLoaded(List<Order> orders);
        void onError(Throwable t);
    }

    public void getOrdersByUserId(int userId, OrderCallback callback) {
        OrderApi.getInstance().getOrdersByUserId(userId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onOrdersLoaded(response.body());
                } else {
                    callback.onError(new Exception("Không có đơn hàng nào."));
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public interface CreateOrderCallback {
        void onSuccess();
        void onError(Throwable t);
    }

    public void createOrder(OrderRequest orderRequest, CreateOrderCallback callback) {
        OrderApi.getInstance().createOrder(orderRequest).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(new Exception("Tạo đơn hàng thất bại"));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
