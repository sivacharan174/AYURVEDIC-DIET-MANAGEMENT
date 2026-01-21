package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditFoodActivity extends AppCompatActivity {

    ImageView btnBack;
    EditText etFoodName, etCalories, etProtein, etCarbs, etFat;
    Spinner spCategory, spRasa, spGuna, spDosha;
    Button btnSave, btnDelete;
    Food food;
    int foodId;

    String[] categories = {"Grains", "Vegetables", "Fruits", "Dairy", "Pulses", "Spices", "Nuts", "Oils", "Beverages", "Other"};
    String[] rasas = {"Sweet", "Sour", "Salty", "Pungent", "Bitter", "Astringent"};
    String[] gunas = {"Hot", "Cold", "Light", "Heavy", "Dry", "Oily"};
    String[] doshas = {"All Doshas", "Vata", "Pitta", "Kapha", "Vata, Pitta", "Pitta, Kapha", "Vata, Kapha"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        btnBack = findViewById(R.id.btnBack);
        etFoodName = findViewById(R.id.etFoodName);
        etCalories = findViewById(R.id.etCalories);
        etProtein = findViewById(R.id.etProtein);
        etCarbs = findViewById(R.id.etCarbs);
        etFat = findViewById(R.id.etFat);
        spCategory = findViewById(R.id.spCategory);
        spRasa = findViewById(R.id.spRasa);
        spGuna = findViewById(R.id.spGuna);
        spDosha = findViewById(R.id.spDosha);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        // Setup spinners
        spCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));
        spRasa.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rasas));
        spGuna.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gunas));
        spDosha.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doshas));

        // Get food data
        String foodJson = getIntent().getStringExtra("food");
        foodId = getIntent().getIntExtra("food_id", 0);
        
        if (foodJson != null) {
            food = new Gson().fromJson(foodJson, Food.class);
            populateFields();
        }

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveFood());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void populateFields() {
        if (food == null) return;
        
        etFoodName.setText(food.name);
        etCalories.setText(String.valueOf(food.calories));
        etProtein.setText(String.valueOf(food.protein));
        etCarbs.setText(String.valueOf(food.carbs));
        etFat.setText(String.valueOf(food.fat));

        // Set spinner selections
        setSpinnerSelection(spCategory, categories, food.category);
        setSpinnerSelection(spRasa, rasas, food.rasa);
        setSpinnerSelection(spGuna, gunas, food.guna);
        setSpinnerSelection(spDosha, doshas, food.doshaEffect);
    }

    private void setSpinnerSelection(Spinner spinner, String[] values, String value) {
        if (value == null) return;
        for (int i = 0; i < values.length; i++) {
            if (values[i].equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void saveFood() {
        String name = etFoodName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Food name is required", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSave.setEnabled(false);

        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(food != null ? food.id : foodId));
        params.put("name", name);
        params.put("category", spCategory.getSelectedItem().toString());
        params.put("calories", etCalories.getText().toString().trim());
        params.put("protein", etProtein.getText().toString().trim());
        params.put("carbs", etCarbs.getText().toString().trim());
        params.put("fat", etFat.getText().toString().trim());
        params.put("rasa", spRasa.getSelectedItem().toString());
        params.put("guna", spGuna.getSelectedItem().toString());
        params.put("dosha_effect", spDosha.getSelectedItem().toString());

        VolleyHelper.getInstance(this).postRequest(
                VolleyHelper.EDIT_FOOD_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnSave.setEnabled(true);
                        if (response.optBoolean("success")) {
                            Toast.makeText(EditFoodActivity.this, "Food updated!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EditFoodActivity.this, 
                                    response.optString("message", "Failed"), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnSave.setEnabled(true);
                        Toast.makeText(EditFoodActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Food")
                .setMessage("Are you sure you want to delete this food item?")
                .setPositiveButton("Delete", (dialog, which) -> deleteFood())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteFood() {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(food != null ? food.id : foodId));
        params.put("action", "delete");

        VolleyHelper.getInstance(this).postRequest(
                VolleyHelper.EDIT_FOOD_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        if (response.optBoolean("success")) {
                            Toast.makeText(EditFoodActivity.this, "Food deleted!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(EditFoodActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
