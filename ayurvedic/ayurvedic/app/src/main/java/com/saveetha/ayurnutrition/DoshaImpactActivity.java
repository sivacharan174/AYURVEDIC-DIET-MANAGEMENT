package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

public class DoshaImpactActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvPatientName, tvVataImpact, tvPittaImpact, tvKaphaImpact, tvAnalysis;
    ProgressBar pbVata, pbPitta, pbKapha;
    RecyclerView rvFoodImpact;
    int patientId;
    String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosha_impact);

        patientId = getIntent().getIntExtra("patient_id", 0);
        patientName = getIntent().getStringExtra("patient_name");

        btnBack = findViewById(R.id.btnBack);
        tvPatientName = findViewById(R.id.tvPatientName);
        tvVataImpact = findViewById(R.id.tvVataImpact);
        tvPittaImpact = findViewById(R.id.tvPittaImpact);
        tvKaphaImpact = findViewById(R.id.tvKaphaImpact);
        tvAnalysis = findViewById(R.id.tvAnalysis);
        pbVata = findViewById(R.id.pbVata);
        pbPitta = findViewById(R.id.pbPitta);
        pbKapha = findViewById(R.id.pbKapha);
        rvFoodImpact = findViewById(R.id.rvFoodImpact);

        tvPatientName.setText("Patient: " + (patientName != null ? patientName : "Unknown"));
        rvFoodImpact.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        loadDoshaImpact();
    }

    private void loadDoshaImpact() {
        String url = VolleyHelper.GET_DOSHA_IMPACT_URL + "?patient_id=" + patientId;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = response.optJSONObject("data");
                    if (data == null) data = response;

                    int vata = data.optInt("vata_impact", 50);
                    int pitta = data.optInt("pitta_impact", 50);
                    int kapha = data.optInt("kapha_impact", 50);
                    String analysis = data.optString("analysis", 
                            "Complete diet chart to see detailed impact analysis.");

                    pbVata.setProgress(vata);
                    pbPitta.setProgress(pitta);
                    pbKapha.setProgress(kapha);

                    tvVataImpact.setText(getImpactLabel(vata));
                    tvPittaImpact.setText(getImpactLabel(pitta));
                    tvKaphaImpact.setText(getImpactLabel(kapha));

                    updateImpactColors(tvVataImpact, vata);
                    updateImpactColors(tvPittaImpact, pitta);
                    updateImpactColors(tvKaphaImpact, kapha);

                    tvAnalysis.setText(analysis);

                } catch (Exception e) {
                    setDefaultValues();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(DoshaImpactActivity.this, error, Toast.LENGTH_SHORT).show();
                setDefaultValues();
            }
        });
    }

    private String getImpactLabel(int value) {
        if (value < 40) return "Reducing";
        if (value > 60) return "Increasing";
        return "Balanced";
    }

    private void updateImpactColors(TextView tv, int value) {
        if (value < 40) {
            tv.setTextColor(0xFF55AAFF); // Blue - reducing
        } else if (value > 60) {
            tv.setTextColor(0xFFFFAA00); // Orange - increasing
        } else {
            tv.setTextColor(0xFF4DC080); // Green - balanced
        }
    }

    private void setDefaultValues() {
        pbVata.setProgress(50);
        pbPitta.setProgress(50);
        pbKapha.setProgress(50);
        tvVataImpact.setText("Balanced");
        tvPittaImpact.setText("Balanced");
        tvKaphaImpact.setText("Balanced");
        tvAnalysis.setText("Add foods to diet chart to see impact analysis.");
    }
}
