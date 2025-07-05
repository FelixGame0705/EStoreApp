package com.group5.estoreapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.group5.estoreapp.R;

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail, etPassword, etConfirm;
    MaterialButton btnRegister;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirm);
        btnRegister = findViewById(R.id.btn_custom);
        tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setText("Register");

        btnRegister.setOnClickListener(v -> {
            // Xử lý đăng ký
            Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show();
        });

        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Quay lại login
        });
    }
}
