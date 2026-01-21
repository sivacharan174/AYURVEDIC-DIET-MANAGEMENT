package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

/**
 * Shows full details of a dietary habits record when clicked from history list
 */
public class DietaryHabitsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietary_habits_detail);

        ImageView btnBack = findViewById(R.id.btnBack);
        TextView tvDate = findViewById(R.id.tvDate);

        btnBack.setOnClickListener(v -> finish());

        String date = getIntent().getStringExtra("date");
        String jsonData = getIntent().getStringExtra("json_data");

        tvDate.setText("Recorded: " + (date != null ? date : ""));

        try {
            JSONObject data = new JSONObject(jsonData);

            TextView tvDietType = findViewById(R.id.tvDietType);
            TextView tvMealTimings = findViewById(R.id.tvMealTimings);
            TextView tvFoodPreferences = findViewById(R.id.tvFoodPreferences);
            TextView tvEatingHabits = findViewById(R.id.tvEatingHabits);
            TextView tvMealCount = findViewById(R.id.tvMealCount);

            if (tvDietType != null)
                tvDietType.setText(getVal(data, "diet_type"));
            if (tvMealTimings != null)
                tvMealTimings.setText(getVal(data, "meal_timings"));
            if (tvFoodPreferences != null)
                tvFoodPreferences.setText(getVal(data, "food_preferences"));
            if (tvEatingHabits != null)
                tvEatingHabits.setText(getVal(data, "eating_habits"));
            if (tvMealCount != null)
                tvMealCount.setText(getVal(data, "meal_count") + " meals/day");
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
