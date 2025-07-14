// NotificationService.java
package com.group5.estoreapp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.group5.estoreapp.model.ApiResponse;
import com.group5.estoreapp.model.Notification;
import com.group5.estoreapp.model.NotificationRequest;
import com.group5.estoreapp.model.UpdateNotificationRequest;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationService {
    private static final String TAG = "NotificationService";
    private static final String BASE_URL = "https://prmbe.felixtien.dev/"; // Your API base URL
    private NotificationApiService apiService;
    private Context context;

    public NotificationService(Context context) {
        this.context = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(NotificationApiService.class);
    }

    // Lấy tất cả notifications
    public void getAllNotifications(Callback<List<Notification>> callback) {
        Log.d(TAG, "Getting all notifications");
        Call<List<Notification>> call = apiService.getAllNotifications();
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                Log.d(TAG, "getAllNotifications response: " + response.code());
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e(TAG, "getAllNotifications failed: " + t.getMessage());
                callback.onFailure(call, t);
            }
        });
    }

    // Lấy notification theo ID
    public void getNotificationById(int id, Callback<Notification> callback) {
        Log.d(TAG, "Getting notification by ID: " + id);
        Call<Notification> call = apiService.getNotificationById(id);
        call.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                Log.d(TAG, "getNotificationById response: " + response.code());
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Log.e(TAG, "getNotificationById failed: " + t.getMessage());
                callback.onFailure(call, t);
            }
        });
    }

    // Lấy notifications theo user ID
    public void getNotificationsByUserId(int userId, Callback<List<Notification>> callback) {
        Log.d(TAG, "Getting notifications for user ID: " + userId);
        Call<List<Notification>> call = apiService.getNotificationsByUserId(userId);
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                Log.d(TAG, "getNotificationsByUserId response: " + response.code());
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Log.e(TAG, "getNotificationsByUserId failed: " + t.getMessage());
                callback.onFailure(call, t);
            }
        });
    }

    // Tạo notification mới
    public void createNotification(String message, int userId, Callback<ApiResponse<Notification>> callback) {
        Log.d(TAG, "Creating notification for user " + userId + ": " + message);
        NotificationRequest request = new NotificationRequest(message, userId);
        Call<ApiResponse<Notification>> call = apiService.createNotification(request);
        call.enqueue(new Callback<ApiResponse<Notification>>() {
            @Override
            public void onResponse(Call<ApiResponse<Notification>> call, Response<ApiResponse<Notification>> response) {
                Log.d(TAG, "createNotification response: " + response.code());
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ApiResponse<Notification>> call, Throwable t) {
                Log.e(TAG, "createNotification failed: " + t.getMessage());
                callback.onFailure(call, t);
            }
        });
    }

    // Cập nhật notification
    public void updateNotification(int id, String message, boolean isRead, Callback<ResponseBody> callback) {
        Log.d(TAG, "Updating notification ID " + id + ": isRead=" + isRead);
        UpdateNotificationRequest request = new UpdateNotificationRequest(message, isRead);
        Call<ResponseBody> call = apiService.updateNotification(id, request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "updateNotification response: " + response.code());
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "updateNotification failed: " + t.getMessage());
                callback.onFailure(call, t);
            }
        });
    }

    // Đánh dấu notification đã đọc
    public void markAsRead(int notificationId, Callback<ResponseBody> callback) {
        Log.d(TAG, "Marking notification " + notificationId + " as read");
        updateNotification(notificationId, "", true, callback);
    }

    // Xóa notification
    public void deleteNotification(int id, Callback<ResponseBody> callback) {
        Log.d(TAG, "Deleting notification ID: " + id);
        Call<ResponseBody> call = apiService.deleteNotification(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "deleteNotification response: " + response.code());
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "deleteNotification failed: " + t.getMessage());
                callback.onFailure(call, t);
            }
        });
    }

    // Lấy auth token nếu cần
    private String getAuthToken() {
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        return prefs.getString("token", "");
    }

    // Lấy current user ID
    public int getCurrentUserId() {
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        return prefs.getInt("userId", -1);
    }

    // Lấy current user role
    public String getCurrentUserRole() {
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        return prefs.getString("role", "User");
    }

    // Custom callback interfaces
    public interface BooleanCallback {
        void onSuccess(boolean result);
        void onFailure(String error);
    }

    public interface IntegerCallback {
        void onSuccess(int result);
        void onFailure(String error);
    }

    // Kiểm tra xem có notification chưa đọc không
    public void hasUnreadNotifications(int userId, BooleanCallback callback) {
        getNotificationsByUserId(userId, new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean hasUnread = false;
                    for (Notification notification : response.body()) {
                        if (!notification.isRead()) {
                            hasUnread = true;
                            break;
                        }
                    }
                    callback.onSuccess(hasUnread);
                } else {
                    callback.onSuccess(false);
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Đếm số notification chưa đọc
    public void getUnreadCount(int userId, IntegerCallback callback) {
        getNotificationsByUserId(userId, new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int unreadCount = 0;
                    for (Notification notification : response.body()) {
                        if (!notification.isRead()) {
                            unreadCount++;
                        }
                    }
                    callback.onSuccess(unreadCount);
                } else {
                    callback.onSuccess(0);
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }

    // Custom callback for simple operations
    public interface SimpleCallback {
        void onSuccess(String message);
        void onFailure(String error);
    }

    // Đánh dấu tất cả notifications là đã đọc
    public void markAllAsRead(int userId, SimpleCallback callback) {
        getNotificationsByUserId(userId, new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Notification> notifications = response.body();

                    if (notifications.isEmpty()) {
                        callback.onSuccess("No notifications to mark");
                        return;
                    }

                    // Đếm số notification cần update
                    int totalUnread = 0;
                    for (Notification notification : notifications) {
                        if (!notification.isRead()) {
                            totalUnread++;
                        }
                    }

                    if (totalUnread == 0) {
                        callback.onSuccess("All notifications already read");
                        return;
                    }

                    // Đánh dấu từng notification là đã đọc
                    final int[] updatedCount = {0};
                    final boolean[] hasError = {false};

                    for (Notification notification : notifications) {
                        if (!notification.isRead()) {
                            int finalTotalUnread = totalUnread;
                            markAsRead(notification.getNotificationID(), new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    updatedCount[0]++;
                                    if (updatedCount[0] == finalTotalUnread && !hasError[0]) {
                                        callback.onSuccess("All notifications marked as read");
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    if (!hasError[0]) {
                                        hasError[0] = true;
                                        callback.onFailure("Failed to mark some notifications as read");
                                    }
                                }
                            });
                        }
                    }
                } else {
                    callback.onFailure("Failed to get notifications");
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                callback.onFailure("Failed to get notifications: " + t.getMessage());
            }
        });
    }
}

