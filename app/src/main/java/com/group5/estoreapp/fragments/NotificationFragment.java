// NotificationFragment.java
package com.group5.estoreapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group5.estoreapp.R;
import com.group5.estoreapp.adapter.NotificationAdapter;
import com.group5.estoreapp.model.ApiResponse;
import com.group5.estoreapp.model.Notification;
import com.group5.estoreapp.services.NotificationService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment implements
        NotificationAdapter.OnNotificationClickListener {

    private static final String TAG = "NotificationFragment";
    private static final long AUTO_REFRESH_INTERVAL = 30000; // 30 seconds

    private NotificationAdapter notificationAdapter;
    private NotificationService notificationService;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fabAddNotification;

    private Handler refreshHandler;
    private Runnable refreshRunnable;

    private int userId;
    private String role;
    private boolean isAutoRefreshEnabled = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        notificationService = new NotificationService(requireContext());

        // Lấy userId và role từ SharedPreferences
        SharedPreferences pref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        userId = pref.getInt("userId", -1);
        role = pref.getString("role", "User");

        // View binding
        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        fabAddNotification = view.findViewById(R.id.fabAddNotification);

        // Setup RecyclerView
        notificationAdapter = new NotificationAdapter(new ArrayList<>());
        notificationAdapter.setOnNotificationClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notificationAdapter);

        // Setup SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this::loadNotifications);

        // Setup FAB (chỉ hiển thị cho Admin)
        if ("Admin".equalsIgnoreCase(role)) {
            fabAddNotification.setVisibility(View.VISIBLE);
            fabAddNotification.setOnClickListener(v -> showCreateNotificationDialog());
        } else {
            fabAddNotification.setVisibility(View.GONE);
        }

        // Setup auto-refresh
        setupAutoRefresh();

        // Load notifications
        loadNotifications();

        return view;
    }

    private void setupAutoRefresh() {
        refreshHandler = new Handler(Looper.getMainLooper());
        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                if (isAutoRefreshEnabled && isAdded() && !isDetached()) {
                    loadNotifications();
                    refreshHandler.postDelayed(this, AUTO_REFRESH_INTERVAL);
                }
            }
        };
    }

    private void startAutoRefresh() {
        isAutoRefreshEnabled = true;
        refreshHandler.postDelayed(refreshRunnable, AUTO_REFRESH_INTERVAL);
    }

    private void stopAutoRefresh() {
        isAutoRefreshEnabled = false;
        if (refreshHandler != null) {
            refreshHandler.removeCallbacks(refreshRunnable);
        }
    }

    private void loadNotifications() {
        swipeRefreshLayout.setRefreshing(true);

        if ("Admin".equalsIgnoreCase(role)) {
            // Admin xem tất cả notifications
            notificationService.getAllNotifications(new Callback<List<Notification>>() {
                @Override
                public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                    if (isAdded() && !isDetached()) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            List<Notification> notifications = response.body();
                            // Sắp xếp theo thời gian giảm dần (mới nhất lên đầu)
                            Collections.sort(notifications, (n1, n2) -> {
                                if (n1.getCreatedTime() != null && n2.getCreatedTime() != null) {
                                    return n2.getCreatedTime().compareTo(n1.getCreatedTime());
                                }
                                return 0;
                            });
                            notificationAdapter.setNotifications(notifications);
                        } else {
                            Toast.makeText(getContext(), "Không thể tải thông báo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Notification>> call, Throwable t) {
                    if (isAdded() && !isDetached()) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e(TAG, "Lỗi API getAllNotifications: " + t.getMessage());
                        Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // User chỉ xem notifications của mình
            notificationService.getNotificationsByUserId(userId, new Callback<List<Notification>>() {
                @Override
                public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                    if (isAdded() && !isDetached()) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            List<Notification> notifications = response.body();
                            // Sắp xếp theo thời gian giảm dần (mới nhất lên đầu)
                            Collections.sort(notifications, (n1, n2) -> {
                                if (n1.getCreatedTime() != null && n2.getCreatedTime() != null) {
                                    return n2.getCreatedTime().compareTo(n1.getCreatedTime());
                                }
                                return 0;
                            });
                            notificationAdapter.setNotifications(notifications);
                        } else {
                            Toast.makeText(getContext(), "Không thể tải thông báo", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Notification>> call, Throwable t) {
                    if (isAdded() && !isDetached()) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e(TAG, "Lỗi API getNotificationsByUserId: " + t.getMessage());
                        Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void showCreateNotificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Tạo thông báo mới");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_notification, null);
        EditText etMessage = dialogView.findViewById(R.id.etMessage);
        EditText etUserId = dialogView.findViewById(R.id.etUserId);

        builder.setView(dialogView);
        builder.setPositiveButton("Tạo", (dialog, which) -> {
            String message = etMessage.getText().toString().trim();
            String userIdStr = etUserId.getText().toString().trim();

            if (!message.isEmpty() && !userIdStr.isEmpty()) {
                try {
                    int targetUserId = Integer.parseInt(userIdStr);
                    createNotification(message, targetUserId);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "User ID không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);

        builder.create().show();
    }

    private void createNotification(String message, int targetUserId) {
        notificationService.createNotification(message, targetUserId, new Callback<ApiResponse<Notification>>() {
            @Override
            public void onResponse(Call<ApiResponse<Notification>> call, Response<ApiResponse<Notification>> response) {
                if (isAdded() && !isDetached()) {
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Toast.makeText(getContext(), "Đã tạo thông báo", Toast.LENGTH_SHORT).show();
                        loadNotifications();
                    } else {
                        Toast.makeText(getContext(), "Không thể tạo thông báo", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Notification>> call, Throwable t) {
                if (isAdded() && !isDetached()) {
                    Log.e(TAG, "Lỗi tạo thông báo: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi tạo thông báo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onNotificationClick(Notification notification, int position) {
        // Đánh dấu đã đọc nếu chưa đọc
        if (!notification.isRead()) {
            markAsRead(notification, position);
        }

        // Hiển thị chi tiết thông báo
        showNotificationDetails(notification);
    }

    @Override
    public void onNotificationLongClick(Notification notification, int position) {
        showNotificationOptions(notification, position);
    }

    private void markAsRead(Notification notification, int position) {
        notificationService.markAsRead(notification.getNotificationID(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (isAdded() && !isDetached()) {
                    if (response.isSuccessful()) {
                        notification.setRead(true);
                        notificationAdapter.updateNotification(notification);
                        Toast.makeText(getContext(), "Đã đánh dấu đã đọc", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Không thể đánh dấu đã đọc", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (isAdded() && !isDetached()) {
                    Log.e(TAG, "Lỗi đánh dấu đã đọc: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi đánh dấu đã đọc", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showNotificationDetails(Notification notification) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chi tiết thông báo");

        String details = "Nội dung: " + notification.getMessage() + "\n" +
                "User ID: " + notification.getUserID() + "\n" +
                "Thời gian: " + (notification.getCreatedTime() != null ? notification.getCreatedTime() : "N/A") + "\n" +
                "Trạng thái: " + (notification.isRead() ? "Đã đọc" : "Chưa đọc");

        builder.setMessage(details);
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    private void showNotificationOptions(Notification notification, int position) {
        String[] options;
        if ("Admin".equalsIgnoreCase(role)) {
            options = new String[]{"Đánh dấu đã đọc", "Xóa thông báo", "Gửi tương tự", "Làm mới"};
        } else {
            options = new String[]{"Đánh dấu đã đọc", "Xóa thông báo", "Làm mới"};
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Tùy chọn thông báo");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Đánh dấu đã đọc
                    if (!notification.isRead()) {
                        markAsRead(notification, position);
                    } else {
                        Toast.makeText(requireContext(), "Thông báo đã được đọc", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1: // Xóa thông báo
                    showDeleteConfirmationDialog(notification, position);
                    break;
                case 2: // Gửi tương tự (Admin) hoặc Làm mới (User)
                    if ("Admin".equalsIgnoreCase(role)) {
                        showSendSimilarDialog(notification);
                    } else {
                        loadNotifications();
                    }
                    break;
                case 3: // Làm mới (chỉ Admin)
                    if ("Admin".equalsIgnoreCase(role)) {
                        loadNotifications();
                    }
                    break;
            }
        });
        builder.create().show();
    }

    private void showDeleteConfirmationDialog(Notification notification, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xóa thông báo");
        builder.setMessage("Bạn có chắc muốn xóa thông báo này?");
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            deleteNotification(notification, position);
        });
        builder.setNegativeButton("Hủy", null);
        builder.create().show();
    }

    private void deleteNotification(Notification notification, int position) {
        notificationService.deleteNotification(notification.getNotificationID(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (isAdded() && !isDetached()) {
                    if (response.isSuccessful()) {
                        notificationAdapter.removeNotification(notification.getNotificationID());
                        Toast.makeText(getContext(), "Đã xóa thông báo", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Không thể xóa thông báo", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (isAdded() && !isDetached()) {
                    Log.e(TAG, "Lỗi xóa thông báo: " + t.getMessage());
                    Toast.makeText(getContext(), "Lỗi xóa thông báo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSendSimilarDialog(Notification notification) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Gửi thông báo tương tự");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_send_similar, null);
        EditText etUserId = dialogView.findViewById(R.id.etUserId);

        builder.setView(dialogView);
        builder.setPositiveButton("Gửi", (dialog, which) -> {
            String userIdStr = etUserId.getText().toString().trim();
            if (!userIdStr.isEmpty()) {
                try {
                    int targetUserId = Integer.parseInt(userIdStr);
                    createNotification(notification.getMessage(), targetUserId);
                } catch (NumberFormatException e) {
                    Toast.makeText(requireContext(), "User ID không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập User ID", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);

        builder.create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Bắt đầu auto-refresh khi fragment visible
        startAutoRefresh();
        // Refresh ngay khi resume
        loadNotifications();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Dừng auto-refresh khi fragment không visible
        stopAutoRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cleanup
        stopAutoRefresh();
    }
}