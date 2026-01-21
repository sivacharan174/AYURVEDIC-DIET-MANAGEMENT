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

public class ClinicalAlertsActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvCritical, tvWarning, tvInfo;
    RecyclerView rvAlerts;
    AlertAdapter adapter;
    List<Alert> alertList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinical_alerts);

        btnBack = findViewById(R.id.btnBack);
        tvCritical = findViewById(R.id.tvCritical);
        tvWarning = findViewById(R.id.tvWarning);
        tvInfo = findViewById(R.id.tvInfo);
        rvAlerts = findViewById(R.id.rvAlerts);

        rvAlerts.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());

        loadAlerts();
    }

    private void loadAlerts() {
        VolleyHelper.getInstance(this).getRequest(
                VolleyHelper.GET_CLINICAL_ALERTS_URL,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            alertList.clear();
                            int critical = 0, warning = 0, info = 0;

                            JSONArray arr = response.optJSONArray("data");
                            if (arr != null) {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    Alert a = new Alert();
                                    a.id = obj.optInt("id");
                                    a.type = obj.optString("type", "info");
                                    a.title = obj.optString("title");
                                    a.message = obj.optString("message");
                                    a.patientName = obj.optString("patient_name");
                                    a.time = obj.optString("created_at");
                                    alertList.add(a);

                                    if ("critical".equalsIgnoreCase(a.type)) critical++;
                                    else if ("warning".equalsIgnoreCase(a.type)) warning++;
                                    else info++;
                                }
                            }

                            tvCritical.setText(String.valueOf(critical));
                            tvWarning.setText(String.valueOf(warning));
                            tvInfo.setText(String.valueOf(info));

                            adapter = new AlertAdapter(ClinicalAlertsActivity.this, alertList);
                            rvAlerts.setAdapter(adapter);

                        } catch (Exception e) {
                            Toast.makeText(ClinicalAlertsActivity.this, "Error loading alerts", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ClinicalAlertsActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
