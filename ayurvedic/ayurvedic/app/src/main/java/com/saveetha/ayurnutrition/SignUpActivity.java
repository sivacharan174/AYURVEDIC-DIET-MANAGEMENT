package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    EditText etName, etEmail, etPhone, etPassword, etClinic;
    Button btnSignup;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etClinic = findViewById(R.id.etClinic);
        btnSignup = findViewById(R.id.btnSignup);
        tvLogin = findViewById(R.id.tvLogin);

        // Signup button
        btnSignup.setOnClickListener(v -> signup());

        // Login link - go to login screen
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void signup() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String clinic = etClinic.getText().toString().trim();

        // Validate input
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Phone is required");
            etPhone.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (clinic.isEmpty()) {
            etClinic.setError("Clinic name is required");
            etClinic.requestFocus();
            return;
        }

        // Disable button to prevent double clicks
        btnSignup.setEnabled(false);

        VolleyHelper.getInstance(this).signup(name, email, phone, password, clinic, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                btnSignup.setEnabled(true);
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        int userId = response.optInt("user_id", 0);
                        Toast.makeText(SignUpActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUpActivity.this, ClinicSetupActivity.class);
                        i.putExtra("user_id", userId);
                        startActivity(i);
                        finish();
                    } else {
                        String message = response.optString("message", "Signup failed");
                        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(SignUpActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                btnSignup.setEnabled(true);
                Toast.makeText(SignUpActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
