package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateDietActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvPatientName;
    Spinner spDuration;
    RadioGroup rgGoal;
    EditText etCalories, etNotes;
    Button btnContinue;
    int patientId;
    String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_diet);

        patientId = getIntent().getIntExtra("patient_id", 0);
        patientName = getIntent().getStringExtra("patient_name");

        btnBack = findViewById(R.id.btnBack);
        tvPatientName = findViewById(R.id.tvPatientName);
        spDuration = findViewById(R.id.spDuration);
        rgGoal = findViewById(R.id.rgGoal);
        etCalories = findViewById(R.id.etCalories);
        etNotes = findViewById(R.id.etNotes);
        btnContinue = findViewById(R.id.btnContinue);

        tvPatientName.setText("Patient: " + (patientName != null ? patientName : "Unknown"));

        // Setup duration spinner
        String[] durations = {"1 Week", "2 Weeks", "1 Month", "3 Months", "6 Months"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, durations);
        spDuration.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        btnContinue.setOnClickListener(v -> {
            String goal = getSelectedGoal();
            if (goal.isEmpty()) {
                Toast.makeText(this, "Please select a diet goal", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, MealStructureActivity.class);
            intent.putExtra("patient_id", patientId);
            intent.putExtra("patient_name", patientName);
            intent.putExtra("duration", spDuration.getSelectedItem().toString());
            intent.putExtra("goal", goal);
            intent.putExtra("calories", etCalories.getText().toString().trim());
            intent.putExtra("notes", etNotes.getText().toString().trim());
            startActivity(intent);
        });
    }

    private String getSelectedGoal() {
        int id = rgGoal.getCheckedRadioButtonId();
        if (id == R.id.rbWeightLoss) return "Weight Loss";
        if (id == R.id.rbWeightGain) return "Weight Gain";
        if (id == R.id.rbMaintenance) return "Maintenance";
        if (id == R.id.rbDosha) return "Dosha Balancing";
        if (id == R.id.rbTherapeutic) return "Therapeutic";
        return "";
    }
}
