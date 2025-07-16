package com.group5.estoreapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", null);

        Request original = chain.request();
        Request.Builder builder = original.newBuilder();

        if (token != null) {
            builder.header("Authorization", "Bearer " + token);

            // ✅ Log token ra Logcat
            Log.d("AuthInterceptor", "Using token: " + token);
        } else {
            Log.w("AuthInterceptor", "No accessToken found in SharedPreferences");
        }

        Request request = builder.build();

        // ✅ Log request info
        Log.d("AuthInterceptor", "Request URL: " + request.url());
        Log.d("AuthInterceptor", "Request Headers: " + request.headers());

        return chain.proceed(request);
    }
}
