package com.group5.estoreapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.group5.estoreapp.R;
import com.group5.estoreapp.activities.EditProfileActivity;
import com.group5.estoreapp.activities.LoginActivity;
import com.group5.estoreapp.activities.OrderHistoryActivity;
import com.group5.estoreapp.api.userApi.UserApiService;
import com.group5.estoreapp.helpers.SessionManager;
import com.group5.estoreapp.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailFragment extends Fragment {

    private static final String TAG = "UserDetailFragment";

    // UI Components
    private ImageView imageAvatar;
    private ImageView imageEditAvatar;
    private ImageView imageEditInfo;
    private TextView textFullName;
    private TextView textDisplayName;
    private TextView textEmail;
    private TextView textPhone;
    private TextView textAddress;
    private LinearLayout layoutChangePassword;
    private LinearLayout layoutLogout;
    private LinearLayout layoutViewOrders; // ✅ Layout cho xem lịch sử đơn hàng

    private int currentUserId;
    private User currentUser;

    // Activity Result Launcher for Edit Profile
    private ActivityResultLauncher<Intent> editProfileLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);

        // Initialize Activity Result Launcher
        initActivityResultLauncher();

        initViews(view);
        setupClickListeners();
        loadUserData();

        return view;
    }

    private void initActivityResultLauncher() {
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        // Profile was updated successfully, refresh data
                        loadUserData();
                        Toast.makeText(getContext(), "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void initViews(View view) {
        imageAvatar = view.findViewById(R.id.imageAvatar);
        imageEditAvatar = view.findViewById(R.id.imageEditAvatar);
        imageEditInfo = view.findViewById(R.id.imageEditInfo);
        textFullName = view.findViewById(R.id.textFullName);

        // Tìm TextViews trong CardView
        textDisplayName = view.findViewById(R.id.textDisplayName);
        textEmail = view.findViewById(R.id.textEmail);
        textPhone = view.findViewById(R.id.textPhone);
        textAddress = view.findViewById(R.id.textAddress);

        // Actions
        layoutChangePassword = view.findViewById(R.id.layoutChangePassword);
        layoutLogout = view.findViewById(R.id.layoutLogout);
        layoutViewOrders = view.findViewById(R.id.layoutViewOrders); // ✅ Khởi tạo layout xem lịch sử đơn hàng
    }

    private void setupClickListeners() {
        // Click listener cho edit avatar
        imageEditAvatar.setOnClickListener(v -> {
            // TODO: Implement avatar change functionality
            Toast.makeText(getContext(), "Chức năng thay đổi avatar sẽ được cập nhật", Toast.LENGTH_SHORT).show();
        });

        // Click listener cho edit info
        imageEditInfo.setOnClickListener(v -> {
            if (currentUser != null) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("username", currentUser.getUsername());
                intent.putExtra("email", currentUser.getEmail());
                intent.putExtra("phone", currentUser.getPhoneNumber());
                intent.putExtra("address", currentUser.getAddress());
                intent.putExtra("role", currentUser.getRole());
                editProfileLauncher.launch(intent);
            } else {
                Toast.makeText(getContext(), "Đang tải thông tin người dùng...", Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener cho avatar để xem ảnh full size
        imageAvatar.setOnClickListener(v -> {
            // TODO: Implement view full size avatar
            Toast.makeText(getContext(), "Xem ảnh đại diện", Toast.LENGTH_SHORT).show();
        });

        // Click listener cho change password
        layoutChangePassword.setOnClickListener(v -> {
            // TODO: Implement change password functionality
            Toast.makeText(getContext(), "Chức năng đổi mật khẩu sẽ được cập nhật", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            // startActivity(intent);
        });

        // ✅ Click listener cho xem lịch sử đơn hàng
        layoutViewOrders.setOnClickListener(v -> {
            navigateToOrderHistory();
        });

        // Click listener cho logout
        layoutLogout.setOnClickListener(v -> {
            showLogoutConfirmDialog();
        });
    }

    // ✅ Method để chuyển đến OrderHistoryActivity
    private void navigateToOrderHistory() {
        if (currentUserId == -1) {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đến OrderHistoryActivity
        Intent intent = new Intent(getContext(), OrderHistoryActivity.class);
        startActivity(intent);
    }

    private void loadUserData() {
        // Lấy user ID từ session
        currentUserId = SessionManager.getUserId(getContext());

        if (currentUserId == -1) {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi API để lấy thông tin user
        UserApiService.getInstance()
                .getUserById(currentUserId)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            currentUser = response.body();
                            displayUserInfo(currentUser);
                        } else {
                            Log.e(TAG, "Error response: " + response.code());
                            showError("Không thể tải thông tin người dùng");
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e(TAG, "API call failed", t);
                        showError("Lỗi kết nối: " + t.getMessage());
                    }
                });
    }

    private void displayUserInfo(User user) {
        if (user == null) {
            showError("Thông tin người dùng không hợp lệ");
            return;
        }

        // Hiển thị tên người dùng ở header
        String displayName = user.getUsername() != null ? user.getUsername() : "Người dùng";
        textFullName.setText(displayName);

        // Hiển thị thông tin chi tiết trong card
        if (textDisplayName != null) {
            textDisplayName.setText(displayName);
        }

        if (textEmail != null) {
            String email = user.getEmail() != null ? user.getEmail() : "Chưa cập nhật";
            textEmail.setText(email);
        }

        if (textPhone != null) {
            String phone = user.getPhoneNumber() != null ? user.getPhoneNumber() : "Chưa cập nhật";
            textPhone.setText(phone);
        }

        if (textAddress != null) {
            String address = user.getAddress() != null ? user.getAddress() : "Chưa cập nhật";
            textAddress.setText(address);
        }

        // TODO: Load avatar from URL if available
        // For now, keep default avatar
        imageAvatar.setImageResource(R.drawable.ic_account);
    }

    private void showError(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    // Method để refresh data sau khi edit
    public void refreshUserData() {
        if (currentUserId != -1) {
            loadUserData();
        }
    }

    // Getter cho current user (có thể dùng cho edit activities)
    public User getCurrentUser() {
        return currentUser;
    }

    private void showLogoutConfirmDialog() {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    performLogout();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void performLogout() {
        if (getContext() == null) return;

        // Xóa session data
        SharedPreferences pref = getContext().getSharedPreferences("auth", getContext().MODE_PRIVATE);
        pref.edit().clear().apply();

        // Chuyển về login activity
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        if (getActivity() != null) {
            getActivity().finish();
        }

        Toast.makeText(getContext(), "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data khi quay lại từ edit screen
        if (currentUserId != -1) {
            loadUserData();
        }
    }
}