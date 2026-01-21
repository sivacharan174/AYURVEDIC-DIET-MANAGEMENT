package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class ClinicSetupActivity extends AppCompatActivity {

    EditText etClinicName, etClinicType, etPractitioners, etSpecialization;
    Button btnContinue;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_setup);

        userId = getIntent().getIntExtra("user_id", 0);
        if (userId == 0) {
            Toast.makeText(this, "Invalid user session", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etClinicName = findViewById(R.id.etClinicName);
        etClinicType = findViewById(R.id.etClinicType);
        etPractitioners = findViewById(R.id.etPractitioners);
        etSpecialization = findViewById(R.id.etSpecialization);

        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> saveClinic());
    }

    private void saveClinic() {
        // Validate required fields
        if (etClinicName.getText().toString().trim().isEmpty()) {
            etClinicName.setError("Clinic name is required");
            etClinicName.requestFocus();
            return;
        }

        if (etClinicType.getText().toString().trim().isEmpty()) {
            etClinicType.setError("Clinic type is required");
            etClinicType.requestFocus();
            return;
        }

        btnContinue.setEnabled(false);

        VolleyHelper.getInstance(this).addClinic(
                userId,
                etClinicName.getText().toString().trim(),
                etClinicType.getText().toString().trim(),
                etPractitioners.getText().toString().trim(),
                etSpecialization.getText().toString().trim(),
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnContinue.setEnabled(true);
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(ClinicSetupActivity.this,
                                        "Clinic setup complete!", Toast.LENGTH_SHORT).show();
                                // Navigate directly to Dashboard
                                Intent intent = new Intent(ClinicSetupActivity.this, DashboardActivity.class);
                                intent.putExtra("user_id", userId);
                                startActivity(intent);
                                finish();
                            } else {
                                String message = response.optString("message", "Failed to save clinic");
                                Toast.makeText(ClinicSetupActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ClinicSetupActivity.this, "Error parsing response", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnContinue.setEnabled(true);
                        Toast.makeText(ClinicSetupActivity.this,
                                "Network error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
