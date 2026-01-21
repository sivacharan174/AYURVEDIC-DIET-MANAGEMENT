package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DietPlanHistoryActivity extends AppCompatActivity {

    ImageView btnBack, btnAdd;
    TextView tvPatientName, tvPlanCount;
    RecyclerView rvDietPlans;
    LinearLayout emptyState;
    Button btnCreateDiet;
    ProgressBar progressBar;

    int patientId;
    String patientName;
    List<DietPlan> dietPlans = new ArrayList<>();
    DietPlanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan_history);

        // Get patient data
        patientId = getIntent().getIntExtra("patient_id", 0);
        patientName = getIntent().getStringExtra("patient_name");

        // Bind views
        btnBack = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvPlanCount = findViewById(R.id.tvPlanCount);
        rvDietPlans = findViewById(R.id.rvDietPlans);
        emptyState = findViewById(R.id.emptyState);
        btnCreateDiet = findViewById(R.id.btnCreateDiet);
        progressBar = findViewById(R.id.progressBar);

        // Set patient name
        tvPatientName.setText(patientName != null ? patientName : "Unknown Patient");

        // Setup RecyclerView
        rvDietPlans.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DietPlanAdapter(this, dietPlans, this::openDietPlanDetail);
        rvDietPlans.setAdapter(adapter);

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Add button - create new diet
        btnAdd.setOnClickListener(v -> createNewDiet());
        btnCreateDiet.setOnClickListener(v -> createNewDiet());

        // Load diet plans
        loadDietPlans();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning
        loadDietPlans();
    }

    private void createNewDiet() {
        Intent intent = new Intent(this, CreateDietActivity.class);
        intent.putExtra("patient_id", patientId);
        intent.putExtra("patient_name", patientName);
        startActivity(intent);
    }

    private void openDietPlanDetail(DietPlan plan) {
        Intent intent = new Intent(this, DietPlanDetailActivity.class);
        intent.putExtra("diet_plan_id", plan.id);
        intent.putExtra("patient_id", patientId);
        intent.putExtra("patient_name", patientName);
        intent.putExtra("goal", plan.goal);
        intent.putExtra("duration", plan.duration);
        intent.putExtra("target_calories", plan.targetCalories);
        intent.putExtra("notes", plan.notes);
        intent.putExtra("meal_items", plan.mealItems);
        intent.putExtra("status", plan.status);
        intent.putExtra("created_at", plan.createdAt);
        startActivity(intent);
    }

    private void loadDietPlans() {
        progressBar.setVisibility(View.VISIBLE);
        rvDietPlans.setVisibility(View.GONE);
        emptyState.setVisibility(View.GONE);

        String url = VolleyHelper.GET_DIET_CHARTS_URL + "?patient_id=" + patientId;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                progressBar.setVisibility(View.GONE);

                try {
                    if (response.optBoolean("success", false)) {
                        dietPlans.clear();
                        JSONArray data = response.optJSONArray("data");

                        if (data != null && data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                DietPlan plan = new DietPlan();
                                plan.id = obj.optInt("id");
                                plan.patientId = obj.optInt("patient_id");
                                plan.duration = obj.optString("duration", "");
                                plan.goal = obj.optString("goal", "");
                                plan.targetCalories = obj.optInt("target_calories", 0);
                                plan.notes = obj.optString("notes", "");

                                // Handle meal_items as either JSONObject or String
                                Object mealItemsObj = obj.opt("meal_items");
                                if (mealItemsObj instanceof JSONObject) {
                                    plan.mealItems = mealItemsObj.toString();
                                } else if (mealItemsObj instanceof String) {
                                    plan.mealItems = (String) mealItemsObj;
                                } else {
                                    plan.mealItems = "{}";
                                }

                                plan.status = obj.optString("status", "active");
                                plan.createdAt = obj.optString("created_at", "");
                                dietPlans.add(plan);
                            }

                            adapter.updateData(dietPlans);
                            rvDietPlans.setVisibility(View.VISIBLE);
                            tvPlanCount.setText(dietPlans.size() + " diet plan" + (dietPlans.size() != 1 ? "s" : ""));
                        } else {
                            showEmptyState();
                        }
                    } else {
                        showEmptyState();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showEmptyState();
                }
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DietPlanHistoryActivity.this,
                        "Error loading diet plans: " + error, Toast.LENGTH_SHORT).show();
                showEmptyState();
            }
        });
    }

    private void showEmptyState() {
        rvDietPlans.setVisibility(View.GONE);
        emptyState.setVisibility(View.VISIBLE);
        tvPlanCount.setText("0 diet plans");
    }
}
