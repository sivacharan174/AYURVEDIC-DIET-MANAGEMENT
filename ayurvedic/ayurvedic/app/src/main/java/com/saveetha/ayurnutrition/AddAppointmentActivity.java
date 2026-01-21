package com.saveetha.ayurnutrition;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddAppointmentActivity extends AppCompatActivity {

    ImageView btnBack;
    Spinner spPatient;
    EditText etDate, etTime, etNotes;
    RadioGroup rgPurpose;
    Button btnSchedule;

    List<Patient> patients = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    // Pre-selected patient (if coming from patient profile)
    int preSelectedPatientId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        // Get pre-selected patient if any
        preSelectedPatientId = getIntent().getIntExtra("patient_id", -1);

        // Bind views
        btnBack = findViewById(R.id.btnBack);
        spPatient = findViewById(R.id.spPatient);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etNotes = findViewById(R.id.etNotes);
        rgPurpose = findViewById(R.id.rgPurpose);
        btnSchedule = findViewById(R.id.btnSchedule);

        btnBack.setOnClickListener(v -> finish());

        // Date picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Time picker
        etTime.setOnClickListener(v -> showTimePicker());

        // Schedule button
        btnSchedule.setOnClickListener(v -> scheduleAppointment());

        // Load patients
        loadPatients();
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            c.set(year, month, day);
            etDate.setText(dateFormat.format(c.getTime()));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hour, minute) -> {
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            etTime.setText(timeFormat.format(c.getTime()));
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
    }

    private void loadPatients() {
        VolleyHelper.getInstance(this).getPatients(new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    patients.clear();
                    JSONArray arr = response.optJSONArray("data");
                    if (arr == null)
                        arr = response.optJSONArray("patients");

                    List<String> names = new ArrayList<>();
                    int preSelectedIndex = 0;

                    if (arr != null) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            Patient p = new Patient();
                            p.id = obj.optInt("id");
                            p.firstName = obj.optString("first_name");
                            p.lastName = obj.optString("last_name");
                            patients.add(p);
                            names.add(p.getFullName());

                            // Check for pre-selected patient
                            if (p.id == preSelectedPatientId) {
                                preSelectedIndex = i;
                            }
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            AddAppointmentActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            names);
                    spPatient.setAdapter(adapter);

                    // Set pre-selected patient
                    if (preSelectedPatientId > 0 && preSelectedIndex < names.size()) {
                        spPatient.setSelection(preSelectedIndex);
                    }

                } catch (Exception e) {
                    Toast.makeText(AddAppointmentActivity.this, "Error loading patients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(AddAppointmentActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getSelectedPurpose() {
        int id = rgPurpose.getCheckedRadioButtonId();
        if (id == R.id.rbConsultation)
            return "Initial Consultation";
        if (id == R.id.rbFollowup)
            return "Follow-up Visit";
        if (id == R.id.rbDietReview)
            return "Diet Plan Review";
        if (id == R.id.rbAssessment)
            return "Dosha Assessment";
        return "Consultation";
    }

    private void scheduleAppointment() {
        // Validate
        if (patients.isEmpty() || spPatient.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Please select a patient", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (time.isEmpty()) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }

        Patient selectedPatient = patients.get(spPatient.getSelectedItemPosition());

        btnSchedule.setEnabled(false);
        btnSchedule.setText("Scheduling...");

        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(selectedPatient.id));
        params.put("appointment_date", date);
        params.put("appointment_time", time);
        params.put("purpose", getSelectedPurpose());
        params.put("notes", etNotes.getText().toString().trim());
        params.put("status", "scheduled");

        VolleyHelper.getInstance(this).postRequest(
                VolleyHelper.SAVE_APPOINTMENT_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnSchedule.setEnabled(true);
                        btnSchedule.setText("Schedule Appointment");

                        try {
                            if (response.optBoolean("success", false)) {
                                Toast.makeText(AddAppointmentActivity.this,
                                        "✅ Appointment scheduled successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddAppointmentActivity.this,
                                        response.optString("message", "Failed to schedule"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(AddAppointmentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnSchedule.setEnabled(true);
                        btnSchedule.setText("Schedule Appointment");
                        Toast.makeText(AddAppointmentActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
