package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class PatientProfileActivity extends AppCompatActivity {

    TextView tvName, tvAgeGender, tvPhone;
    TextView tvPrakriti, tvVikriti, tvAgni;
    android.widget.ProgressBar pbVata, pbPitta, pbKapha;
    TextView tvVataPercent, tvPittaPercent, tvKaphaPercent;
    ImageView btnBack, btnEdit;
    TextView btnAddRecord;

    // Summary TextViews for Patient Data cards
    TextView tvAssessmentSummary, tvLifestyleSummary, tvMedicalHistorySummary, tvDietaryHabitsSummary;

    // RecyclerView for Medical Records
    RecyclerView rvMedicalRecords;
    MedicalRecordAdapter recordAdapter;
    List<MedicalRecord> recordList = new ArrayList<>();

    // Quick Action Cards
    View cardCreateDiet, cardTimeline, cardReport;

    // Patient Data Cards
    View cardAssessment, cardLifestyle, cardMedicalHistory, cardDietaryHabits;

    int patientId;
    String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        // Bind header views
        tvName = findViewById(R.id.tvName);
        tvAgeGender = findViewById(R.id.tvAgeGender);
        tvPhone = findViewById(R.id.tvPhone);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnAddRecord = findViewById(R.id.btnAddRecord);

        // Bind dosha views
        tvPrakriti = findViewById(R.id.tvPrakriti);
        tvVikriti = findViewById(R.id.tvVikriti);
        tvAgni = findViewById(R.id.tvAgni);

        // Bind progress bars and percent labels
        pbVata = findViewById(R.id.pbVata);
        pbPitta = findViewById(R.id.pbPitta);
        pbKapha = findViewById(R.id.pbKapha);
        tvVataPercent = findViewById(R.id.tvVataPercent);
        tvPittaPercent = findViewById(R.id.tvPittaPercent);
        tvKaphaPercent = findViewById(R.id.tvKaphaPercent);

        // Bind summary TextViews
        tvAssessmentSummary = findViewById(R.id.tvAssessmentSummary);
        tvLifestyleSummary = findViewById(R.id.tvLifestyleSummary);
        tvMedicalHistorySummary = findViewById(R.id.tvMedicalHistorySummary);
        tvDietaryHabitsSummary = findViewById(R.id.tvDietaryHabitsSummary);

        // Bind Medical Records RecyclerView
        rvMedicalRecords = findViewById(R.id.rvMedicalRecords);
        rvMedicalRecords.setLayoutManager(new LinearLayoutManager(this));
        recordAdapter = new MedicalRecordAdapter(this, recordList, record -> {
            // Open medical record detail view
            Intent intent = new Intent(this, MedicalRecordDetailActivity.class);
            intent.putExtra("record_id", record.id);
            intent.putExtra("patient_id", patientId);
            intent.putExtra("visit_date", record.visitDate);
            intent.putExtra("visit_type", record.visitType);
            intent.putExtra("reason", record.reason);
            intent.putExtra("diagnosis", record.diagnosis);
            intent.putExtra("prescription", record.prescription);
            intent.putExtra("notes", record.notes);
            startActivity(intent);
        });
        rvMedicalRecords.setAdapter(recordAdapter);

        // Bind Quick Action cards
        cardCreateDiet = findViewById(R.id.cardCreateDiet);
        cardTimeline = findViewById(R.id.cardTimeline);
        cardReport = findViewById(R.id.cardReport);

        // Bind Patient Data cards
        cardAssessment = findViewById(R.id.cardAssessment);
        cardLifestyle = findViewById(R.id.cardLifestyle);
        cardMedicalHistory = findViewById(R.id.cardMedicalHistory);
        cardDietaryHabits = findViewById(R.id.cardDietaryHabits);

        // Get patient data from intent
        Intent i = getIntent();
        patientId = i.getIntExtra("patient_id", 0);
        patientName = i.getStringExtra("name");
        String age = i.getStringExtra("age");
        String gender = i.getStringExtra("gender");

        // Set patient info
        tvName.setText(patientName != null ? patientName : "Unknown");
        tvAgeGender.setText((gender != null ? gender : "N/A") + " • " + (age != null ? age : "N/A") + " years");

        // Setup back button
        btnBack.setOnClickListener(v -> finish());

        // Edit patient button
        btnEdit.setOnClickListener(v -> {
            // TODO: Open edit patient screen
            Toast.makeText(this, "Edit patient feature coming soon", Toast.LENGTH_SHORT).show();
        });

        // Add Medical Record button
        btnAddRecord.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddMedicalRecordActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        // ===== Quick Actions =====

        // Create Diet
        cardCreateDiet.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateDietActivity.class);
            intent.putExtra("patient_id", patientId);
            intent.putExtra("patient_name", patientName);
            startActivity(intent);
        });

        // Timeline / History
        cardTimeline.setOnClickListener(v -> {
            Intent intent = new Intent(this, TimelineActivity.class);
            intent.putExtra("patient_id", patientId);
            intent.putExtra("patient_name", patientName);
            startActivity(intent);
        });

        // Diet Plan History (renamed from Report)
        cardReport.setOnClickListener(v -> {
            Intent intent = new Intent(this, DietPlanHistoryActivity.class);
            intent.putExtra("patient_id", patientId);
            intent.putExtra("patient_name", patientName);
            startActivity(intent);
        });

        // ===== Patient Data =====

        // Assessments - Opens read-only view
        cardAssessment.setOnClickListener(v -> {
            Intent intent = new Intent(this, AssessmentsViewActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        // Lifestyle Inputs - Opens read-only view
        cardLifestyle.setOnClickListener(v -> {
            Intent intent = new Intent(this, LifestyleViewActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        // Medical History - Opens read-only view
        cardMedicalHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, MedicalHistoryViewActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        // Dietary Habits - Opens read-only view
        cardDietaryHabits.setOnClickListener(v -> {
            Intent intent = new Intent(this, DietaryHabitsViewActivity.class);
            intent.putExtra("patient_id", patientId);
            startActivity(intent);
        });

        // Load all patient data
        loadAllPatientData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh all data when returning
        loadAllPatientData();
    }

    private void loadAllPatientData() {
        loadMedicalRecords();
        loadLifestyleData();
        loadMedicalHistoryData();
        loadDietaryHabitsData();
        loadAssessmentData();
    }

    private void loadMedicalRecords() {
        String url = VolleyHelper.GET_MEDICAL_RECORDS_URL + "?patient_id=" + patientId;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject json) {
                try {
                    if (json.getBoolean("success")) {
                        recordList.clear();
                        JSONArray records = json.getJSONArray("records");

                        for (int i = 0; i < records.length(); i++) {
                            JSONObject obj = records.getJSONObject(i);
                            MedicalRecord record = new MedicalRecord();
                            record.id = obj.optInt("id", 0);
                            record.patientId = obj.optInt("patient_id", 0);
                            record.visitDate = obj.optString("visit_date", "");
                            record.visitType = obj.optString("visit_type", "");
                            record.reason = obj.optString("reason", "");
                            record.diagnosis = obj.optString("diagnosis", "");
                            record.prescription = obj.optString("prescription", "");
                            record.notes = obj.optString("notes", "");
                            recordList.add(record);
                        }

                        recordAdapter.updateRecords(recordList);
                    }
                } catch (Exception e) {
                    // Silently fail - empty list is fine
                }
            }

            @Override
            public void onError(String error) {
                // Silently fail
            }
        });
    }

    private void loadLifestyleData() {
        String url = VolleyHelper.GET_LIFESTYLE_URL + "?patient_id=" + patientId;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.optBoolean("exists", false)) {
                        JSONObject data = response.getJSONObject("data");
                        String sleepQuality = data.optString("sleep_quality", "");
                        String activityLevel = data.optString("activity_level", "");
                        String stressLevel = data.optString("stress_level", "");

                        StringBuilder summary = new StringBuilder();
                        if (!sleepQuality.isEmpty())
                            summary.append("Sleep: ").append(sleepQuality);
                        if (!activityLevel.isEmpty()) {
                            if (summary.length() > 0)
                                summary.append(" • ");
                            summary.append(activityLevel);
                        }
                        if (!stressLevel.isEmpty()) {
                            if (summary.length() > 0)
                                summary.append(" • ");
                            summary.append("Stress: ").append(stressLevel);
                        }

                        if (summary.length() > 0) {
                            tvLifestyleSummary.setText(summary.toString());
                        }
                    }
                } catch (Exception e) {
                    // Keep default text
                }
            }

            @Override
            public void onError(String error) {
                // Keep default text
            }
        });
    }

    private void loadMedicalHistoryData() {
        String url = VolleyHelper.GET_MEDICAL_HISTORY_URL + "?patient_id=" + patientId;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.optBoolean("exists", false)) {
                        JSONObject data = response.getJSONObject("data");
                        String conditions = data.optString("conditions", "");
                        String allergies = data.optString("allergies", "");

                        StringBuilder summary = new StringBuilder();
                        if (!conditions.isEmpty() && !conditions.equals("null")) {
                            // Truncate if too long
                            String conditionsShort = conditions.length() > 30 ? conditions.substring(0, 30) + "..."
                                    : conditions;
                            summary.append(conditionsShort);
                        }
                        if (!allergies.isEmpty() && !allergies.equals("null")) {
                            if (summary.length() > 0)
                                summary.append(" | ");
                            summary.append("Allergies: Yes");
                        }

                        if (summary.length() > 0) {
                            tvMedicalHistorySummary.setText(summary.toString());
                        }
                    }
                } catch (Exception e) {
                    // Keep default text
                }
            }

            @Override
            public void onError(String error) {
                // Keep default text
            }
        });
    }

    private void loadDietaryHabitsData() {
        String url = VolleyHelper.GET_DIETARY_HABITS_URL + "?patient_id=" + patientId;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.optBoolean("exists", false)) {
                        JSONObject data = response.getJSONObject("data");
                        String dietType = data.optString("diet_type", "");
                        String foodPrefs = data.optString("food_preferences", "");

                        StringBuilder summary = new StringBuilder();
                        if (!dietType.isEmpty()) {
                            summary.append(dietType);
                        }
                        if (!foodPrefs.isEmpty() && !foodPrefs.equals("null")) {
                            if (summary.length() > 0)
                                summary.append(" • ");
                            // Truncate if too long
                            String prefsShort = foodPrefs.length() > 20 ? foodPrefs.substring(0, 20) + "..."
                                    : foodPrefs;
                            summary.append(prefsShort);
                        }

                        if (summary.length() > 0) {
                            tvDietaryHabitsSummary.setText(summary.toString());
                        }
                    }
                } catch (Exception e) {
                    // Keep default text
                }
            }

            @Override
            public void onError(String error) {
                // Keep default text
            }
        });
    }

    private void loadAssessmentData() {
        // Load Prakriti data for the assessment summary and Dosha Constitution
        String url = VolleyHelper.GET_PRAKRITI_URL + "?patient_id=" + patientId;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.optBoolean("exists", false)) {
                        JSONObject data = response.getJSONObject("data");
                        int vata = data.optInt("vata", 0);
                        int pitta = data.optInt("pitta", 0);
                        int kapha = data.optInt("kapha", 0);

                        // Update Assessment summary card
                        String summary = "V:" + vata + "% • P:" + pitta + "% • K:" + kapha + "%";
                        tvAssessmentSummary.setText(summary);

                        // Update Dosha Constitution progress bars and percentages
                        if (pbVata != null)
                            pbVata.setProgress(vata);
                        if (pbPitta != null)
                            pbPitta.setProgress(pitta);
                        if (pbKapha != null)
                            pbKapha.setProgress(kapha);

                        if (tvVataPercent != null)
                            tvVataPercent.setText(vata + "%");
                        if (tvPittaPercent != null)
                            tvPittaPercent.setText(pitta + "%");
                        if (tvKaphaPercent != null)
                            tvKaphaPercent.setText(kapha + "%");

                        // Determine dominant dosha for Prakriti label
                        String prakritiType = getDominantDosha(vata, pitta, kapha);
                        if (tvPrakriti != null)
                            tvPrakriti.setText(prakritiType);
                    }
                } catch (Exception e) {
                    // Keep default text
                }
            }

            @Override
            public void onError(String error) {
                // Keep default text
            }
        });

        // Load Vikriti data
        loadVikritiData();

        // Load Agni data
        loadAgniData();
    }

    private void loadVikritiData() {
        String url = VolleyHelper.GET_VIKRITI_URL + "?patient_id=" + patientId;
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.optBoolean("exists", false)) {
                        JSONObject data = response.getJSONObject("data");
                        int vata = data.optInt("vata", 0);
                        int pitta = data.optInt("pitta", 0);
                        int kapha = data.optInt("kapha", 0);

                        // Determine imbalanced dosha for Vikriti label
                        String vikritiType = getImbalancedDosha(vata, pitta, kapha);
                        if (tvVikriti != null)
                            tvVikriti.setText(vikritiType);
                    }
                } catch (Exception e) {
                    // Keep default text
                }
            }

            @Override
            public void onError(String error) {
                // Keep default text
            }
        });
    }

    private void loadAgniData() {
        String url = VolleyHelper.GET_AGNI_URL + "?patient_id=" + patientId;
        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    if (response.getBoolean("success") && response.optBoolean("exists", false)) {
                        JSONObject data = response.getJSONObject("data");
                        String agniType = data.optString("agni_type", "Normal");
                        if (tvAgni != null)
                            tvAgni.setText(agniType);
                    }
                } catch (Exception e) {
                    // Keep default text
                }
            }

            @Override
            public void onError(String error) {
                // Keep default text
            }
        });
    }

    private String getDominantDosha(int vata, int pitta, int kapha) {
        if (vata >= pitta && vata >= kapha) {
            if (pitta > kapha && pitta >= vata - 10)
                return "Vata-Pitta";
            if (kapha > pitta && kapha >= vata - 10)
                return "Vata-Kapha";
            return "Vata";
        } else if (pitta >= vata && pitta >= kapha) {
            if (vata > kapha && vata >= pitta - 10)
                return "Pitta-Vata";
            if (kapha > vata && kapha >= pitta - 10)
                return "Pitta-Kapha";
            return "Pitta";
        } else {
            if (vata > pitta && vata >= kapha - 10)
                return "Kapha-Vata";
            if (pitta > vata && pitta >= kapha - 10)
                return "Kapha-Pitta";
            return "Kapha";
        }
    }

    private String getImbalancedDosha(int vata, int pitta, int kapha) {
        if (vata >= pitta && vata >= kapha)
            return "Vata ↑";
        if (pitta >= vata && pitta >= kapha)
            return "Pitta ↑";
        return "Kapha ↑";
    }
}
