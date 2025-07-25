package com.group5.estoreapp.api;

import com.group5.estoreapp.model.Order;
import com.group5.estoreapp.model.OrderRequest;
import com.group5.estoreapp.model.OrderResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class OrderApi {
    private static final String BASE_URL = "https://prmbe.felixtien.dev/api/";
    private static OrderApi instance;

    private interface API {
        @GET("Orders/user/{userId}")
        Call<List<Order>> getOrdersByUserId(@Path("userId") int userId);

        @POST("Orders")
        Call<OrderResponse> createOrder(@Body OrderRequest orderRequest);

//        @POST("Orders/{id}/billing")
//        Call<Void> submitBilling(@Path("id") int orderId, @Body BillingInfo billingInfo);

    }

    private final API api;

    private OrderApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(API.class);
    }

    public static synchronized OrderApi getInstance() {
        if (instance == null) instance = new OrderApi();
        return instance;
    }

    public Call<List<Order>> getOrdersByUserId(int userId) {
        return api.getOrdersByUserId(userId);
    }

    public Call<OrderResponse> createOrder(OrderRequest orderRequest) {
        return api.createOrder(orderRequest);
    }
//    public Call<Void> submitBilling(int orderId, BillingInfo billingInfo) {
//        return api.submitBilling(orderId, billingInfo);
//    }

}

