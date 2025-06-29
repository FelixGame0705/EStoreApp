package com.group5.estoreapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    CheckBox cbRemember;
    MaterialButton btnLogin;
    TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        btnLogin = findViewById(R.id.btn_custom);
        tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setText("Login");

        btnLogin.setOnClickListener(v -> {
            // Xử lý đăng nhập
            Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show();
        });

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
