package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MedicalRecordDetailActivity extends AppCompatActivity {

    TextView tvVisitDate, tvVisitType, tvReason, tvDiagnosis, tvPrescription, tvNotes;
    ImageView btnBack, btnEdit;

    int recordId;
    int patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record_detail);

        // Bind views
        tvVisitDate = findViewById(R.id.tvVisitDate);
        tvVisitType = findViewById(R.id.tvVisitType);
        tvReason = findViewById(R.id.tvReason);
        tvDiagnosis = findViewById(R.id.tvDiagnosis);
        tvPrescription = findViewById(R.id.tvPrescription);
        tvNotes = findViewById(R.id.tvNotes);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);

        // Get data from intent
        Intent i = getIntent();
        recordId = i.getIntExtra("record_id", 0);
        patientId = i.getIntExtra("patient_id", 0);

        // Get record details from intent extras
        String visitDate = i.getStringExtra("visit_date");
        String visitType = i.getStringExtra("visit_type");
        String reason = i.getStringExtra("reason");
        String diagnosis = i.getStringExtra("diagnosis");
        String prescription = i.getStringExtra("prescription");
        String notes = i.getStringExtra("notes");

        // Display the data
        tvVisitDate.setText(visitDate != null ? visitDate : "N/A");
        tvVisitType.setText(visitType != null ? visitType + " Visit" : "Visit");
        tvReason.setText(reason != null ? reason : "Not specified");
        tvDiagnosis.setText(diagnosis != null ? diagnosis : "Not specified");
        tvPrescription.setText(prescription != null ? prescription : "Not specified");
        tvNotes.setText(notes != null ? notes : "No notes available");

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Edit button
        btnEdit.setOnClickListener(v -> {
            Toast.makeText(this, "Edit record feature coming soon", Toast.LENGTH_SHORT).show();
        });
    }
}
