package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodSearchActivity extends AppCompatActivity {

    ImageView btnBack;
    EditText etSearch;
    RecyclerView rvFoods;
    Button btnAll, btnGrains, btnVegetables, btnFruits, btnDairy, btnSpices;
    MaterialCardView selectionBar;
    TextView tvSelectionCount, tvTitle, tvSubtitle;
    Button btnClearSelection, btnDone;

    FoodAdapter adapter;
    List<Food> foodList = new ArrayList<>();
    List<Food> selectedFoods = new ArrayList<>();
    Set<Integer> selectedIds = new HashSet<>();

    boolean selectMode = false;
    String currentMeal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);

        // Check if we're in select mode (selecting food for meal)
        selectMode = getIntent().getBooleanExtra("select_mode", false);
        currentMeal = getIntent().getStringExtra("meal");

        btnBack = findViewById(R.id.btnBack);
        etSearch = findViewById(R.id.etSearch);
        rvFoods = findViewById(R.id.rvFoods);
        btnAll = findViewById(R.id.btnAll);
        btnGrains = findViewById(R.id.btnGrains);
        btnVegetables = findViewById(R.id.btnVegetables);
        btnFruits = findViewById(R.id.btnFruits);
        btnDairy = findViewById(R.id.btnDairy);
        btnSpices = findViewById(R.id.btnSpices);

        // Multi-select views
        selectionBar = findViewById(R.id.selectionBar);
        tvSelectionCount = findViewById(R.id.tvSelectionCount);
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        btnClearSelection = findViewById(R.id.btnClearSelection);
        btnDone = findViewById(R.id.btnDone);

        rvFoods.setLayoutManager(new LinearLayoutManager(this));

        // Update title for select mode
        if (selectMode) {
            tvTitle.setText("Select Foods");
            tvSubtitle.setText("Tap to select, Done when finished");
        }

        btnBack.setOnClickListener(v -> finish());

        // Search filter
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null)
                    adapter.filter(s.toString());
            }
        });

        // Category filters
        btnAll.setOnClickListener(v -> filterCategory("All"));
        btnGrains.setOnClickListener(v -> filterCategory("Grains"));
        btnVegetables.setOnClickListener(v -> filterCategory("Vegetables"));
        btnFruits.setOnClickListener(v -> filterCategory("Fruits"));
        btnDairy.setOnClickListener(v -> filterCategory("Dairy"));
        btnSpices.setOnClickListener(v -> filterCategory("Spices"));

        // Multi-select buttons
        btnClearSelection.setOnClickListener(v -> {
            selectedFoods.clear();
            selectedIds.clear();
            updateSelectionBar();
            if (adapter != null)
                adapter.notifyDataSetChanged();
        });

        btnDone.setOnClickListener(v -> returnSelectedFoods());

        loadFoods();
    }

    private void filterCategory(String category) {
        if (adapter != null)
            adapter.filterByCategory(category);
    }

    private void toggleFoodSelection(Food food) {
        if (selectedIds.contains(food.id)) {
            // Remove from selection
            selectedIds.remove(food.id);
            selectedFoods.removeIf(f -> f.id == food.id);
        } else {
            // Add to selection
            selectedIds.add(food.id);
            selectedFoods.add(food);
        }
        updateSelectionBar();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    private void updateSelectionBar() {
        int count = selectedFoods.size();
        if (count > 0) {
            selectionBar.setVisibility(View.VISIBLE);
            tvSelectionCount.setText(count + " item" + (count > 1 ? "s" : "") + " selected");
        } else {
            selectionBar.setVisibility(View.GONE);
        }
    }

    private void returnSelectedFoods() {
        if (selectedFoods.isEmpty()) {
            Toast.makeText(this, "Please select at least one food", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("selected_foods", new Gson().toJson(selectedFoods));
        resultIntent.putExtra("meal", currentMeal);
        resultIntent.putExtra("count", selectedFoods.size());
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(this, "Added " + selectedFoods.size() + " foods", Toast.LENGTH_SHORT).show();
        finish();
    }

    public boolean isFoodSelected(int foodId) {
        return selectedIds.contains(foodId);
    }

    private void loadFoods() {
        VolleyHelper.getInstance(this).getRequest(VolleyHelper.GET_FOODS_URL, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    foodList.clear();
                    JSONArray arr = response.optJSONArray("data");
                    if (arr == null)
                        arr = response.optJSONArray("foods");
                    if (arr == null)
                        return;

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        Food f = new Food();
                        f.id = obj.optInt("id");
                        f.name = obj.optString("name");
                        f.category = obj.optString("category");
                        f.calories = obj.optInt("calories");
                        f.protein = (float) obj.optDouble("protein", 0);
                        f.carbs = (float) obj.optDouble("carbs", 0);
                        f.fat = (float) obj.optDouble("fat", 0);
                        f.rasa = obj.optString("rasa");
                        f.guna = obj.optString("guna");
                        f.digestibility = obj.optString("digestibility");
                        f.doshaEffect = obj.optString("dosha_effect");
                        foodList.add(f);
                    }

                    // If in select mode, use click listener for multi-select
                    if (selectMode) {
                        adapter = new FoodAdapter(FoodSearchActivity.this, foodList, food -> {
                            toggleFoodSelection(food);
                        });
                        adapter.setSelectionChecker(FoodSearchActivity.this::isFoodSelected);
                    } else {
                        adapter = new FoodAdapter(FoodSearchActivity.this, foodList);
                    }
                    rvFoods.setAdapter(adapter);

                } catch (Exception e) {
                    Toast.makeText(FoodSearchActivity.this, "Error parsing foods", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(FoodSearchActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
