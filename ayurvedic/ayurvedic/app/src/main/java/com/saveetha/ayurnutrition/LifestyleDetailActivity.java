package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

/**
 * Shows full details of a lifestyle record when clicked from history list
 */
public class LifestyleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifestyle_detail);

        ImageView btnBack = findViewById(R.id.btnBack);
        TextView tvDate = findViewById(R.id.tvDate);

        btnBack.setOnClickListener(v -> finish());

        String date = getIntent().getStringExtra("date");
        String jsonData = getIntent().getStringExtra("json_data");

        tvDate.setText("Recorded: " + (date != null ? date : ""));

        try {
            JSONObject data = new JSONObject(jsonData);

            TextView tvSleepQuality = findViewById(R.id.tvSleepQuality);
            TextView tvSleepHours = findViewById(R.id.tvSleepHours);
            TextView tvBowelMovement = findViewById(R.id.tvBowelMovement);
            TextView tvActivityLevel = findViewById(R.id.tvActivityLevel);
            TextView tvExerciseType = findViewById(R.id.tvExerciseType);
            TextView tvStressLevel = findViewById(R.id.tvStressLevel);
            TextView tvWaterIntake = findViewById(R.id.tvWaterIntake);

            if (tvSleepQuality != null)
                tvSleepQuality.setText(getVal(data, "sleep_quality"));
            if (tvSleepHours != null)
                tvSleepHours.setText(getVal(data, "sleep_hours") + " hours");
            if (tvBowelMovement != null)
                tvBowelMovement.setText(getVal(data, "bowel_movement"));
            if (tvActivityLevel != null)
                tvActivityLevel.setText(getVal(data, "activity_level"));
            if (tvExerciseType != null)
                tvExerciseType.setText(getVal(data, "exercise_type"));
            if (tvStressLevel != null)
                tvStressLevel.setText(getVal(data, "stress_level"));
            if (tvWaterIntake != null)
                tvWaterIntake.setText(getVal(data, "water_intake") + " glasses/day");
        } catch (Exception e) {
        }
    }

    private String getVal(JSONObject data, String key) {
        String value = data.optString(key, "");
        if (value.isEmpty() || value.equals("null"))
            return "Not specified";
        return value;
    }
}
