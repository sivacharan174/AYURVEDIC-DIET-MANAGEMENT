package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

public class AnalyticsActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvTotalPatients, tvActiveDiets, tvPendingTasks;
    TextView tvVataPercent, tvPittaPercent, tvKaphaPercent;
    RecyclerView rvActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        btnBack = findViewById(R.id.btnBack);
        tvTotalPatients = findViewById(R.id.tvTotalPatients);
        tvActiveDiets = findViewById(R.id.tvActiveDiets);
        tvPendingTasks = findViewById(R.id.tvPendingTasks);
        tvVataPercent = findViewById(R.id.tvVataPercent);
        tvPittaPercent = findViewById(R.id.tvPittaPercent);
        tvKaphaPercent = findViewById(R.id.tvKaphaPercent);
        rvActivity = findViewById(R.id.rvActivity);

        rvActivity.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        loadAnalytics();
    }

    private void loadAnalytics() {
        VolleyHelper.getInstance(this).getRequest(
                VolleyHelper.GET_ANALYTICS_URL,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            JSONObject data = response.optJSONObject("data");
                            if (data == null) data = response;

                            tvTotalPatients.setText(String.valueOf(data.optInt("total_patients", 0)));
                            tvActiveDiets.setText(String.valueOf(data.optInt("active_diets", 0)));
                            tvPendingTasks.setText(String.valueOf(data.optInt("pending_tasks", 0)));

                            tvVataPercent.setText(data.optInt("vata_percent", 33) + "%");
                            tvPittaPercent.setText(data.optInt("pitta_percent", 33) + "%");
                            tvKaphaPercent.setText(data.optInt("kapha_percent", 34) + "%");

                        } catch (Exception e) {
                            setDefaultValues();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(AnalyticsActivity.this, error, Toast.LENGTH_SHORT).show();
                        setDefaultValues();
                    }
                });
    }

    private void setDefaultValues() {
        tvTotalPatients.setText("0");
        tvActiveDiets.setText("0");
        tvPendingTasks.setText("0");
        tvVataPercent.setText("33%");
        tvPittaPercent.setText("33%");
        tvKaphaPercent.setText("34%");
    }
}
