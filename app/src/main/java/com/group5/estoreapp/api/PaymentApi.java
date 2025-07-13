package com.group5.estoreapp.api;

import com.group5.estoreapp.model.PaymentRequest;
import com.group5.estoreapp.model.PaymentResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class PaymentApi {
    private static final String BASE_URL = "https://prmbe.felixtien.dev/api/";
    private static PaymentApi instance;

    private interface API {
        @POST("Payments/process")
        Call<PaymentResponse> createPayment(@Body PaymentRequest request);
    }

    private final API api;

    private PaymentApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(API.class);
    }

    public static synchronized PaymentApi getInstance() {
        if (instance == null) instance = new PaymentApi();
        return instance;
    }

    public Call<PaymentResponse> createPayment(PaymentRequest paymentRequest) {
        return api.createPayment(paymentRequest);
    }
}
