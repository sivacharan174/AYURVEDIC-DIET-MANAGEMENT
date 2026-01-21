package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MedicalHistoryActivity extends AppCompatActivity {

    ImageView btnBack, btnEdit;
    TextView tvTitle;

    // Conditions
    CheckBox cbDiabetes, cbHypertension, cbThyroid, cbHeartDisease, cbArthritis, cbDigestive;
    EditText etOtherConditions;

    // Medications
    EditText etMedications;

    // Allergies
    CheckBox cbAllergyDairy, cbAllergyGluten, cbAllergyNuts, cbAllergySeafood;
    EditText etOtherAllergies;

    Button btnSave;
    ProgressBar progressBar;
    int patientId;
    boolean isEditMode = false;
    boolean hasExistingData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        patientId = getIntent().getIntExtra("patient_id", 0);

        // Bind views
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        tvTitle = findViewById(R.id.tvTitle);

        cbDiabetes = findViewById(R.id.cbDiabetes);
        cbHypertension = findViewById(R.id.cbHypertension);
        cbThyroid = findViewById(R.id.cbThyroid);
        cbHeartDisease = findViewById(R.id.cbHeartDisease);
        cbArthritis = findViewById(R.id.cbArthritis);
        cbDigestive = findViewById(R.id.cbDigestive);
        etOtherConditions = findViewById(R.id.etOtherConditions);

        etMedications = findViewById(R.id.etMedications);

        cbAllergyDairy = findViewById(R.id.cbAllergyDairy);
        cbAllergyGluten = findViewById(R.id.cbAllergyGluten);
        cbAllergyNuts = findViewById(R.id.cbAllergyNuts);
        cbAllergySeafood = findViewById(R.id.cbAllergySeafood);
        etOtherAllergies = findViewById(R.id.etOtherAllergies);

        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveMedicalHistory());

        // Edit button click
        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                if (hasExistingData && !isEditMode) {
                    enableEditMode(true);
                }
            });
        }

        // Load existing data
        loadExistingData();
    }

    private void loadExistingData() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
        setFormEnabled(false);

        String url = VolleyHelper.GET_MEDICAL_HISTORY_URL + "?patient_id=" + patientId;
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                try {
                    if (response.getBoolean("success") && response.optBoolean("exists", false)) {
                        hasExistingData = true;
                        JSONObject data = response.getJSONObject("data");
                        populateForm(data);

                        if (btnEdit != null)
                            btnEdit.setVisibility(View.VISIBLE);
                        if (tvTitle != null)
                            tvTitle.setText("Medical History (View)");
                        btnSave.setText("Update Medical History");
                        setFormEnabled(false);
                    } else {
                        hasExistingData = false;
                        if (btnEdit != null)
                            btnEdit.setVisibility(View.GONE);
                        if (tvTitle != null)
                            tvTitle.setText("Medical History");
                        btnSave.setText("Save Medical History");
                        setFormEnabled(true);
                    }
                } catch (Exception e) {
                    setFormEnabled(true);
                }
            }

            @Override
            public void onError(String error) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                setFormEnabled(true);
            }
        });
    }

    private void populateForm(JSONObject data) {
        try {
            // Conditions
            String conditions = data.optString("conditions", "").toLowerCase();
            cbDiabetes.setChecked(conditions.contains("diabetes"));
            cbHypertension.setChecked(conditions.contains("hypertension"));
            cbThyroid.setChecked(conditions.contains("thyroid"));
            cbHeartDisease.setChecked(conditions.contains("heart"));
            cbArthritis.setChecked(conditions.contains("arthritis"));
            cbDigestive.setChecked(conditions.contains("digestive"));

            // Extract other conditions (anything not matching preset checkboxes)
            // For simplicity, we just show the raw conditions string in a hint

            // Medications
            String medications = data.optString("medications", "");
            if (!medications.isEmpty() && !medications.equals("null")) {
                etMedications.setText(medications);
            }

            // Allergies
            String allergies = data.optString("allergies", "").toLowerCase();
            cbAllergyDairy.setChecked(allergies.contains("dairy"));
            cbAllergyGluten.setChecked(allergies.contains("gluten"));
            cbAllergyNuts.setChecked(allergies.contains("nuts"));
            cbAllergySeafood.setChecked(allergies.contains("seafood"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableEditMode(boolean enable) {
        isEditMode = enable;
        setFormEnabled(enable);
        if (enable) {
            if (tvTitle != null)
                tvTitle.setText("Medical History (Edit)");
            btnSave.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Edit mode enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFormEnabled(boolean enabled) {
        cbDiabetes.setEnabled(enabled);
        cbHypertension.setEnabled(enabled);
        cbThyroid.setEnabled(enabled);
        cbHeartDisease.setEnabled(enabled);
        cbArthritis.setEnabled(enabled);
        cbDigestive.setEnabled(enabled);
        etOtherConditions.setEnabled(enabled);

        etMedications.setEnabled(enabled);

        cbAllergyDairy.setEnabled(enabled);
        cbAllergyGluten.setEnabled(enabled);
        cbAllergyNuts.setEnabled(enabled);
        cbAllergySeafood.setEnabled(enabled);
        etOtherAllergies.setEnabled(enabled);

        if (hasExistingData && !isEditMode) {
            btnSave.setVisibility(View.GONE);
        } else {
            btnSave.setVisibility(View.VISIBLE);
        }
    }

    private void saveMedicalHistory() {
        // Build conditions string
        StringBuilder conditions = new StringBuilder();
        if (cbDiabetes.isChecked())
            conditions.append("Diabetes, ");
        if (cbHypertension.isChecked())
            conditions.append("Hypertension, ");
        if (cbThyroid.isChecked())
            conditions.append("Thyroid Disorder, ");
        if (cbHeartDisease.isChecked())
            conditions.append("Heart Disease, ");
        if (cbArthritis.isChecked())
            conditions.append("Arthritis, ");
        if (cbDigestive.isChecked())
            conditions.append("Digestive Disorders, ");
        if (!etOtherConditions.getText().toString().trim().isEmpty()) {
            conditions.append(etOtherConditions.getText().toString().trim());
        }

        // Build allergies string
        StringBuilder allergies = new StringBuilder();
        if (cbAllergyDairy.isChecked())
            allergies.append("Dairy, ");
        if (cbAllergyGluten.isChecked())
            allergies.append("Gluten, ");
        if (cbAllergyNuts.isChecked())
            allergies.append("Nuts, ");
        if (cbAllergySeafood.isChecked())
            allergies.append("Seafood, ");
        if (!etOtherAllergies.getText().toString().trim().isEmpty()) {
            allergies.append(etOtherAllergies.getText().toString().trim());
        }

        btnSave.setEnabled(false);

        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("conditions", conditions.toString());
        params.put("medications", etMedications.getText().toString().trim());
        params.put("allergies", allergies.toString());

        VolleyHelper.getInstance(this).postRequest(
                VolleyHelper.SAVE_MEDICAL_HISTORY_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnSave.setEnabled(true);
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(MedicalHistoryActivity.this,
                                        hasExistingData ? "Medical history updated" : "Medical history saved",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(MedicalHistoryActivity.this,
                                        response.optString("message", "Failed to save"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(MedicalHistoryActivity.this,
                                    "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnSave.setEnabled(true);
                        Toast.makeText(MedicalHistoryActivity.this,
                                error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
