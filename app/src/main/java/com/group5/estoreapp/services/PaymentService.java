package com.group5.estoreapp.services;

import android.util.Log;

import com.group5.estoreapp.api.PaymentApi;
import com.group5.estoreapp.model.ApiResponse;
import com.group5.estoreapp.model.PaymentRequest;
import com.group5.estoreapp.model.PaymentResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentService {

    private final PaymentApi api;

    public PaymentService() {
        this.api = PaymentApi.getInstance();
    }

    public interface PaymentCallback {
        void onSuccess(String paymentUrl);
        void onError(Throwable t);
    }

    public interface PaymentStatusCallback {
        void onResult(boolean isSuccess);
        void onError(Throwable t);
    }

    public void createPayment(PaymentRequest request, PaymentCallback callback) {
        api.createPayment(request).enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getPaymentUrl() != null) {
                    callback.onSuccess(response.body().getPaymentUrl());
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e("PaymentService", "Tạo link thanh toán thất bại: " + errorBody);
                        callback.onError(new Exception("Tạo link thanh toán thất bại: " + errorBody));
                    } catch (Exception e) {
                        callback.onError(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.e("PaymentService", "Lỗi kết nối tới server tạo thanh toán", t);
                callback.onError(t);
            }
        });
    }

    public void checkPaymentStatus(int orderId, PaymentStatusCallback callback) {
        PaymentApi.getInstance().getPaymentByOrderId(orderId).enqueue(new Callback<List<PaymentResponse>>() {
            @Override
            public void onResponse(Call<List<PaymentResponse>> call, Response<List<PaymentResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    String status = response.body().get(0).getPaymentStatus(); // ✅ Lấy paymentStatus đầu tiên
                    boolean isSuccess = "Completed".equalsIgnoreCase(status);
                    callback.onResult(isSuccess);
                } else {
                    callback.onResult(false);
                }
            }

            @Override
            public void onFailure(Call<List<PaymentResponse>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
