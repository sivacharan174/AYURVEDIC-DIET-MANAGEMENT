package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

/**
 * Shows full details of a medical history record when clicked from history list
 */
public class MedicalHistoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history_detail);

        ImageView btnBack = findViewById(R.id.btnBack);
        TextView tvDate = findViewById(R.id.tvDate);

        btnBack.setOnClickListener(v -> finish());

        String date = getIntent().getStringExtra("date");
        String jsonData = getIntent().getStringExtra("json_data");

        tvDate.setText("Recorded: " + (date != null ? date : ""));

        try {
            JSONObject data = new JSONObject(jsonData);

            TextView tvConditions = findViewById(R.id.tvConditions);
            TextView tvMedications = findViewById(R.id.tvMedications);
            TextView tvAllergies = findViewById(R.id.tvAllergies);
            TextView tvFamilyHistory = findViewById(R.id.tvFamilyHistory);
            TextView tvSurgeries = findViewById(R.id.tvSurgeries);

            if (tvConditions != null)
                tvConditions.setText(getVal(data, "conditions"));
            if (tvMedications != null)
                tvMedications.setText(getVal(data, "medications"));
            if (tvAllergies != null)
                tvAllergies.setText(getVal(data, "allergies"));
            if (tvFamilyHistory != null)
                tvFamilyHistory.setText(getVal(data, "family_history"));
            if (tvSurgeries != null)
                tvSurgeries.setText(getVal(data, "surgeries"));
        } catch (Exception e) {
        }
    }

    private String getVal(JSONObject data, String key) {
        String value = data.optString(key, "");
        if (value.isEmpty() || value.equals("null"))
            return "None specified";
        return value;
    }
}
