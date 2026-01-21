package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealStructureActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_FOOD = 100;

    ImageView btnBack, btnPreview;
    Chip btnMorning, btnBreakfast, btnLunch, btnEvening, btnDinner;
    Button btnAddFood, btnSave, btnSuggestions, btnCheckConflicts;
    TextView tvMealTitle, tvItemCount;
    RecyclerView rvMealItems;

    int patientId;
    String patientName, duration, goal, targetCalories, notes;
    String currentMeal = "Morning";

    // Store meal items for each meal
    Map<String, List<Food>> mealItems = new HashMap<>();
    MealItemAdapter currentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_structure);

        // Get data from previous activity
        patientId = getIntent().getIntExtra("patient_id", 0);
        patientName = getIntent().getStringExtra("patient_name");
        duration = getIntent().getStringExtra("duration");
        goal = getIntent().getStringExtra("goal");
        targetCalories = getIntent().getStringExtra("calories");
        notes = getIntent().getStringExtra("notes");

        // Initialize meal items map
        mealItems.put("Morning", new ArrayList<>());
        mealItems.put("Breakfast", new ArrayList<>());
        mealItems.put("Lunch", new ArrayList<>());
        mealItems.put("Evening", new ArrayList<>());
        mealItems.put("Dinner", new ArrayList<>());

        // Bind views
        btnBack = findViewById(R.id.btnBack);
        btnPreview = findViewById(R.id.btnPreview);
        btnMorning = findViewById(R.id.btnMorning);
        btnBreakfast = findViewById(R.id.btnBreakfast);
        btnLunch = findViewById(R.id.btnLunch);
        btnEvening = findViewById(R.id.btnEvening);
        btnDinner = findViewById(R.id.btnDinner);
        btnAddFood = findViewById(R.id.btnAddFood);
        btnSave = findViewById(R.id.btnSave);
        btnSuggestions = findViewById(R.id.btnSuggestions);
        btnCheckConflicts = findViewById(R.id.btnCheckConflicts);
        tvMealTitle = findViewById(R.id.tvMealTitle);
        rvMealItems = findViewById(R.id.rvMealItems);

        rvMealItems.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        // Meal tab handlers
        btnMorning.setOnClickListener(v -> switchMeal("Morning", "Morning (6-8 AM)"));
        btnBreakfast.setOnClickListener(v -> switchMeal("Breakfast", "Breakfast (8-10 AM)"));
        btnLunch.setOnClickListener(v -> switchMeal("Lunch", "Lunch (12-2 PM)"));
        btnEvening.setOnClickListener(v -> switchMeal("Evening", "Evening Snack (4-6 PM)"));
        btnDinner.setOnClickListener(v -> switchMeal("Dinner", "Dinner (7-9 PM)"));

        // Add Food Button - launches FoodSearchActivity and waits for result
        btnAddFood.setOnClickListener(v -> {
            Intent intent = new Intent(this, FoodSearchActivity.class);
            intent.putExtra("select_mode", true);
            intent.putExtra("meal", currentMeal);
            startActivityForResult(intent, REQUEST_ADD_FOOD);
        });

        btnPreview.setOnClickListener(v -> showDietPreview());

        btnSave.setOnClickListener(v -> saveDietChart());

        // Smart Suggestions Button (Slide 33)
        btnSuggestions.setOnClickListener(v -> {
            Intent intent = new Intent(this, AutoSuggestionsActivity.class);
            intent.putExtra("patient_id", patientId);
            intent.putExtra("patient_name", patientName);
            startActivity(intent);
        });

        // Check Conflicts Button (Slide 34 - Viruddha Ahara)
        btnCheckConflicts.setOnClickListener(v -> {
            // Collect all food IDs from current diet
            StringBuilder foodIds = new StringBuilder();
            for (List<Food> foods : mealItems.values()) {
                for (Food f : foods) {
                    if (foodIds.length() > 0)
                        foodIds.append(",");
                    foodIds.append(f.id);
                }
            }
            if (foodIds.length() == 0) {
                Toast.makeText(this, "Add some foods first to check conflicts", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, FoodConflictCheckActivity.class);
            intent.putExtra("food_ids", foodIds.toString());
            startActivity(intent);
        });

        // Set initial meal
        switchMeal("Morning", "Morning (6-8 AM)");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_FOOD && resultCode == RESULT_OK && data != null) {
            String meal = data.getStringExtra("meal");
            String targetMeal = (meal != null && !meal.isEmpty()) ? meal : currentMeal;

            // Handle multiple foods (new format)
            String foodsJson = data.getStringExtra("selected_foods");
            if (foodsJson != null && !foodsJson.isEmpty()) {
                try {
                    java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<java.util.List<Food>>() {
                    }.getType();
                    java.util.List<Food> foods = new Gson().fromJson(foodsJson, listType);

                    List<Food> items = mealItems.get(targetMeal);
                    if (items == null) {
                        items = new ArrayList<>();
                        mealItems.put(targetMeal, items);
                    }
                    items.addAll(foods);

                    // Refresh the current meal view
                    if (targetMeal.equals(currentMeal)) {
                        updateMealItemsList();
                    }

                    Toast.makeText(this, foods.size() + " foods added to " + targetMeal, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    android.util.Log.e("MealStructure", "Error parsing foods: " + e.getMessage());
                }
                return;
            }

            // Handle single food (backward compatibility)
            String foodJson = data.getStringExtra("selected_food");
            if (foodJson != null && !foodJson.isEmpty()) {
                try {
                    Food food = new Gson().fromJson(foodJson, Food.class);

                    List<Food> items = mealItems.get(targetMeal);
                    if (items == null) {
                        items = new ArrayList<>();
                        mealItems.put(targetMeal, items);
                    }
                    items.add(food);

                    // Refresh the current meal view
                    if (targetMeal.equals(currentMeal)) {
                        updateMealItemsList();
                    }

                    Toast.makeText(this, food.name + " added to " + targetMeal, Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    android.util.Log.e("MealStructure", "Error parsing food: " + e.getMessage());
                }
            }
        }
    }

    private void switchMeal(String meal, String title) {
        currentMeal = meal;
        tvMealTitle.setText(title);
        // Update chip colors
        resetButtonColors();
        int selectedColor = 0xFF00E676; // Green
        android.content.res.ColorStateList selectedColorList = android.content.res.ColorStateList
                .valueOf(selectedColor);
        switch (meal) {
            case "Morning":
                btnMorning.setChipBackgroundColor(selectedColorList);
                btnMorning.setTextColor(0xFF0D2818);
                break;
            case "Breakfast":
                btnBreakfast.setChipBackgroundColor(selectedColorList);
                btnBreakfast.setTextColor(0xFF0D2818);
                break;
            case "Lunch":
                btnLunch.setChipBackgroundColor(selectedColorList);
                btnLunch.setTextColor(0xFF0D2818);
                break;
            case "Evening":
                btnEvening.setChipBackgroundColor(selectedColorList);
                btnEvening.setTextColor(0xFF0D2818);
                break;
            case "Dinner":
                btnDinner.setChipBackgroundColor(selectedColorList);
                btnDinner.setTextColor(0xFF0D2818);
                break;
        }
        // Update RecyclerView with items for this meal
        updateMealItemsList();
    }

    private void updateMealItemsList() {
        List<Food> items = mealItems.get(currentMeal);
        if (items == null)
            items = new ArrayList<>();

        currentAdapter = new MealItemAdapter(this, items, position -> {
            // Remove item from list
            List<Food> currentItems = mealItems.get(currentMeal);
            if (currentItems != null && position < currentItems.size()) {
                currentItems.remove(position);
                currentAdapter.updateItems(currentItems);
                Toast.makeText(this, "Food removed", Toast.LENGTH_SHORT).show();
            }
        });
        rvMealItems.setAdapter(currentAdapter);
    }

    private void resetButtonColors() {
        int defaultColor = 0xFF1A5D4A;
        android.content.res.ColorStateList defaultColorList = android.content.res.ColorStateList.valueOf(defaultColor);
        btnMorning.setChipBackgroundColor(defaultColorList);
        btnMorning.setTextColor(0xFFFFFFFF);
        btnBreakfast.setChipBackgroundColor(defaultColorList);
        btnBreakfast.setTextColor(0xFFFFFFFF);
        btnLunch.setChipBackgroundColor(defaultColorList);
        btnLunch.setTextColor(0xFFFFFFFF);
        btnEvening.setChipBackgroundColor(defaultColorList);
        btnEvening.setTextColor(0xFFFFFFFF);
        btnDinner.setChipBackgroundColor(defaultColorList);
        btnDinner.setTextColor(0xFFFFFFFF);
    }

    private void showDietPreview() {
        // Calculate totals
        int totalCalories = 0;
        int totalItems = 0;

        StringBuilder preview = new StringBuilder();
        preview.append("Diet Preview for ").append(patientName).append("\n\n");
        preview.append("Goal: ").append(goal).append("\n");
        preview.append("Duration: ").append(duration).append("\n\n");

        for (String meal : new String[] { "Morning", "Breakfast", "Lunch", "Evening", "Dinner" }) {
            List<Food> items = mealItems.get(meal);
            if (items != null && !items.isEmpty()) {
                preview.append(meal).append(":\n");
                for (Food f : items) {
                    preview.append("  - ").append(f.name).append(" (").append(f.calories).append(" kcal)\n");
                    totalCalories += f.calories;
                    totalItems++;
                }
                preview.append("\n");
            }
        }

        preview.append("Total Items: ").append(totalItems).append("\n");
        preview.append("Total Calories: ").append(totalCalories).append(" kcal");

        // Show dialog
        new android.app.AlertDialog.Builder(this)
                .setTitle("Diet Chart Preview")
                .setMessage(preview.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private String getMealItemsJson() {
        try {
            JSONObject json = new JSONObject();
            for (Map.Entry<String, List<Food>> entry : mealItems.entrySet()) {
                JSONArray arr = new JSONArray();
                for (Food f : entry.getValue()) {
                    JSONObject foodObj = new JSONObject();
                    foodObj.put("id", f.id);
                    foodObj.put("name", f.name);
                    foodObj.put("calories", f.calories);
                    foodObj.put("rasa", f.rasa);
                    arr.put(foodObj);
                }
                json.put(entry.getKey(), arr);
            }
            return json.toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    private void saveDietChart() {
        btnSave.setEnabled(false);

        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("duration", duration != null ? duration : "");
        params.put("goal", goal != null ? goal : "");
        params.put("target_calories", targetCalories != null ? targetCalories : "0");
        params.put("notes", notes != null ? notes : "");
        params.put("meal_items", getMealItemsJson());

        VolleyHelper.getInstance(this).postRequest(
                VolleyHelper.SAVE_DIET_CHART_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnSave.setEnabled(true);
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(MealStructureActivity.this,
                                        "Diet chart saved!", Toast.LENGTH_SHORT).show();
                                // Go back to dashboard
                                Intent intent = new Intent(MealStructureActivity.this, DashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MealStructureActivity.this,
                                        response.optString("message", "Failed"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(MealStructureActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnSave.setEnabled(true);
                        Toast.makeText(MealStructureActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
