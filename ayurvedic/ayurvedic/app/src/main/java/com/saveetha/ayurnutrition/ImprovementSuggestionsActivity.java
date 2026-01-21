package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImprovementSuggestionsActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvPatientName, tvScore, tvScoreLabel;
    RecyclerView rvRecommendedFoods;
    int patientId;
    String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improvement_suggestions);

        patientId = getIntent().getIntExtra("patient_id", 0);
        patientName = getIntent().getStringExtra("patient_name");

        btnBack = findViewById(R.id.btnBack);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvScore = findViewById(R.id.tvScore);
        tvScoreLabel = findViewById(R.id.tvScoreLabel);
        rvRecommendedFoods = findViewById(R.id.rvRecommendedFoods);

        tvPatientName.setText("For: " + (patientName != null ? patientName : "Patient"));
        rvRecommendedFoods.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        btnBack.setOnClickListener(v -> finish());

        loadSuggestions();
    }

    private void loadSuggestions() {
        String url = VolleyHelper.GET_SUGGESTIONS_URL + "?patient_id=" + patientId;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.optJSONObject("data");
                    if (data == null) data = response;

                    int score = data.optInt("score", 70);
                    String label = data.optString("label", "Good");

                    tvScore.setText(String.valueOf(score));
                    tvScoreLabel.setText(label);

                    // Set color based on score
                    if (score >= 80) {
                        tvScore.setTextColor(0xFF4DC080);
                    } else if (score >= 60) {
                        tvScore.setTextColor(0xFFFFAA00);
                    } else {
                        tvScore.setTextColor(0xFFFF5555);
                    }

                    // Load recommended foods
                    JSONArray foods = data.optJSONArray("recommended_foods");
                    if (foods != null) {
                        List<Food> foodList = new ArrayList<>();
                        for (int i = 0; i < foods.length() && i < 5; i++) {
                            JSONObject obj = foods.getJSONObject(i);
                            Food f = new Food();
                            f.id = obj.optInt("id");
                            f.name = obj.optString("name");
                            f.category = obj.optString("category");
                            foodList.add(f);
                        }
                        // Would set adapter here
                    }

                } catch (Exception e) {
                    setDefaultValues();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ImprovementSuggestionsActivity.this, error, Toast.LENGTH_SHORT).show();
                setDefaultValues();
            }
        });
    }

    private void setDefaultValues() {
        tvScore.setText("--");
        tvScoreLabel.setText("Complete assessment for suggestions");
    }
}
