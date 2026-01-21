package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class AyurvedicBalanceActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvPatientName, tvScore, tvScoreLabel;
    TextView tvVata, tvPitta, tvKapha;
    TextView tvRasaBalance, tvRecommendations;
    int patientId;
    String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayurvedic_balance);

        patientId = getIntent().getIntExtra("patient_id", 0);
        patientName = getIntent().getStringExtra("patient_name");

        btnBack = findViewById(R.id.btnBack);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvScore = findViewById(R.id.tvScore);
        tvScoreLabel = findViewById(R.id.tvScoreLabel);
        tvVata = findViewById(R.id.tvVata);
        tvPitta = findViewById(R.id.tvPitta);
        tvKapha = findViewById(R.id.tvKapha);
        tvRasaBalance = findViewById(R.id.tvRasaBalance);
        tvRecommendations = findViewById(R.id.tvRecommendations);

        tvPatientName.setText("Patient: " + (patientName != null ? patientName : "Unknown"));

        btnBack.setOnClickListener(v -> finish());

        loadBalanceScore();
    }

    private void loadBalanceScore() {
        String url = VolleyHelper.GET_BALANCE_SCORE_URL + "?patient_id=" + patientId;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.optJSONObject("data");
                    if (data == null) data = response;

                    int score = data.optInt("score", 75);
                    String label = data.optString("label", "Good");
                    String vataStatus = data.optString("vata", "Balanced");
                    String pittaStatus = data.optString("pitta", "Balanced");
                    String kaphaStatus = data.optString("kapha", "Balanced");
                    String rasaAnalysis = data.optString("rasa_analysis", 
                            "Diet analysis pending. Complete the assessment for detailed analysis.");
                    String recommendations = data.optString("recommendations",
                            "• Complete Prakriti assessment\n• Add current diet information\n• Review regularly");

                    tvScore.setText(String.valueOf(score));
                    tvScoreLabel.setText(label);
                    
                    // Update score color based on value
                    if (score >= 80) {
                        tvScore.setTextColor(0xFF4DC080); // Green
                    } else if (score >= 60) {
                        tvScore.setTextColor(0xFFFFAA00); // Orange
                    } else {
                        tvScore.setTextColor(0xFFFF5555); // Red
                    }

                    updateDoshaStatus(tvVata, vataStatus);
                    updateDoshaStatus(tvPitta, pittaStatus);
                    updateDoshaStatus(tvKapha, kaphaStatus);

                    tvRasaBalance.setText(rasaAnalysis);
                    tvRecommendations.setText(recommendations);

                } catch (Exception e) {
                    // Keep default values
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(AyurvedicBalanceActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDoshaStatus(TextView tv, String status) {
        tv.setText(status);
        if (status.toLowerCase().contains("balanced")) {
            tv.setTextColor(0xFF4DC080); // Green
        } else if (status.toLowerCase().contains("excess") || status.toLowerCase().contains("high")) {
            tv.setTextColor(0xFFFF5555); // Red
        } else if (status.toLowerCase().contains("slight")) {
            tv.setTextColor(0xFFFFAA00); // Orange
        } else {
            tv.setTextColor(0xFFFFFFFF); // White
        }
    }
}
