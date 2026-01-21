package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class AgniAssessmentActivity extends AppCompatActivity {

    RadioGroup rgAgni;
    Button btnNext;
    ImageView btnBack, btnEdit;
    TextView tvTitle;
    ProgressBar progressBar;
    int patientId;
    String selectedAgni = "";
    boolean isEditMode = false;
    boolean hasExistingData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agni_assessment);

        rgAgni = findViewById(R.id.rgAgni);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        tvTitle = findViewById(R.id.tvTitle);
        progressBar = findViewById(R.id.progressBar);

        patientId = getIntent().getIntExtra("patient_id", 0);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        rgAgni.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSama)
                selectedAgni = "Sama Agni";
            else if (checkedId == R.id.rbTikshna)
                selectedAgni = "Tikshna Agni";
            else if (checkedId == R.id.rbManda)
                selectedAgni = "Manda Agni";
            else if (checkedId == R.id.rbVishama)
                selectedAgni = "Vishama Agni";
        });

        btnNext.setOnClickListener(v -> {
            if (selectedAgni.isEmpty()) {
                Toast.makeText(this, "Please select Agni type", Toast.LENGTH_SHORT).show();
            } else {
                saveAgni();
            }
        });

        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                if (hasExistingData && !isEditMode) {
                    enableEditMode(true);
                }
            });
        }

        loadExistingData();
    }

    private void loadExistingData() {
        if (progressBar != null)
            progressBar.setVisibility(View.VISIBLE);
        setFormEnabled(false);

        String url = VolleyHelper.GET_AGNI_URL + "?patient_id=" + patientId;
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                try {
                    if (response.getBoolean("success") && response.optBoolean("exists", false)) {
                        hasExistingData = true;
                        JSONObject data = response.getJSONObject("data");
                        String agniType = data.optString("agni_type", "");

                        // Set the corresponding radio button
                        selectedAgni = agniType;
                        switch (agniType) {
                            case "Sama Agni":
                                rgAgni.check(R.id.rbSama);
                                break;
                            case "Tikshna Agni":
                                rgAgni.check(R.id.rbTikshna);
                                break;
                            case "Manda Agni":
                                rgAgni.check(R.id.rbManda);
                                break;
                            case "Vishama Agni":
                                rgAgni.check(R.id.rbVishama);
                                break;
                        }

                        if (btnEdit != null)
                            btnEdit.setVisibility(View.VISIBLE);
                        if (tvTitle != null)
                            tvTitle.setText("🔥 Agni (View)");
                        btnNext.setText("Update & Continue");
                        setFormEnabled(false);
                    } else {
                        hasExistingData = false;
                        if (btnEdit != null)
                            btnEdit.setVisibility(View.GONE);
                        if (tvTitle != null)
                            tvTitle.setText("🔥 Agni Assessment");
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
                tvTitle.setText("🔥 Agni (Edit)");
            btnNext.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Edit mode enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFormEnabled(boolean enabled) {
        for (int i = 0; i < rgAgni.getChildCount(); i++) {
            rgAgni.getChildAt(i).setEnabled(enabled);
        }

        if (hasExistingData && !isEditMode) {
            btnNext.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }
    }

    private void saveAgni() {
        btnNext.setEnabled(false);

        VolleyHelper.getInstance(this).saveAgni(patientId, selectedAgni, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                btnNext.setEnabled(true);
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        Toast.makeText(AgniAssessmentActivity.this,
                                hasExistingData ? "Agni updated" : "Agni saved",
                                Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AgniAssessmentActivity.this, DigestiveStrengthActivity.class);
                        i.putExtra("patient_id", patientId);
                        startActivity(i);
                    } else {
                        Toast.makeText(AgniAssessmentActivity.this, "Failed to save Agni", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AgniAssessmentActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                btnNext.setEnabled(true);
                Toast.makeText(AgniAssessmentActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
