package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class PrakritiAssessmentActivity extends AppCompatActivity {

    EditText etVata, etPitta, etKapha;
    Button btnNext;
    ImageView btnBack, btnEdit;
    TextView tvTitle;
    ProgressBar progressBar;
    int patientId;
    boolean isEditMode = false;
    boolean hasExistingData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prakriti_assessment);

        patientId = getIntent().getIntExtra("patient_id", 0);

        etVata = findViewById(R.id.etVata);
        etPitta = findViewById(R.id.etPitta);
        etKapha = findViewById(R.id.etKapha);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        tvTitle = findViewById(R.id.tvTitle);
        progressBar = findViewById(R.id.progressBar);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        btnNext.setOnClickListener(v -> savePrakriti());

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

        String url = VolleyHelper.GET_PRAKRITI_URL + "?patient_id=" + patientId;
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                try {
                    if (response.getBoolean("success") && response.optBoolean("exists", false)) {
                        hasExistingData = true;
                        JSONObject data = response.getJSONObject("data");

                        // Populate form
                        etVata.setText(String.valueOf(data.optInt("vata", 0)));
                        etPitta.setText(String.valueOf(data.optInt("pitta", 0)));
                        etKapha.setText(String.valueOf(data.optInt("kapha", 0)));

                        // Show edit button and change title
                        if (btnEdit != null)
                            btnEdit.setVisibility(View.VISIBLE);
                        if (tvTitle != null)
                            tvTitle.setText("🧘 Prakriti (View)");
                        btnNext.setText("Update & Continue");

                        // Keep form disabled until edit is clicked
                        setFormEnabled(false);
                    } else {
                        // No existing data, enable form for new entry
                        hasExistingData = false;
                        if (btnEdit != null)
                            btnEdit.setVisibility(View.GONE);
                        if (tvTitle != null)
                            tvTitle.setText("🧘 Prakriti Assessment");
                        btnNext.setText("Next");
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

    private void enableEditMode(boolean enable) {
        isEditMode = enable;
        setFormEnabled(enable);
        if (enable) {
            if (tvTitle != null)
                tvTitle.setText("🧘 Prakriti (Edit)");
            btnNext.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Edit mode enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFormEnabled(boolean enabled) {
        etVata.setEnabled(enabled);
        etPitta.setEnabled(enabled);
        etKapha.setEnabled(enabled);

        if (hasExistingData && !isEditMode) {
            btnNext.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }
    }

    private void savePrakriti() {
        String vataStr = etVata.getText().toString().trim();
        String pittaStr = etPitta.getText().toString().trim();
        String kaphaStr = etKapha.getText().toString().trim();

        if (vataStr.isEmpty() || pittaStr.isEmpty() || kaphaStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int vata = Integer.parseInt(vataStr);
        int pitta = Integer.parseInt(pittaStr);
        int kapha = Integer.parseInt(kaphaStr);

        btnNext.setEnabled(false);

        VolleyHelper.getInstance(this).savePrakriti(patientId, vata, pitta, kapha, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                btnNext.setEnabled(true);
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        Toast.makeText(PrakritiAssessmentActivity.this,
                                hasExistingData ? "Prakriti updated" : "Prakriti saved",
                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PrakritiAssessmentActivity.this, VikritiAssessmentActivity.class);
                        i.putExtra("patient_id", patientId);
                        startActivity(i);
                    } else {
                        Toast.makeText(PrakritiAssessmentActivity.this, "Failed to save", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PrakritiAssessmentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                btnNext.setEnabled(true);
                Toast.makeText(PrakritiAssessmentActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
