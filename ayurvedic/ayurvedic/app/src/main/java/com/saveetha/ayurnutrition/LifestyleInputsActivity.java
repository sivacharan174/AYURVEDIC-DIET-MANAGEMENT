package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LifestyleInputsActivity extends AppCompatActivity {

    ImageView btnBack, btnEdit;
    RadioGroup rgSleepQuality, rgBowel, rgActivity, rgStress;
    EditText etSleepHours, etWaterIntake;
    Button btnSave;
    ProgressBar progressBar;
    TextView tvTitle;
    int patientId;
    boolean isEditMode = false;
    boolean hasExistingData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle_inputs);

        patientId = getIntent().getIntExtra("patient_id", 0);

        // Bind views
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        tvTitle = findViewById(R.id.tvTitle);
        rgSleepQuality = findViewById(R.id.rgSleepQuality);
        rgBowel = findViewById(R.id.rgBowel);
        rgActivity = findViewById(R.id.rgActivity);
        rgStress = findViewById(R.id.rgStress);
        etSleepHours = findViewById(R.id.etSleepHours);
        etWaterIntake = findViewById(R.id.etWaterIntake);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveLifestyle());

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

        String url = VolleyHelper.GET_LIFESTYLE_URL + "?patient_id=" + patientId;
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

                        // Show edit button and change title
                        if (btnEdit != null)
                            btnEdit.setVisibility(View.VISIBLE);
                        if (tvTitle != null)
                            tvTitle.setText("Lifestyle (View)");
                        btnSave.setText("Update Lifestyle Info");

                        // Keep form disabled until edit is clicked
                        setFormEnabled(false);
                    } else {
                        // No existing data, enable form for new entry
                        hasExistingData = false;
                        if (btnEdit != null)
                            btnEdit.setVisibility(View.GONE);
                        if (tvTitle != null)
                            tvTitle.setText("Lifestyle Inputs");
                        btnSave.setText("Save Lifestyle Info");
                        setFormEnabled(true);
                    }
                } catch (Exception e) {
                    setFormEnabled(true);
                    Toast.makeText(LifestyleInputsActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
                setFormEnabled(true);
                // Silent fail, allow new entry
            }
        });
    }

    private void populateForm(JSONObject data) {
        try {
            // Sleep Quality
            String sleepQuality = data.optString("sleep_quality", "");
            switch (sleepQuality) {
                case "Good":
                    rgSleepQuality.check(R.id.rbSleepGood);
                    break;
                case "Moderate":
                    rgSleepQuality.check(R.id.rbSleepModerate);
                    break;
                case "Poor":
                    rgSleepQuality.check(R.id.rbSleepPoor);
                    break;
            }

            // Sleep Hours
            String sleepHours = data.optString("sleep_hours", "");
            if (!sleepHours.isEmpty() && !sleepHours.equals("null")) {
                etSleepHours.setText(sleepHours);
            }

            // Bowel Movement
            String bowel = data.optString("bowel_movement", "");
            switch (bowel) {
                case "Regular":
                    rgBowel.check(R.id.rbBowelRegular);
                    break;
                case "Constipation":
                    rgBowel.check(R.id.rbBowelConstipation);
                    break;
                case "Loose motions":
                    rgBowel.check(R.id.rbBowelLoose);
                    break;
                case "Irregular":
                    rgBowel.check(R.id.rbBowelIrregular);
                    break;
            }

            // Water Intake
            String waterIntake = data.optString("water_intake", "");
            if (!waterIntake.isEmpty() && !waterIntake.equals("null")) {
                etWaterIntake.setText(waterIntake);
            }

            // Activity Level
            String activity = data.optString("activity_level", "");
            switch (activity) {
                case "Sedentary":
                    rgActivity.check(R.id.rbSedentary);
                    break;
                case "Light Active":
                    rgActivity.check(R.id.rbLightActive);
                    break;
                case "Moderate Active":
                    rgActivity.check(R.id.rbModerateActive);
                    break;
                case "Very Active":
                    rgActivity.check(R.id.rbVeryActive);
                    break;
            }

            // Stress Level
            String stress = data.optString("stress_level", "");
            switch (stress) {
                case "Low":
                    rgStress.check(R.id.rbStressLow);
                    break;
                case "Medium":
                    rgStress.check(R.id.rbStressMedium);
                    break;
                case "High":
                    rgStress.check(R.id.rbStressHigh);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableEditMode(boolean enable) {
        isEditMode = enable;
        setFormEnabled(enable);
        if (enable) {
            if (tvTitle != null)
                tvTitle.setText("Lifestyle (Edit)");
            btnSave.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Edit mode enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFormEnabled(boolean enabled) {
        // Enable/disable radio groups
        setRadioGroupEnabled(rgSleepQuality, enabled);
        setRadioGroupEnabled(rgBowel, enabled);
        setRadioGroupEnabled(rgActivity, enabled);
        setRadioGroupEnabled(rgStress, enabled);

        // Enable/disable edit texts
        etSleepHours.setEnabled(enabled);
        etWaterIntake.setEnabled(enabled);

        // Show/hide save button based on mode
        if (hasExistingData && !isEditMode) {
            btnSave.setVisibility(View.GONE);
        } else {
            btnSave.setVisibility(View.VISIBLE);
        }
    }

    private void setRadioGroupEnabled(RadioGroup rg, boolean enabled) {
        for (int i = 0; i < rg.getChildCount(); i++) {
            rg.getChildAt(i).setEnabled(enabled);
        }
    }

    private void saveLifestyle() {
        String sleepQuality = "";
        int sleepId = rgSleepQuality.getCheckedRadioButtonId();
        if (sleepId == R.id.rbSleepGood)
            sleepQuality = "Good";
        else if (sleepId == R.id.rbSleepModerate)
            sleepQuality = "Moderate";
        else if (sleepId == R.id.rbSleepPoor)
            sleepQuality = "Poor";

        String bowel = "";
        int bowelId = rgBowel.getCheckedRadioButtonId();
        if (bowelId == R.id.rbBowelRegular)
            bowel = "Regular";
        else if (bowelId == R.id.rbBowelConstipation)
            bowel = "Constipation";
        else if (bowelId == R.id.rbBowelLoose)
            bowel = "Loose motions";
        else if (bowelId == R.id.rbBowelIrregular)
            bowel = "Irregular";

        String activity = "";
        int activityId = rgActivity.getCheckedRadioButtonId();
        if (activityId == R.id.rbSedentary)
            activity = "Sedentary";
        else if (activityId == R.id.rbLightActive)
            activity = "Light Active";
        else if (activityId == R.id.rbModerateActive)
            activity = "Moderate Active";
        else if (activityId == R.id.rbVeryActive)
            activity = "Very Active";

        String stress = "";
        int stressId = rgStress.getCheckedRadioButtonId();
        if (stressId == R.id.rbStressLow)
            stress = "Low";
        else if (stressId == R.id.rbStressMedium)
            stress = "Medium";
        else if (stressId == R.id.rbStressHigh)
            stress = "High";

        btnSave.setEnabled(false);

        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("sleep_quality", sleepQuality);
        params.put("sleep_hours", etSleepHours.getText().toString().trim());
        params.put("bowel_movement", bowel);
        params.put("water_intake", etWaterIntake.getText().toString().trim());
        params.put("activity_level", activity);
        params.put("stress_level", stress);

        VolleyHelper.getInstance(this).postRequest(
                VolleyHelper.SAVE_LIFESTYLE_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnSave.setEnabled(true);
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(LifestyleInputsActivity.this,
                                        hasExistingData ? "Lifestyle info updated" : "Lifestyle info saved",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(LifestyleInputsActivity.this,
                                        response.optString("message", "Failed"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(LifestyleInputsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnSave.setEnabled(true);
                        Toast.makeText(LifestyleInputsActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
