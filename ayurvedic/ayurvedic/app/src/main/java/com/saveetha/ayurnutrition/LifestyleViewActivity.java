package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays all Lifestyle records in RecyclerView cards.
 * Click on a card to see full details.
 */
public class LifestyleViewActivity extends AppCompatActivity implements PatientDataHistoryAdapter.OnItemClickListener {

    ImageView btnBack;
    MaterialButton btnAddNew;
    ProgressBar progressBar;
    LinearLayout noDataContainer;
    RecyclerView rvHistory;

    PatientDataHistoryAdapter adapter;
    List<PatientDataHistoryItem> dataList = new ArrayList<>();

    int patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_history_view);

        patientId = getIntent().getIntExtra("patient_id", 0);

        btnBack = findViewById(R.id.btnBack);
        btnAddNew = findViewById(R.id.btnAddNew);
        progressBar = findViewById(R.id.progressBar);
        noDataContainer = findViewById(R.id.noDataContainer);
        rvHistory = findViewById(R.id.rvHistory);

        // Update header
        android.widget.TextView tvTitle = findViewById(R.id.tvTitle);
        android.widget.TextView tvSubtitle = findViewById(R.id.tvSubtitle);
        android.widget.TextView tvNoDataEmoji = findViewById(R.id.tvNoDataEmoji);
        android.widget.TextView tvNoDataTitle = findViewById(R.id.tvNoDataTitle);

        if (tvTitle != null)
            tvTitle.setText("🏃 Lifestyle History");
        if (tvSubtitle != null)
            tvSubtitle.setText("Tap any record to see details");
        if (tvNoDataEmoji != null)
            tvNoDataEmoji.setText("🏃");
        if (tvNoDataTitle != null)
            tvNoDataTitle.setText("No lifestyle records yet");

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PatientDataHistoryAdapter(dataList, this);
        rvHistory.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(this, LifestyleInputsActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        if (btnAddNew != null)
            btnAddNew.setText("➕ Add New Lifestyle Record");

        // loadData() is called in onResume()
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        noDataContainer.setVisibility(View.GONE);
        rvHistory.setVisibility(View.GONE);
        dataList.clear();

        String url = VolleyHelper.GET_LIFESTYLE_URL + "?patient_id=" + patientId + "&all=true";
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.getBoolean("success")) {
                        if (response.has("records")) {
                            JSONArray records = response.getJSONArray("records");
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject data = records.getJSONObject(i);
                                addItem(data);
                            }
                        } else if (response.optBoolean("exists", false)) {
                            JSONObject data = response.getJSONObject("data");
                            addItem(data);
                        }
                    }
                } catch (Exception e) {
                }

                if (dataList.isEmpty()) {
                    noDataContainer.setVisibility(View.VISIBLE);
                } else {
                    adapter.updateData(dataList);
                    rvHistory.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                noDataContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addItem(JSONObject data) {
        try {
            PatientDataHistoryItem item = new PatientDataHistoryItem();
            item.setId(data.optInt("id", 0));
            item.setPatientId(patientId);
            item.setDataType("Lifestyle");
            item.setDate(formatDate(data.optString("created_at", "")));

            String summary = "Sleep: " + data.optString("sleep_quality", "N/A") +
                    ", Activity: " + data.optString("activity_level", "N/A");
            item.setSummary(summary);
            item.setJsonData(data.toString());

            dataList.add(item);
        } catch (Exception e) {
        }
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty())
            return "";
        if (dateStr.length() >= 10)
            return dateStr.substring(0, 10);
        return dateStr;
    }

    @Override
    public void onItemClick(PatientDataHistoryItem item) {
        Intent intent = new Intent(this, LifestyleDetailActivity.class);
        intent.putExtra("date", item.getDate());
        intent.putExtra("json_data", item.getJsonData());
        startActivity(intent);
    }
}
