package com.saveetha.ayurnutrition;

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

public class FoodConflictCheckActivity extends AppCompatActivity {

    private ImageView btnBack;
    private LinearLayout warningBanner, emptyState;
    private TextView tvWarning, tvConflictCount;
    private RecyclerView rvConflicts;
    private ProgressBar progressBar;
    private Button btnProceed;

    private String foodIds; // Comma-separated food IDs
    private List<FoodConflict> conflictList = new ArrayList<>();
    private ConflictAdapter conflictAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_conflict_check);

        foodIds = getIntent().getStringExtra("food_ids");

        initViews();
        setupListeners();

        if (foodIds != null && !foodIds.isEmpty()) {
            checkConflicts();
        } else {
            loadAllConflicts();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        warningBanner = findViewById(R.id.warningBanner);
        emptyState = findViewById(R.id.emptyState);
        tvWarning = findViewById(R.id.tvWarning);
        tvConflictCount = findViewById(R.id.tvConflictCount);
        rvConflicts = findViewById(R.id.rvConflicts);
        progressBar = findViewById(R.id.progressBar);
        btnProceed = findViewById(R.id.btnProceed);

        rvConflicts.setLayoutManager(new LinearLayoutManager(this));
        conflictAdapter = new ConflictAdapter(conflictList);
        rvConflicts.setAdapter(conflictAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnProceed.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }

    private void checkConflicts() {
        progressBar.setVisibility(View.VISIBLE);
        rvConflicts.setVisibility(View.GONE);
        emptyState.setVisibility(View.GONE);

        String url = VolleyHelper.GET_FOOD_CONFLICTS_URL + "?food_ids=" + foodIds;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                parseConflictResponse(response);
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FoodConflictCheckActivity.this,
                        "Error checking conflicts: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllConflicts() {
        progressBar.setVisibility(View.VISIBLE);

        VolleyHelper.getInstance(this).getRequest(
                VolleyHelper.GET_FOOD_CONFLICTS_URL,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        progressBar.setVisibility(View.GONE);
                        parseConflictResponse(response);
                    }

                    @Override
                    public void onError(String error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FoodConflictCheckActivity.this,
                                "Error loading conflicts: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void parseConflictResponse(JSONObject response) {
        try {
            if (response.getBoolean("success")) {
                conflictList.clear();

                JSONArray conflicts = response.optJSONArray("conflicts");
                if (conflicts != null) {
                    for (int i = 0; i < conflicts.length(); i++) {
                        JSONObject obj = conflicts.getJSONObject(i);
                        FoodConflict conflict = new FoodConflict();
                        conflict.food1Name = obj.optString("food1_name");
                        conflict.food2Name = obj.optString("food2_name");
                        conflict.conflictType = obj.optString("conflict_type");
                        conflict.reason = obj.optString("reason");
                        conflict.reference = obj.optString("ayurvedic_reference");
                        conflictList.add(conflict);
                    }
                }

                updateUI(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI(JSONObject response) {
        int count = conflictList.size();
        tvConflictCount.setText(count + " Conflict" + (count != 1 ? "s" : "") + " Found");

        if (count > 0) {
            // Show conflicts
            rvConflicts.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
            conflictAdapter.notifyDataSetChanged();

            // Show warning banner
            boolean hasSevere = response.optBoolean("has_severe_conflicts", false);
            warningBanner.setVisibility(View.VISIBLE);

            if (hasSevere) {
                warningBanner.setBackgroundColor(
                        androidx.core.content.ContextCompat.getColor(this, android.R.color.holo_red_light));
                tvWarning.setText("SEVERE: Incompatible food combinations detected!");
                btnProceed.setText("Modify Diet (Recommended)");
            } else {
                warningBanner.setBackgroundColor(0xFFFFE0B2);
                tvWarning.setText("Some food combinations may not be ideal");
                btnProceed.setText("Proceed Anyway");
            }
        } else {
            // No conflicts
            rvConflicts.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
            warningBanner.setVisibility(View.GONE);
            btnProceed.setText("Proceed with Diet");
        }
    }

    // Data class for conflicts
    public static class FoodConflict {
        public String food1Name;
        public String food2Name;
        public String conflictType;
        public String reason;
        public String reference;
    }

    // Adapter for conflict list
    private class ConflictAdapter extends RecyclerView.Adapter<ConflictAdapter.ViewHolder> {
        private List<FoodConflict> conflicts;

        ConflictAdapter(List<FoodConflict> conflicts) {
            this.conflicts = conflicts;
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_conflict, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            FoodConflict conflict = conflicts.get(position);
            holder.bind(conflict);
        }

        @Override
        public int getItemCount() {
            return conflicts.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvFoods, tvType, tvReason, tvReference;
            View indicator;

            ViewHolder(View itemView) {
                super(itemView);
                tvFoods = itemView.findViewById(R.id.tvFoods);
                tvType = itemView.findViewById(R.id.tvType);
                tvReason = itemView.findViewById(R.id.tvReason);
                tvReference = itemView.findViewById(R.id.tvReference);
                indicator = itemView.findViewById(R.id.indicator);
            }

            void bind(FoodConflict conflict) {
                tvFoods.setText(conflict.food1Name + " + " + conflict.food2Name);
                tvType.setText(conflict.conflictType.toUpperCase());
                tvReason.setText(conflict.reason);
                tvReference.setText(conflict.reference);

                // Set indicator color based on severity
                int color;
                switch (conflict.conflictType.toLowerCase()) {
                    case "severe":
                        color = 0xFFC62828;
                        break;
                    case "moderate":
                        color = 0xFFFF8F00;
                        break;
                    default:
                        color = 0xFFFBC02D;
                }
                indicator.setBackgroundColor(color);
            }
        }
    }
}
