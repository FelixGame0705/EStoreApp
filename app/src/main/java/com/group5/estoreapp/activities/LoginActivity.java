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
import com.group5.estoreapp.activities.MainActivity;
import com.group5.estoreapp.R;
import com.group5.estoreapp.model.User;
import com.group5.estoreapp.services.UserService;
import com.group5.estoreapp.sqlite.UserDbHelper;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    CheckBox cbRemember;
    MaterialButton btnLogin;
    TextView tvRegister;
    UserService userService = new UserService();
    UserDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        dbHelper = new UserDbHelper(this);

        btnLogin.setText("Login");

        btnLogin.setOnClickListener(v -> {
            hideKeyboard();

            String username = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

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

                    JsonObject tokenObj = response.getAsJsonObject("token");
                    String accessToken = tokenObj.get("accessToken").getAsString();
                    String role = tokenObj.get("role").getAsString();



                    // ✅ Giải mã JWT để lấy userId
                    int userId = extractUserIdFromToken(accessToken);

                    // Lưu token vào SharedPreferences
                    SharedPreferences pref = getSharedPreferences("auth", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("accessToken", accessToken);
                    editor.putInt("userId", userId);
                    editor.putString("role", role);
                    Log.d("LoginActivity", "AccessToken: " + accessToken);
                    if (cbRemember.isChecked()) {
                        editor.putString("username", username);
                        editor.putString("password", password);
                    }

                    editor.apply();

                    // ✅ Lấy user chi tiết từ API rồi lưu vào SQLite
                    userService.getUserByUsername(username, new UserService.UserCallback() {
                        @Override
                        public void onSuccess(User user) {
                            dbHelper.clearUser();
                            dbHelper.insertUser(user);

                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(LoginActivity.this, "Không lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private int extractUserIdFromToken(String token) {
        try {
            JWT jwt = new JWT(token);
            String userIdStr = jwt.getClaim("nameid").asString();
            return Integer.parseInt(userIdStr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
