package com.group5.estoreapp.services;

import com.google.gson.JsonObject;
import com.group5.estoreapp.api.UserApi;
import com.group5.estoreapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {

    // ===== CALLBACK INTERFACES =====

    public interface LoginCallback {
        void onSuccess(JsonObject response);
        void onError(Throwable t);
    }

    public interface RegisterCallback {
        void onSuccess(JsonObject response);
        void onError(Throwable t);
    }

    public interface UserCallback {
        void onSuccess(User user);
        void onError(Throwable t);
    }

    public interface ValidateCallback {
        void onSuccess(JsonObject result);
        void onError(Throwable t);
    }

    // ===== METHODS =====

    public void login(String username, String password, LoginCallback callback) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);

        UserApi.getInstance().login(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Sai tài khoản hoặc mật khẩu"));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void register(JsonObject body, RegisterCallback callback) {
        UserApi.getInstance().register(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Đăng ký thất bại"));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(t);
            }
        });
    }


    public void getUserByUsername(String username, UserCallback callback) {
        UserApi.getInstance().getUserByUsername(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Không tìm thấy người dùng"));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void validateUser(String username, String password, ValidateCallback callback) {
        JsonObject body = new JsonObject();
        body.addProperty("username", username);
        body.addProperty("password", password);

        UserApi.getInstance().validateUser(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError(new Exception("Xác thực thất bại"));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
