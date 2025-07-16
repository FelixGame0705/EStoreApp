// NotificationApiService.java - Interface không đổi
package com.group5.estoreapp.services;

import com.group5.estoreapp.model.ApiResponse;
import com.group5.estoreapp.model.Notification;
import com.group5.estoreapp.model.NotificationRequest;
import com.group5.estoreapp.model.UpdateNotificationRequest;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface NotificationApiService {

    @GET("api/notification")
    Call<List<Notification>> getAllNotifications();

    @GET("api/notification/{id}")
    Call<Notification> getNotificationById(@Path("id") int id);

    @GET("api/notification/user/{userId}")
    Call<List<Notification>> getNotificationsByUserId(@Path("userId") int userId);

    @POST("api/notification")
    Call<ApiResponse<Notification>> createNotification(@Body NotificationRequest request);

    @PUT("api/notification/{id}")
    Call<ResponseBody> updateNotification(@Path("id") int id, @Body UpdateNotificationRequest request);

    @DELETE("api/notification/{id}")
    Call<ResponseBody> deleteNotification(@Path("id") int id);
}