package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Modern Timeline Activity showing all patient activities chronologically
 */
public class TimelineActivity extends AppCompatActivity implements TimelineAdapter.OnItemClickListener {

    ImageView btnBack;
    TextView tvPatientName;
    ProgressBar progressBar;
    LinearLayout noDataContainer;
    RecyclerView rvTimeline;
    Chip chipAll, chipAssessments, chipLifestyle, chipMedical, chipDiet;

    TimelineAdapter adapter;
    List<TimelineItem> allItems = new ArrayList<>();
    List<TimelineItem> filteredItems = new ArrayList<>();

    int patientId;
    String patientName;
    int loadCount = 0;
    String currentFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        patientId = getIntent().getIntExtra("patient_id", 0);
        patientName = getIntent().getStringExtra("patient_name");

        btnBack = findViewById(R.id.btnBack);
        tvPatientName = findViewById(R.id.tvPatientName);
        progressBar = findViewById(R.id.progressBar);
        noDataContainer = findViewById(R.id.noDataContainer);
        rvTimeline = findViewById(R.id.rvTimeline);

        chipAll = findViewById(R.id.chipAll);
        chipAssessments = findViewById(R.id.chipAssessments);
        chipLifestyle = findViewById(R.id.chipLifestyle);
        chipMedical = findViewById(R.id.chipMedical);
        chipDiet = findViewById(R.id.chipDiet);

        if (tvPatientName != null && patientName != null) {
            tvPatientName.setText(patientName);
        }

        rvTimeline.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TimelineAdapter(filteredItems, this);
        rvTimeline.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        setupChipListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllData();
    }

    private void setupChipListeners() {
        chipAll.setOnClickListener(v -> filterBy("All"));
        chipAssessments.setOnClickListener(v -> filterBy("Assessment"));
        chipLifestyle.setOnClickListener(v -> filterBy("Lifestyle"));
        chipMedical.setOnClickListener(v -> filterBy("Medical"));
        chipDiet.setOnClickListener(v -> filterBy("Diet"));
    }

    private void updateChipStyles(String selected) {
        int activeColor = android.graphics.Color.parseColor("#00E676");
        int inactiveColor = android.graphics.Color.parseColor("#1A5D4A");

        chipAll.setChipBackgroundColor(
                android.content.res.ColorStateList.valueOf(selected.equals("All") ? activeColor : inactiveColor));
        chipAssessments.setChipBackgroundColor(android.content.res.ColorStateList
                .valueOf(selected.equals("Assessment") ? activeColor : inactiveColor));
        chipLifestyle.setChipBackgroundColor(
                android.content.res.ColorStateList.valueOf(selected.equals("Lifestyle") ? activeColor : inactiveColor));
        chipMedical.setChipBackgroundColor(
                android.content.res.ColorStateList.valueOf(selected.equals("Medical") ? activeColor : inactiveColor));
        chipDiet.setChipBackgroundColor(
                android.content.res.ColorStateList.valueOf(selected.equals("Diet") ? activeColor : inactiveColor));
    }

    private void filterBy(String category) {
        currentFilter = category;
        updateChipStyles(category);

        filteredItems.clear();
        if (category.equals("All")) {
            filteredItems.addAll(allItems);
        } else {
            for (TimelineItem item : allItems) {
                if (item.getCategory().equals(category)) {
                    filteredItems.add(item);
                }
            }
        }

        if (filteredItems.isEmpty()) {
            noDataContainer.setVisibility(View.VISIBLE);
            rvTimeline.setVisibility(View.GONE);
        } else {
            noDataContainer.setVisibility(View.GONE);
            rvTimeline.setVisibility(View.VISIBLE);
            adapter.updateData(filteredItems);
        }
    }

    private void loadAllData() {
        progressBar.setVisibility(View.VISIBLE);
        noDataContainer.setVisibility(View.GONE);
        rvTimeline.setVisibility(View.GONE);

        allItems.clear();
        filteredItems.clear();
        loadCount = 0;

        loadPrakriti();
        loadVikriti();
        loadAgni();
        loadLifestyle();
        loadMedicalHistory();
        loadDietaryHabits();
    }

    private void checkLoadComplete() {
        loadCount++;
        if (loadCount >= 6) {
            progressBar.setVisibility(View.GONE);

            // Sort all items by date (newest first)
            Collections.sort(allItems);

            filterBy(currentFilter);
        }
    }

    private void loadPrakriti() {
        String url = VolleyHelper.GET_PRAKRITI_URL + "?patient_id=" + patientId + "&all=true";
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.has("records")) {
                        JSONArray records = response.getJSONArray("records");
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject data = records.getJSONObject(i);
                            TimelineItem item = new TimelineItem();
                            item.setId(data.optInt("id"));
                            item.setPatientId(patientId);
                            item.setCategory("Assessment");
                            item.setType("Prakriti");
                            item.setEmoji("🧘");
                            parseDateTime(item, data.optString("created_at", ""));
                            item.setSummary("V:" + data.optInt("vata") + "% P:" + data.optInt("pitta") + "% K:"
                                    + data.optInt("kapha") + "%");
                            item.setJsonData(data.toString());
                            allItems.add(item);
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

    private void loadVikriti() {
        String url = VolleyHelper.GET_VIKRITI_URL + "?patient_id=" + patientId + "&all=true";
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.has("records")) {
                        JSONArray records = response.getJSONArray("records");
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject data = records.getJSONObject(i);
                            TimelineItem item = new TimelineItem();
                            item.setId(data.optInt("id"));
                            item.setPatientId(patientId);
                            item.setCategory("Assessment");
                            item.setType("Vikriti");
                            item.setEmoji("⚖️");
                            parseDateTime(item, data.optString("created_at", ""));
                            item.setSummary("V:" + data.optInt("vata") + "% P:" + data.optInt("pitta") + "% K:"
                                    + data.optInt("kapha") + "%");
                            item.setJsonData(data.toString());
                            allItems.add(item);
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

    private void loadAgni() {
        String url = VolleyHelper.GET_AGNI_URL + "?patient_id=" + patientId + "&all=true";
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.has("records")) {
                        JSONArray records = response.getJSONArray("records");
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject data = records.getJSONObject(i);
                            TimelineItem item = new TimelineItem();
                            item.setId(data.optInt("id"));
                            item.setPatientId(patientId);
                            item.setCategory("Assessment");
                            item.setType("Agni");
                            item.setEmoji("🔥");
                            parseDateTime(item, data.optString("created_at", ""));
                            item.setSummary(data.optString("agni_type", ""));
                            item.setJsonData(data.toString());
                            allItems.add(item);
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

    private void loadLifestyle() {
        String url = VolleyHelper.GET_LIFESTYLE_URL + "?patient_id=" + patientId + "&all=true";
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.has("records")) {
                        JSONArray records = response.getJSONArray("records");
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject data = records.getJSONObject(i);
                            TimelineItem item = new TimelineItem();
                            item.setId(data.optInt("id"));
                            item.setPatientId(patientId);
                            item.setCategory("Lifestyle");
                            item.setType("Lifestyle");
                            item.setEmoji("🏃");
                            parseDateTime(item, data.optString("created_at", ""));
                            item.setSummary("Sleep: " + data.optString("sleep_quality", "N/A") +
                                    " | Activity: " + data.optString("activity_level", "N/A"));
                            item.setJsonData(data.toString());
                            allItems.add(item);
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

    private void loadMedicalHistory() {
        String url = VolleyHelper.GET_MEDICAL_HISTORY_URL + "?patient_id=" + patientId + "&all=true";
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.has("records")) {
                        JSONArray records = response.getJSONArray("records");
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject data = records.getJSONObject(i);
                            TimelineItem item = new TimelineItem();
                            item.setId(data.optInt("id"));
                            item.setPatientId(patientId);
                            item.setCategory("Medical");
                            item.setType("Medical History");
                            item.setEmoji("🏥");
                            parseDateTime(item, data.optString("created_at", ""));
                            String conditions = data.optString("conditions", "");
                            item.setSummary(
                                    conditions.length() > 50 ? conditions.substring(0, 50) + "..." : conditions);
                            item.setJsonData(data.toString());
                            allItems.add(item);
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

    private void loadDietaryHabits() {
        String url = VolleyHelper.GET_DIETARY_HABITS_URL + "?patient_id=" + patientId + "&all=true";
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.has("records")) {
                        JSONArray records = response.getJSONArray("records");
                        for (int i = 0; i < records.length(); i++) {
                            JSONObject data = records.getJSONObject(i);
                            TimelineItem item = new TimelineItem();
                            item.setId(data.optInt("id"));
                            item.setPatientId(patientId);
                            item.setCategory("Diet");
                            item.setType("Dietary Habits");
                            item.setEmoji("🍽️");
                            parseDateTime(item, data.optString("created_at", ""));
                            item.setSummary(data.optString("diet_type", "N/A") +
                                    " | " + data.optString("meal_count", "") + " meals/day");
                            item.setJsonData(data.toString());
                            allItems.add(item);
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

    private void parseDateTime(TimelineItem item, String datetime) {
        item.setFullDate(datetime);
        if (datetime == null || datetime.isEmpty()) {
            item.setDate("");
            item.setTime("");
            return;
        }

        try {
            // Parse "2026-01-05 14:30:00" format
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());

            Date date = inputFormat.parse(datetime);
            if (date != null) {
                item.setDate(dateFormat.format(date));
                item.setTime(timeFormat.format(date));
            }
        } catch (Exception e) {
            // Fallback: just use the date part
            if (datetime.length() >= 10) {
                item.setDate(datetime.substring(0, 10));
            }
            item.setTime("");
        }
    }

    @Override
    public void onItemClick(TimelineItem item) {
        // Navigate to appropriate detail activity based on category
        Intent intent = null;

        switch (item.getCategory()) {
            case "Assessment":
                intent = new Intent(this, AssessmentDetailActivity.class);
                intent.putExtra("type", item.getType());
                intent.putExtra("date", item.getDate());
                try {
                    JSONObject data = new JSONObject(item.getJsonData());
                    intent.putExtra("vata", data.optInt("vata", 0));
                    intent.putExtra("pitta", data.optInt("pitta", 0));
                    intent.putExtra("kapha", data.optInt("kapha", 0));
                    intent.putExtra("agni_type", data.optString("agni_type", ""));
                } catch (Exception e) {
                }
                break;

            case "Lifestyle":
                intent = new Intent(this, LifestyleDetailActivity.class);
                intent.putExtra("date", item.getDate());
                intent.putExtra("json_data", item.getJsonData());
                break;

            case "Medical":
                intent = new Intent(this, MedicalHistoryDetailActivity.class);
                intent.putExtra("date", item.getDate());
                intent.putExtra("json_data", item.getJsonData());
                break;

            case "Diet":
                intent = new Intent(this, DietaryHabitsDetailActivity.class);
                intent.putExtra("date", item.getDate());
                intent.putExtra("json_data", item.getJsonData());
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}
