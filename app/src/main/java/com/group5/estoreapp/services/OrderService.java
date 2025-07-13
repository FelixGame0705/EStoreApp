package com.group5.estoreapp.services;

import com.group5.estoreapp.api.OrderApi;
import com.group5.estoreapp.model.Order;
import com.group5.estoreapp.model.OrderRequest;
import com.group5.estoreapp.model.OrderResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderService {

    // Callback để lấy danh sách đơn hàng của user
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

    // Callback để nhận kết quả tạo đơn hàng
    public interface CreateOrderCallback {
        void onSuccess(int orderId);
        void onError(Throwable t);
    }

    // Tạo đơn hàng và lấy về orderId
    public void createOrder(OrderRequest orderRequest, CreateOrderCallback callback) {
        OrderApi.getInstance().createOrder(orderRequest).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int orderId = response.body().getOrderId();
                    callback.onSuccess(orderId);
                } else {
                    callback.onError(new Exception("Tạo đơn hàng thất bại"));
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
