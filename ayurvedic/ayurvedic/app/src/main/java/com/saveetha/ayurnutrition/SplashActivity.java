package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    Button btnGetStarted;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("ayur_prefs", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);
        boolean hasSeenSplash = prefs.getBoolean("has_seen_splash", false);

        if (isLoggedIn) {
            // User is logged in, skip splash and go directly to Dashboard
            startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            finish();
            return;
        }

        if (hasSeenSplash) {
            // User has seen splash before (logged out), go to Login
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // First time install - show splash screen
        setContentView(R.layout.activity_splash);

        btnGetStarted = findViewById(R.id.btnGetStarted);

        btnGetStarted.setOnClickListener(v -> {
            // Mark that user has seen splash screen
            prefs.edit().putBoolean("has_seen_splash", true).apply();

            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        });
    }
}
