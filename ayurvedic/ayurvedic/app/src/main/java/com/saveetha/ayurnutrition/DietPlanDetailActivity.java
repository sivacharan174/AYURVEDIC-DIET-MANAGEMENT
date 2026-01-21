package com.saveetha.ayurnutrition;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONObject;

public class DietPlanDetailActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvGoalEmoji, tvGoal, tvPatientName, tvStatus;
    TextView tvDuration, tvCalories, tvDate, tvNotes;

    MaterialCardView cardMorning, cardBreakfast, cardLunch, cardEvening, cardDinner, cardNotes;
    LinearLayout llMorningItems, llBreakfastItems, llLunchItems, llEveningItems, llDinnerItems;

    int dietPlanId, patientId, targetCalories;
    String patientName, goal, duration, notes, mealItems, status, createdAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan_detail);

        // Get intent data
        dietPlanId = getIntent().getIntExtra("diet_plan_id", 0);
        patientId = getIntent().getIntExtra("patient_id", 0);
        patientName = getIntent().getStringExtra("patient_name");
        goal = getIntent().getStringExtra("goal");
        duration = getIntent().getStringExtra("duration");
        targetCalories = getIntent().getIntExtra("target_calories", 0);
        notes = getIntent().getStringExtra("notes");
        mealItems = getIntent().getStringExtra("meal_items");
        status = getIntent().getStringExtra("status");
        createdAt = getIntent().getStringExtra("created_at");

        // Bind views
        btnBack = findViewById(R.id.btnBack);
        tvGoalEmoji = findViewById(R.id.tvGoalEmoji);
        tvGoal = findViewById(R.id.tvGoal);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvStatus = findViewById(R.id.tvStatus);
        tvDuration = findViewById(R.id.tvDuration);
        tvCalories = findViewById(R.id.tvCalories);
        tvDate = findViewById(R.id.tvDate);
        tvNotes = findViewById(R.id.tvNotes);

        cardMorning = findViewById(R.id.cardMorning);
        cardBreakfast = findViewById(R.id.cardBreakfast);
        cardLunch = findViewById(R.id.cardLunch);
        cardEvening = findViewById(R.id.cardEvening);
        cardDinner = findViewById(R.id.cardDinner);
        cardNotes = findViewById(R.id.cardNotes);

        llMorningItems = findViewById(R.id.llMorningItems);
        llBreakfastItems = findViewById(R.id.llBreakfastItems);
        llLunchItems = findViewById(R.id.llLunchItems);
        llEveningItems = findViewById(R.id.llEveningItems);
        llDinnerItems = findViewById(R.id.llDinnerItems);

        // Hide download button - PDF is in Settings
        findViewById(R.id.btnDownloadPdf).setVisibility(View.GONE);
        findViewById(R.id.btnDownload).setVisibility(View.GONE);

        btnBack.setOnClickListener(v -> finish());

        // Set data
        populateData();
        parseMealItems();
    }

    private void populateData() {
        // Goal emoji and text
        tvGoalEmoji.setText(getGoalEmoji(goal));
        tvGoal.setText(goal != null ? goal + " Diet" : "Diet Plan");
        tvPatientName.setText("for " + (patientName != null ? patientName : "Unknown"));

        // Duration, Calories, Date
        tvDuration.setText(duration != null ? duration : "N/A");
        tvCalories.setText(targetCalories > 0 ? targetCalories + " kcal" : "Not set");
        tvDate.setText(formatDate(createdAt));

        // Status
        if (status != null && status.equalsIgnoreCase("active")) {
            tvStatus.setText("Active");
            tvStatus.setTextColor(Color.parseColor("#00E676"));
            tvStatus.setBackgroundResource(R.drawable.badge_bg_green);
        } else {
            tvStatus.setText("Completed");
            tvStatus.setTextColor(Color.parseColor("#9E9E9E"));
            tvStatus.setBackgroundResource(R.drawable.badge_bg_gray);
        }

        // Notes
        if (notes != null && !notes.isEmpty() && !notes.equals("null")) {
            cardNotes.setVisibility(View.VISIBLE);
            tvNotes.setText(notes);
        }
    }

    private void parseMealItems() {
        android.util.Log.d("DietPlanDetail", "parseMealItems called, mealItems: " + mealItems);

        if (mealItems == null || mealItems.isEmpty() || mealItems.equals("{}") || mealItems.equals("null")) {
            android.util.Log.d("DietPlanDetail", "No meal items to parse");
            return;
        }

        try {
            JSONObject json = new JSONObject(mealItems);
            android.util.Log.d("DietPlanDetail", "Parsed JSON keys: " + json.keys().toString());

            // Parse each meal - try different key formats
            parseMeal(json, "Morning", cardMorning, llMorningItems);
            parseMeal(json, "morning", cardMorning, llMorningItems);

            parseMeal(json, "Breakfast", cardBreakfast, llBreakfastItems);
            parseMeal(json, "breakfast", cardBreakfast, llBreakfastItems);

            parseMeal(json, "Lunch", cardLunch, llLunchItems);
            parseMeal(json, "lunch", cardLunch, llLunchItems);

            parseMeal(json, "Evening", cardEvening, llEveningItems);
            parseMeal(json, "evening", cardEvening, llEveningItems);

            parseMeal(json, "Dinner", cardDinner, llDinnerItems);
            parseMeal(json, "dinner", cardDinner, llDinnerItems);

        } catch (Exception e) {
            android.util.Log.e("DietPlanDetail", "Error parsing meal items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void parseMeal(JSONObject json, String mealKey, MaterialCardView card, LinearLayout container) {
        try {
            JSONArray items = json.optJSONArray(mealKey);
            android.util.Log.d("DietPlanDetail",
                    "parseMeal for " + mealKey + ", items: " + (items != null ? items.length() : "null"));

            if (items != null && items.length() > 0) {
                card.setVisibility(View.VISIBLE);
                container.removeAllViews();

                for (int i = 0; i < items.length(); i++) {
                    JSONObject food = items.getJSONObject(i);
                    String name = food.optString("name", food.optString("title", "Unknown Food"));
                    int calories = food.optInt("calories", 0);
                    String rasa = food.optString("rasa", "");

                    android.util.Log.d("DietPlanDetail", "Adding food: " + name + ", " + calories + " kcal");
                    addFoodItem(container, name, calories, rasa);
                }
            }
        } catch (Exception e) {
            android.util.Log.e("DietPlanDetail", "Error in parseMeal for " + mealKey + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addFoodItem(LinearLayout container, String name, int calories, String rasa) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.HORIZONTAL);
        itemLayout.setPadding(0, 8, 0, 8);

        // Bullet point
        TextView bullet = new TextView(this);
        bullet.setText("•  ");
        bullet.setTextColor(Color.parseColor("#00E676"));
        bullet.setTextSize(14);
        itemLayout.addView(bullet);

        // Food name
        TextView tvName = new TextView(this);
        tvName.setText(name);
        tvName.setTextColor(Color.parseColor("#FFFFFF"));
        tvName.setTextSize(14);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        tvName.setLayoutParams(nameParams);
        itemLayout.addView(tvName);

        // Calories
        TextView tvCal = new TextView(this);
        tvCal.setText(calories + " kcal");
        tvCal.setTextColor(Color.parseColor("#B8FFDD"));
        tvCal.setTextSize(12);
        itemLayout.addView(tvCal);

        container.addView(itemLayout);
    }

    private String getGoalEmoji(String goal) {
        if (goal == null)
            return "🥗";
        switch (goal.toLowerCase()) {
            case "weight loss":
                return "⚖️";
            case "weight gain":
                return "💪";
            case "maintenance":
                return "✅";
            case "dosha balancing":
                return "☯️";
            case "therapeutic":
                return "💊";
            default:
                return "🥗";
        }
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "Unknown";
        }
        try {
            String[] parts = dateStr.split(" ")[0].split("-");
            String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
            int month = Integer.parseInt(parts[1]) - 1;
            int day = Integer.parseInt(parts[2]);
            return months[month] + " " + day + ", " + parts[0];
        } catch (Exception e) {
            return dateStr;
        }
    }
}
