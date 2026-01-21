package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class NutrientBalanceActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvPatientName, tvCalories, tvProtein, tvCarbs, tvFat;
    ProgressBar pbCalories, pbProtein, pbCarbs, pbFat;
    int patientId;
    String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrient_balance);

        patientId = getIntent().getIntExtra("patient_id", 0);
        patientName = getIntent().getStringExtra("patient_name");

        btnBack = findViewById(R.id.btnBack);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvCalories = findViewById(R.id.tvCalories);
        tvProtein = findViewById(R.id.tvProtein);
        tvCarbs = findViewById(R.id.tvCarbs);
        tvFat = findViewById(R.id.tvFat);
        pbCalories = findViewById(R.id.pbCalories);
        pbProtein = findViewById(R.id.pbProtein);
        pbCarbs = findViewById(R.id.pbCarbs);
        pbFat = findViewById(R.id.pbFat);

        tvPatientName.setText("Patient: " + (patientName != null ? patientName : "Unknown"));

        btnBack.setOnClickListener(v -> finish());

        loadNutrientBalance();
    }

    private void loadNutrientBalance() {
        String url = VolleyHelper.GET_NUTRIENT_BALANCE_URL + "?patient_id=" + patientId;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.optJSONObject("data");
                    if (data == null) data = response;

                    // Current values
                    int currentCalories = data.optInt("current_calories", 0);
                    int targetCalories = data.optInt("target_calories", 1800);
                    float currentProtein = (float) data.optDouble("current_protein", 0);
                    float targetProtein = (float) data.optDouble("target_protein", 60);
                    float currentCarbs = (float) data.optDouble("current_carbs", 0);
                    float targetCarbs = (float) data.optDouble("target_carbs", 225);
                    float currentFat = (float) data.optDouble("current_fat", 0);
                    float targetFat = (float) data.optDouble("target_fat", 50);

                    // Update UI
                    tvCalories.setText(currentCalories + " / " + targetCalories + " kcal");
                    tvProtein.setText(currentProtein + " / " + targetProtein + " g");
                    tvCarbs.setText(currentCarbs + " / " + targetCarbs + " g");
                    tvFat.setText(currentFat + " / " + targetFat + " g");

                    // Update progress bars
                    pbCalories.setProgress(targetCalories > 0 ? (currentCalories * 100 / targetCalories) : 0);
                    pbProtein.setProgress(targetProtein > 0 ? (int)(currentProtein * 100 / targetProtein) : 0);
                    pbCarbs.setProgress(targetCarbs > 0 ? (int)(currentCarbs * 100 / targetCarbs) : 0);
                    pbFat.setProgress(targetFat > 0 ? (int)(currentFat * 100 / targetFat) : 0);

                } catch (Exception e) {
                    // Show default values
                    tvCalories.setText("0 / 1800 kcal");
                    tvProtein.setText("0 / 60 g");
                    tvCarbs.setText("0 / 225 g");
                    tvFat.setText("0 / 50 g");
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(NutrientBalanceActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
