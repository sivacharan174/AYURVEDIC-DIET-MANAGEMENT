package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCustomFoodActivity extends AppCompatActivity {

    ImageView btnBack;
    EditText etFoodName, etCalories, etProtein, etCarbs, etFat;
    Spinner spCategory, spRasa, spGuna, spDosha;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom_food);

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

        // Setup spinners
        String[] categories = {"Grains", "Vegetables", "Fruits", "Dairy", "Pulses", "Spices", "Nuts", "Oils", "Beverages", "Other"};
        spCategory.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories));

        String[] rasas = {"Sweet", "Sour", "Salty", "Pungent", "Bitter", "Astringent"};
        spRasa.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rasas));

        String[] gunas = {"Hot", "Cold", "Light", "Heavy", "Dry", "Oily"};
        spGuna.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gunas));

        String[] doshas = {"All Doshas", "Vata", "Pitta", "Kapha", "Vata, Pitta", "Pitta, Kapha", "Vata, Kapha"};
        spDosha.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doshas));

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveFood());
    }

    private void saveFood() {
        String name = etFoodName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter food name", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSave.setEnabled(false);

        Map<String, String> params = new HashMap<>();
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
                VolleyHelper.ADD_CUSTOM_FOOD_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnSave.setEnabled(true);
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(AddCustomFoodActivity.this,
                                        "Food added successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddCustomFoodActivity.this,
                                        response.optString("message", "Failed"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(AddCustomFoodActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnSave.setEnabled(true);
                        Toast.makeText(AddCustomFoodActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
