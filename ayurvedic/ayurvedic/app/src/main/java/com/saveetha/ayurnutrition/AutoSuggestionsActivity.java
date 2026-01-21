package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AutoSuggestionsActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvPatientName, tvDominantDosha, tvPrakriti, tvRecommendation;
    private TabLayout tabLayout;
    private RecyclerView rvFoods;
    private ProgressBar progressBar;
    private Button btnAddToDiet;

    private int patientId;
    private String patientName = "";
    private List<Food> suggestedFoods = new ArrayList<>();
    private List<Food> foodsToAvoid = new ArrayList<>();
    private FoodAdapter foodAdapter;
    private boolean showingSuggested = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_suggestions);

        patientId = getIntent().getIntExtra("patient_id", 0);
        patientName = getIntent().getStringExtra("patient_name");

        if (patientId <= 0) {
            Toast.makeText(this, "Invalid patient ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
        loadSuggestions();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvDominantDosha = findViewById(R.id.tvDominantDosha);
        tvPrakriti = findViewById(R.id.tvPrakriti);
        tvRecommendation = findViewById(R.id.tvRecommendation);
        tabLayout = findViewById(R.id.tabLayout);
        rvFoods = findViewById(R.id.rvFoods);
        progressBar = findViewById(R.id.progressBar);
        btnAddToDiet = findViewById(R.id.btnAddToDiet);

        if (patientName != null && !patientName.isEmpty()) {
            tvPatientName.setText("Patient: " + patientName);
        }

        // Setup RecyclerView
        rvFoods.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter(this, suggestedFoods, food -> {
            // Food item clicked - can be selected for diet
            Toast.makeText(this, food.name + " - " + food.rasa, Toast.LENGTH_SHORT).show();
        });
        rvFoods.setAdapter(foodAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showingSuggested = true;
                    updateFoodList(suggestedFoods);
                } else {
                    showingSuggested = false;
                    updateFoodList(foodsToAvoid);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        btnAddToDiet.setOnClickListener(v -> {
            // Return selected foods to diet builder
            Intent resultIntent = new Intent();
            // Add selected food IDs
            finish();
        });
    }

    private void loadSuggestions() {
        progressBar.setVisibility(View.VISIBLE);

        String url = VolleyHelper.GET_DOSHA_SUGGESTIONS_URL + "?patient_id=" + patientId + "&limit=15";

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.getBoolean("success")) {
                        // Update UI with dosha info
                        String dominantDosha = response.getString("dominant_dosha");
                        tvDominantDosha.setText(capitalize(dominantDosha));

                        JSONObject prakriti = response.optJSONObject("prakriti");
                        if (prakriti != null) {
                            tvPrakriti.setText(String.format("V:%d%% P:%d%% K:%d%%",
                                    prakriti.optInt("vata", 0),
                                    prakriti.optInt("pitta", 0),
                                    prakriti.optInt("kapha", 0)));
                        }

                        String note = response.optString("recommendation_note", "");
                        tvRecommendation.setText(note);

                        // Parse suggested foods
                        suggestedFoods.clear();
                        JSONArray suggestedArray = response.optJSONArray("suggested_foods");
                        if (suggestedArray != null) {
                            for (int i = 0; i < suggestedArray.length(); i++) {
                                JSONObject obj = suggestedArray.getJSONObject(i);
                                Food food = new Food();
                                food.id = obj.optInt("id");
                                food.name = obj.optString("name");
                                food.category = obj.optString("category");
                                food.calories = obj.optInt("calories");
                                food.rasa = obj.optString("rasa");
                                suggestedFoods.add(food);
                            }
                        }

                        // Parse foods to avoid
                        foodsToAvoid.clear();
                        JSONArray avoidArray = response.optJSONArray("foods_to_avoid");
                        if (avoidArray != null) {
                            for (int i = 0; i < avoidArray.length(); i++) {
                                JSONObject obj = avoidArray.getJSONObject(i);
                                Food food = new Food();
                                food.id = obj.optInt("id");
                                food.name = obj.optString("name");
                                food.category = obj.optString("category");
                                food.rasa = obj.optString("dosha_effect");
                                foodsToAvoid.add(food);
                            }
                        }

                        // Update list
                        updateFoodList(suggestedFoods);
                    } else {
                        Toast.makeText(AutoSuggestionsActivity.this,
                                response.optString("message", "Failed to load suggestions"),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(AutoSuggestionsActivity.this,
                            "Error parsing suggestions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AutoSuggestionsActivity.this,
                        "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateFoodList(List<Food> foods) {
        foodAdapter = new FoodAdapter(this, foods, null);
        rvFoods.setAdapter(foodAdapter);
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty())
            return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
