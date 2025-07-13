package com.group5.estoreapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.group5.estoreapp.R;
import com.group5.estoreapp.api.userApi.UserApiService;
import com.group5.estoreapp.helpers.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";

    // UI Components
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPhone;
    private EditText etAddress;
    private Button btnSave;
    private Button btnCancel;
    private ProgressBar progressBar;
    private ImageView ivBack;

    // Data
    private int userId;
    private String originalUsername;
    private String originalEmail;
    private String originalPhone;
    private String originalAddress;
    private String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setupToolbar();
        initViews();
        getUserDataFromIntent();
        setupClickListeners();
        populateFields();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chỉnh sửa thông tin");
        }
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
        progressBar = findViewById(R.id.progress_bar);
        ivBack = findViewById(R.id.iv_back);
    }

    private void getUserDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            userId = SessionManager.getUserId(this);
            originalUsername = intent.getStringExtra("username");
            originalEmail = intent.getStringExtra("email");
            originalPhone = intent.getStringExtra("phone");
            originalAddress = intent.getStringExtra("address");
            userRole = intent.getStringExtra("role");

            if (userRole == null) {
                userRole = "User"; // Default role
            }
        }
    }

    private void populateFields() {
        if (originalUsername != null) etUsername.setText(originalUsername);
        if (originalEmail != null) etEmail.setText(originalEmail);
        if (originalPhone != null) etPhone.setText(originalPhone);
        if (originalAddress != null) etAddress.setText(originalAddress);
    }

    private void setupClickListeners() {
        ivBack.setOnClickListener(v -> onBackPressed());

        btnCancel.setOnClickListener(v -> onBackPressed());

        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                updateUserProfile();
            }
        });
    }

    private boolean validateInputs() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Validate username
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Tên người dùng không được để trống");
            etUsername.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            etUsername.setError("Tên người dùng phải có ít nhất 3 ký tự");
            etUsername.requestFocus();
            return false;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email không được để trống");
            etEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return false;
        }

        // Validate phone
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Số điện thoại không được để trống");
            etPhone.requestFocus();
            return false;
        }

        if (phone.length() < 10 || phone.length() > 11) {
            etPhone.setError("Số điện thoại phải có 10-11 số");
            etPhone.requestFocus();
            return false;
        }

        // Address can be empty
        return true;
    }

    private void updateUserProfile() {
        setLoading(true);

        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Create JSON body
        JsonObject userBody = new JsonObject();
        userBody.addProperty("username", username);
        userBody.addProperty("email", email);
        userBody.addProperty("phoneNumber", phone);
        userBody.addProperty("address", address);
        userBody.addProperty("role", userRole);

        // Call API
        UserApiService.getInstance()
                .updateUser(userId, userBody)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        setLoading(false);

                        if (response.isSuccessful()) {
                            Toast.makeText(EditProfileActivity.this,
                                    "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();

                            // Return result to previous activity
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("updated", true);
                            setResult(RESULT_OK, resultIntent);
                            finish();

                        } else {
                            Log.e(TAG, "Error updating profile: " + response.code());
                            String errorMsg = "Cập nhật thất bại";

                            // Try to get error message from response
                            try {
                                if (response.errorBody() != null) {
                                    String errorBody = response.errorBody().string();
                                    Log.e(TAG, "Error body: " + errorBody);
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error reading error body", e);
                            }

                            if (response.code() == 400) {
                                errorMsg = "Dữ liệu không hợp lệ";
                            } else if (response.code() == 404) {
                                errorMsg = "Không tìm thấy người dùng";
                            } else if (response.code() == 500) {
                                errorMsg = "Lỗi server";
                            }

                            Toast.makeText(EditProfileActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        setLoading(false);
                        Log.e(TAG, "API call failed", t);
                        Toast.makeText(EditProfileActivity.this,
                                "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            btnSave.setEnabled(false);
            btnCancel.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnSave.setEnabled(true);
            btnCancel.setEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        // Check if there are unsaved changes
        if (hasUnsavedChanges()) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có thay đổi chưa được lưu. Bạn có muốn thoát không?")
                    .setPositiveButton("Thoát", (dialog, which) -> {
                        super.onBackPressed();
                    })
                    .setNegativeButton("Ở lại", null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasUnsavedChanges() {
        String currentUsername = etUsername.getText().toString().trim();
        String currentEmail = etEmail.getText().toString().trim();
        String currentPhone = etPhone.getText().toString().trim();
        String currentAddress = etAddress.getText().toString().trim();

        return !currentUsername.equals(originalUsername != null ? originalUsername : "") ||
                !currentEmail.equals(originalEmail != null ? originalEmail : "") ||
                !currentPhone.equals(originalPhone != null ? originalPhone : "") ||
                !currentAddress.equals(originalAddress != null ? originalAddress : "");
    }
}