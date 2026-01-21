package com.saveetha.ayurnutrition;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddMedicalRecordActivity extends AppCompatActivity {

    EditText etVisitDate, etReason, etDiagnosis, etPrescription, etNotes;
    Spinner spinnerVisitType;
    MaterialButton btnSave;
    ImageView btnBack;

    int patientId;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_record);

        // Bind views
        etVisitDate = findViewById(R.id.etVisitDate);
        etReason = findViewById(R.id.etReason);
        etDiagnosis = findViewById(R.id.etDiagnosis);
        etPrescription = findViewById(R.id.etPrescription);
        etNotes = findViewById(R.id.etNotes);
        spinnerVisitType = findViewById(R.id.spinnerVisitType);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        // Get patient ID
        patientId = getIntent().getIntExtra("patient_id", 0);

        // Setup visit type spinner
        String[] visitTypes = { "Initial", "Follow-up", "Emergency", "Routine Check-up" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, visitTypes);
        spinnerVisitType.setAdapter(adapter);
        spinnerVisitType.setSelection(1); // Default to Follow-up

        // Date picker
        etVisitDate.setOnClickListener(v -> showDatePicker());

        // Set today's date as default
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etVisitDate.setText(sdf.format(calendar.getTime()));

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Save button
        btnSave.setOnClickListener(v -> saveRecord());
    }

    private void showDatePicker() {
        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            etVisitDate.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    private void saveRecord() {
        String visitDate = etVisitDate.getText().toString().trim();
        String visitType = spinnerVisitType.getSelectedItem().toString();
        String reason = etReason.getText().toString().trim();
        String diagnosis = etDiagnosis.getText().toString().trim();
        String prescription = etPrescription.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (visitDate.isEmpty()) {
            Toast.makeText(this, "Please select a visit date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (reason.isEmpty()) {
            Toast.makeText(this, "Please enter reason for visit", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSave.setEnabled(false);
        btnSave.setText("Saving...");

        StringRequest request = new StringRequest(Request.Method.POST,
                VolleyHelper.SAVE_MEDICAL_RECORD_URL,
                response -> {
                    btnSave.setEnabled(true);
                    btnSave.setText("💾 Save Visit Record");
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            Toast.makeText(this, "Visit record saved!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, json.optString("message", "Failed to save"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    btnSave.setEnabled(true);
                    btnSave.setText("💾 Save Visit Record");
                    Toast.makeText(this, "Network error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("patient_id", String.valueOf(patientId));
                params.put("visit_date", visitDate);
                params.put("visit_type", visitType);
                params.put("reason", reason);
                params.put("diagnosis", diagnosis);
                params.put("prescription", prescription);
                params.put("notes", notes);
                return params;
            }
        };

        VolleyHelper.getInstance(this).addToRequestQueue(request);
    }
}
