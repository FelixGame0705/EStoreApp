package com.group5.estoreapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.JWT;
import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.group5.estoreapp.R;
import com.group5.estoreapp.services.UserService;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    CheckBox cbRemember;
    MaterialButton btnLogin;
    TextView tvRegister;
    UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences authPref = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences rememberPref = getSharedPreferences("remember", MODE_PRIVATE);

        String token = authPref.getString("accessToken", null);
        boolean rememberMe = rememberPref.getBoolean("rememberMe", false);

        if (token != null && !token.isEmpty() && rememberMe) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);


        // ✅ Load thông tin nếu ghi nhớ trước đó
        if (rememberMe) {
            etEmail.setText(rememberPref.getString("username", ""));
            etPassword.setText(rememberPref.getString("password", ""));
            cbRemember.setChecked(true);
        }


        btnLogin.setText("Login");

        btnLogin.setOnClickListener(v -> {
            String username = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Kiểm tra nhập liệu
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Đang đăng nhập...");
            dialog.setCancelable(false);
            dialog.show();

            userService.login(username, password, new UserService.LoginCallback() {
                public void onSuccess(JsonObject response) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    // ✅ Lấy accessToken từ token object
                    JsonObject tokenObj = response.getAsJsonObject("token");
                    String accessToken = tokenObj.get("accessToken").getAsString();
                    String role = tokenObj.get("role").getAsString();

                    // ✅ Giải mã JWT để lấy userId
                    int userId = extractUserIdFromToken(accessToken);


//                    // Lưu vào SharedPreferences
//                    SharedPreferences pref = getSharedPreferences("auth", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.putString("accessToken", accessToken);
//                    editor.putInt("userId", userId);
//                    editor.putString("role", role);
//                    Log.d("LoginActivity", "AccessToken: " + accessToken);
//                    if (cbRemember.isChecked()) {
//                        editor.putString("username", etEmail.getText().toString().trim());
//                        editor.putString("password", etPassword.getText().toString().trim());
//                        editor.putBoolean("rememberMe", true);
//                    }else {
//                        // ✅ Nếu KHÔNG tick → xoá thông tin cũ
//                        editor.remove("username");
//                        editor.remove("password");
//                        editor.putBoolean("rememberMe", false);
//                    }
//
//                    editor.apply();
// Lưu token
                    SharedPreferences.Editor authEditor = authPref.edit();
                    authEditor.putString("accessToken", accessToken);
                    authEditor.putInt("userId", userId);
                    authEditor.putString("role", role);
                    authEditor.apply();

                    // Ghi nhớ tài khoản nếu cần
                    SharedPreferences.Editor rememberEditor = rememberPref.edit();
                    if (cbRemember.isChecked()) {
                        rememberEditor.putString("username", username);
                        rememberEditor.putString("password", password);
                        rememberEditor.putBoolean("rememberMe", true);
                    } else {
                        rememberEditor.clear();
                    }
                    rememberEditor.apply();



                    // Chuyển qua MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }



                @Override
                public void onError(Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private int extractUserIdFromToken(String token) {
        try {
            JWT jwt = new JWT(token);
            String userIdStr = jwt.getClaim("nameid").asString();
            return Integer.parseInt(userIdStr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // fallback nếu lỗi
        }
    }

}
