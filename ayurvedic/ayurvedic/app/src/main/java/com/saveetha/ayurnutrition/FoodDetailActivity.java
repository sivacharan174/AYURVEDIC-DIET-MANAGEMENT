package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class FoodDetailActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvFoodName, tvCategory, tvRasa, tvCalories, tvProtein, tvCarbs, tvFat;
    TextView tvGuna, tvDigestibility, tvDoshaEffect;
    Button btnAddToDiet;
    Food food;
    boolean selectMode;
    String mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        // Check if called from meal structure
        selectMode = getIntent().getBooleanExtra("select_mode", false);
        mealType = getIntent().getStringExtra("meal");

        btnBack = findViewById(R.id.btnBack);
        tvFoodName = findViewById(R.id.tvFoodName);
        tvCategory = findViewById(R.id.tvCategory);
        tvRasa = findViewById(R.id.tvRasa);
        tvCalories = findViewById(R.id.tvCalories);
        tvProtein = findViewById(R.id.tvProtein);
        tvCarbs = findViewById(R.id.tvCarbs);
        tvFat = findViewById(R.id.tvFat);
        tvGuna = findViewById(R.id.tvGuna);
        tvDigestibility = findViewById(R.id.tvDigestibility);
        tvDoshaEffect = findViewById(R.id.tvDoshaEffect);
        btnAddToDiet = findViewById(R.id.btnAddToDiet);

        btnBack.setOnClickListener(v -> finish());

        String foodJson = getIntent().getStringExtra("food");
        if (foodJson != null) {
            food = new Gson().fromJson(foodJson, Food.class);
            displayFood();
        }

        btnAddToDiet.setOnClickListener(v -> addToDietChart());
    }

    private void displayFood() {
        tvFoodName.setText(food.name);
        tvCategory.setText(food.category);
        tvRasa.setText(food.rasa != null ? food.rasa : "N/A");
        tvCalories.setText(String.valueOf(food.calories));
        tvProtein.setText(food.protein + "g");
        tvCarbs.setText(food.carbs + "g");
        tvFat.setText(food.fat + "g");
        tvGuna.setText(food.guna != null ? food.guna : "N/A");
        tvDigestibility.setText(food.digestibility != null ? food.digestibility : "N/A");
        tvDoshaEffect.setText(food.doshaEffect != null ? food.doshaEffect : "N/A");
    }

    private void addToDietChart() {
        if (food == null) return;

        // Return food data to calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("food", new Gson().toJson(food));
        resultIntent.putExtra("meal", mealType);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, food.name + " added to " + 
            (mealType != null ? mealType : "diet chart") + "!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
