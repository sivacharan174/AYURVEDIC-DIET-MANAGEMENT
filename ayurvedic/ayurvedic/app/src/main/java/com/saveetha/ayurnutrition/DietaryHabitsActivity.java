package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DietaryHabitsActivity extends AppCompatActivity {

    ImageView btnBack, btnEdit;
    TextView tvTitle;
    RadioGroup rgDietType;
    EditText etBreakfastTime, etLunchTime, etDinnerTime;
    CheckBox cbSpicy, cbSweet, cbOily, cbRaw;
    CheckBox cbSkipMeals, cbLateNight, cbFastEating, cbSnacking;
    Button btnSave;
    ProgressBar progressBar;
    int patientId;
    boolean isEditMode = false;
    boolean hasExistingData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietary_habits);

        patientId = getIntent().getIntExtra("patient_id", 0);

        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        tvTitle = findViewById(R.id.tvTitle);
        rgDietType = findViewById(R.id.rgDietType);
        etBreakfastTime = findViewById(R.id.etBreakfastTime);
        etLunchTime = findViewById(R.id.etLunchTime);
        etDinnerTime = findViewById(R.id.etDinnerTime);
        cbSpicy = findViewById(R.id.cbSpicy);
        cbSweet = findViewById(R.id.cbSweet);
        cbOily = findViewById(R.id.cbOily);
        cbRaw = findViewById(R.id.cbRaw);
        cbSkipMeals = findViewById(R.id.cbSkipMeals);
        cbLateNight = findViewById(R.id.cbLateNight);
        cbFastEating = findViewById(R.id.cbFastEating);
        cbSnacking = findViewById(R.id.cbSnacking);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveDietaryHabits());

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

        String url = VolleyHelper.GET_DIETARY_HABITS_URL + "?patient_id=" + patientId;
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
                            tvTitle.setText("Dietary Habits (View)");
                        btnSave.setText("Update Dietary Habits");
                        setFormEnabled(false);
                    } else {
                        hasExistingData = false;
                        if (btnEdit != null)
                            btnEdit.setVisibility(View.GONE);
                        if (tvTitle != null)
                            tvTitle.setText("Dietary Habits");
                        btnSave.setText("Save Dietary Habits");
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
            // Diet Type
            String dietType = data.optString("diet_type", "");
            switch (dietType) {
                case "Vegetarian":
                    rgDietType.check(R.id.rbVeg);
                    break;
                case "Non-Vegetarian":
                    rgDietType.check(R.id.rbNonVeg);
                    break;
                case "Eggetarian":
                    rgDietType.check(R.id.rbEggetarian);
                    break;
                case "Vegan":
                    rgDietType.check(R.id.rbVegan);
                    break;
            }

            // Meal Timings - parse from saved format
            String mealTimings = data.optString("meal_timings", "");
            if (!mealTimings.isEmpty()) {
                String[] parts = mealTimings.split(", ");
                for (String part : parts) {
                    if (part.startsWith("Breakfast: ")) {
                        etBreakfastTime.setText(part.replace("Breakfast: ", ""));
                    } else if (part.startsWith("Lunch: ")) {
                        etLunchTime.setText(part.replace("Lunch: ", ""));
                    } else if (part.startsWith("Dinner: ")) {
                        etDinnerTime.setText(part.replace("Dinner: ", ""));
                    }
                }
            }

            // Food Preferences
            String foodPrefs = data.optString("food_preferences", "").toLowerCase();
            cbSpicy.setChecked(foodPrefs.contains("spicy"));
            cbSweet.setChecked(foodPrefs.contains("sweet"));
            cbOily.setChecked(foodPrefs.contains("oily"));
            cbRaw.setChecked(foodPrefs.contains("raw"));

            // Eating Habits
            String habits = data.optString("eating_habits", "").toLowerCase();
            cbSkipMeals.setChecked(habits.contains("skip"));
            cbLateNight.setChecked(habits.contains("late"));
            cbFastEating.setChecked(habits.contains("quick") || habits.contains("fast"));
            cbSnacking.setChecked(habits.contains("snack"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableEditMode(boolean enable) {
        isEditMode = enable;
        setFormEnabled(enable);
        if (enable) {
            if (tvTitle != null)
                tvTitle.setText("Dietary Habits (Edit)");
            btnSave.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Edit mode enabled", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFormEnabled(boolean enabled) {
        // Enable/disable radio group
        for (int i = 0; i < rgDietType.getChildCount(); i++) {
            rgDietType.getChildAt(i).setEnabled(enabled);
        }

        etBreakfastTime.setEnabled(enabled);
        etLunchTime.setEnabled(enabled);
        etDinnerTime.setEnabled(enabled);

        cbSpicy.setEnabled(enabled);
        cbSweet.setEnabled(enabled);
        cbOily.setEnabled(enabled);
        cbRaw.setEnabled(enabled);
        cbSkipMeals.setEnabled(enabled);
        cbLateNight.setEnabled(enabled);
        cbFastEating.setEnabled(enabled);
        cbSnacking.setEnabled(enabled);

        if (hasExistingData && !isEditMode) {
            btnSave.setVisibility(View.GONE);
        } else {
            btnSave.setVisibility(View.VISIBLE);
        }
    }

    private void saveDietaryHabits() {
        String dietType = "";
        int selectedId = rgDietType.getCheckedRadioButtonId();
        if (selectedId == R.id.rbVeg)
            dietType = "Vegetarian";
        else if (selectedId == R.id.rbNonVeg)
            dietType = "Non-Vegetarian";
        else if (selectedId == R.id.rbEggetarian)
            dietType = "Eggetarian";
        else if (selectedId == R.id.rbVegan)
            dietType = "Vegan";

        StringBuilder preferences = new StringBuilder();
        if (cbSpicy.isChecked())
            preferences.append("Spicy, ");
        if (cbSweet.isChecked())
            preferences.append("Sweet, ");
        if (cbOily.isChecked())
            preferences.append("Oily, ");
        if (cbRaw.isChecked())
            preferences.append("Raw, ");

        StringBuilder habits = new StringBuilder();
        if (cbSkipMeals.isChecked())
            habits.append("Skips meals, ");
        if (cbLateNight.isChecked())
            habits.append("Late night eating, ");
        if (cbFastEating.isChecked())
            habits.append("Eats quickly, ");
        if (cbSnacking.isChecked())
            habits.append("Frequent snacking, ");

        String mealTimings = "Breakfast: " + etBreakfastTime.getText().toString() +
                ", Lunch: " + etLunchTime.getText().toString() +
                ", Dinner: " + etDinnerTime.getText().toString();

        btnSave.setEnabled(false);

        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("diet_type", dietType);
        params.put("meal_timings", mealTimings);
        params.put("food_preferences", preferences.toString());
        params.put("eating_habits", habits.toString());

        VolleyHelper.getInstance(this).postRequest(
                VolleyHelper.SAVE_DIETARY_HABITS_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnSave.setEnabled(true);
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(DietaryHabitsActivity.this,
                                        hasExistingData ? "Dietary habits updated" : "Dietary habits saved",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(DietaryHabitsActivity.this,
                                        response.optString("message", "Failed"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(DietaryHabitsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnSave.setEnabled(true);
                        Toast.makeText(DietaryHabitsActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
