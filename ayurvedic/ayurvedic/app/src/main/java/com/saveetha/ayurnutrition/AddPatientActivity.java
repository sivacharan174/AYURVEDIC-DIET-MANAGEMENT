package com.saveetha.ayurnutrition;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.Calendar;

public class AddPatientActivity extends AppCompatActivity {

    EditText etFirstName, etLastName, etDob, etEmail, etPhone, etAddress, etRegDate, etFollowUp1, etFollowUp2;
    Spinner spGender;
    Button btnAddPatient;
    ImageView btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etDob = findViewById(R.id.etDob);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etRegDate = findViewById(R.id.etRegDate);
        spGender = findViewById(R.id.spGender);
        etFollowUp1 = findViewById(R.id.etFollowUp1);
        etFollowUp2 = findViewById(R.id.etFollowUp2);
        btnAddPatient = findViewById(R.id.btnAddPatient);
        btnClose = findViewById(R.id.btnClose);

        // Setup gender spinner
        ArrayAdapter<String> genderAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        new String[]{"Male", "Female", "Other"});
        spGender.setAdapter(genderAdapter);

        // Setup back button
        btnClose.setOnClickListener(v -> finish());

        // Setup date pickers
        etDob.setOnClickListener(v -> showDatePicker(etDob));
        etRegDate.setOnClickListener(v -> showDatePicker(etRegDate));

        btnAddPatient.setOnClickListener(v -> addPatient());
    }

    private void showDatePicker(EditText target) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String date = String.format("%04d-%02d-%02d", year, month + 1, day);
            target.setText(date);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void addPatient() {
        // Validate required fields
        if (etFirstName.getText().toString().trim().isEmpty()) {
            etFirstName.setError("First name is required");
            etFirstName.requestFocus();
            return;
        }

        if (etLastName.getText().toString().trim().isEmpty()) {
            etLastName.setError("Last name is required");
            etLastName.requestFocus();
            return;
        }

        btnAddPatient.setEnabled(false);

        VolleyHelper.getInstance(this).addPatient(
                etFirstName.getText().toString().trim(),
                etLastName.getText().toString().trim(),
                etDob.getText().toString().trim(),
                spGender.getSelectedItem().toString(),
                etEmail.getText().toString().trim(),
                etPhone.getText().toString().trim(),
                etAddress.getText().toString().trim(),
                etRegDate.getText().toString().trim(),
                etFollowUp1.getText().toString().trim(),
                etFollowUp2.getText().toString().trim(),
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnAddPatient.setEnabled(true);
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(AddPatientActivity.this,
                                        "Patient Added Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                String message = response.optString("message", "Failed to add patient");
                                Toast.makeText(AddPatientActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(AddPatientActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnAddPatient.setEnabled(true);
                        Toast.makeText(AddPatientActivity.this,
                                "Network error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
