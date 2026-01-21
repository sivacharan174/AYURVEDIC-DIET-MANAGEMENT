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
 * Displays all Assessments (Prakriti, Vikriti, Agni) in RecyclerView cards.
 * Click on a card to see full details.
 */
public class AssessmentsViewActivity extends AppCompatActivity implements AssessmentHistoryAdapter.OnItemClickListener {

    ImageView btnBack;
    MaterialButton btnAddNew;
    ProgressBar progressBar;
    LinearLayout noDataContainer;
    RecyclerView rvHistory;

    AssessmentHistoryAdapter adapter;
    List<AssessmentHistoryItem> assessmentList = new ArrayList<>();

    int patientId;
    int loadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessments_view);

        patientId = getIntent().getIntExtra("patient_id", 0);

        btnBack = findViewById(R.id.btnBack);
        btnAddNew = findViewById(R.id.btnAddNew);
        progressBar = findViewById(R.id.progressBar);
        noDataContainer = findViewById(R.id.noDataContainer);
        rvHistory = findViewById(R.id.rvHistory);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AssessmentHistoryAdapter(assessmentList, this);
        rvHistory.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(this, PrakritiAssessmentActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        // Data loading happens in onResume()
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllAssessments();
    }

    private void loadAllAssessments() {
        progressBar.setVisibility(View.VISIBLE);
        noDataContainer.setVisibility(View.GONE);
        rvHistory.setVisibility(View.GONE);

        assessmentList.clear();
        loadCount = 0;

        loadPrakriti();
        loadVikriti();
        loadAgni();
    }

    private void checkLoadComplete() {
        loadCount++;
        if (loadCount >= 3) {
            progressBar.setVisibility(View.GONE);
            if (assessmentList.isEmpty()) {
                noDataContainer.setVisibility(View.VISIBLE);
            } else {
                // Sort by date (newest first)
                assessmentList.sort((a, b) -> b.getDate().compareTo(a.getDate()));
                adapter.updateData(assessmentList);
                rvHistory.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadPrakriti() {
        String url = VolleyHelper.GET_PRAKRITI_URL + "?patient_id=" + patientId + "&all=true";
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        // Check for array (multiple records)
                        if (response.has("records")) {
                            JSONArray records = response.getJSONArray("records");
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject data = records.getJSONObject(i);
                                addPrakritiItem(data);
                            }
                        } else if (response.optBoolean("exists", false)) {
                            // Single record
                            JSONObject data = response.getJSONObject("data");
                            addPrakritiItem(data);
                        }
                    }
                } catch (Exception e) {
                }
                checkLoadComplete();
            }

            @Override
            public void onError(String error) {
                checkLoadComplete();
            }
        });
    }

    private void addPrakritiItem(JSONObject data) {
        try {
            AssessmentHistoryItem item = new AssessmentHistoryItem();
            item.setId(data.optInt("id", 0));
            item.setPatientId(patientId);
            item.setAssessmentType("Prakriti");
            item.setDate(formatDate(data.optString("created_at", "")));
            item.setVata(data.optInt("vata", 0));
            item.setPitta(data.optInt("pitta", 0));
            item.setKapha(data.optInt("kapha", 0));
            assessmentList.add(item);
        } catch (Exception e) {
        }
    }

    private void loadVikriti() {
        String url = VolleyHelper.GET_VIKRITI_URL + "?patient_id=" + patientId + "&all=true";
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        if (response.has("records")) {
                            JSONArray records = response.getJSONArray("records");
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject data = records.getJSONObject(i);
                                addVikritiItem(data);
                            }
                        } else if (response.optBoolean("exists", false)) {
                            JSONObject data = response.getJSONObject("data");
                            addVikritiItem(data);
                        }
                    }
                } catch (Exception e) {
                }
                checkLoadComplete();
            }

            @Override
            public void onError(String error) {
                checkLoadComplete();
            }
        });
    }

    private void addVikritiItem(JSONObject data) {
        try {
            AssessmentHistoryItem item = new AssessmentHistoryItem();
            item.setId(data.optInt("id", 0));
            item.setPatientId(patientId);
            item.setAssessmentType("Vikriti");
            item.setDate(formatDate(data.optString("created_at", "")));
            item.setVata(data.optInt("vata", 0));
            item.setPitta(data.optInt("pitta", 0));
            item.setKapha(data.optInt("kapha", 0));
            assessmentList.add(item);
        } catch (Exception e) {
        }
    }

    private void loadAgni() {
        String url = VolleyHelper.GET_AGNI_URL + "?patient_id=" + patientId + "&all=true";
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        if (response.has("records")) {
                            JSONArray records = response.getJSONArray("records");
                            for (int i = 0; i < records.length(); i++) {
                                JSONObject data = records.getJSONObject(i);
                                addAgniItem(data);
                            }
                        } else if (response.optBoolean("exists", false)) {
                            JSONObject data = response.getJSONObject("data");
                            addAgniItem(data);
                        }
                    }
                } catch (Exception e) {
                }
                checkLoadComplete();
            }

            @Override
            public void onError(String error) {
                checkLoadComplete();
            }
        });
    }

    private void addAgniItem(JSONObject data) {
        try {
            AssessmentHistoryItem item = new AssessmentHistoryItem();
            item.setId(data.optInt("id", 0));
            item.setPatientId(patientId);
            item.setAssessmentType("Agni");
            item.setDate(formatDate(data.optString("created_at", "")));
            item.setAgniType(data.optString("agni_type", ""));
            assessmentList.add(item);
        } catch (Exception e) {
        }
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty())
            return "";
        // Simple formatting - take first 10 chars for date part
        if (dateStr.length() >= 10) {
            return dateStr.substring(0, 10);
        }
        return dateStr;
    }

    @Override
    public void onItemClick(AssessmentHistoryItem item) {
        Intent intent = new Intent(this, AssessmentDetailActivity.class);
        intent.putExtra("type", item.getAssessmentType());
        intent.putExtra("date", item.getDate());
        intent.putExtra("vata", item.getVata());
        intent.putExtra("pitta", item.getPitta());
        intent.putExtra("kapha", item.getKapha());
        intent.putExtra("agni_type", item.getAgniType());
        startActivity(intent);
    }
}
