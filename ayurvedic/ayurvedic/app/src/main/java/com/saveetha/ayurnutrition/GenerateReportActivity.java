package com.saveetha.ayurnutrition;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GenerateReportActivity extends AppCompatActivity {

    ImageView btnBack;
    Spinner spPatient;
    RadioGroup rgReportType;
    EditText etFromDate, etToDate;
    CheckBox cbDietCharts, cbNutrientAnalysis, cbDoshaAnalysis, cbRecommendations;
    Button btnGenerate, btnShare;

    List<Patient> patients = new ArrayList<>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // Data for PDF
    JSONObject prakritiData, vikritiData, lifestyleData, dietaryData, medicalHistoryData;
    JSONArray dietChartsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);

        btnBack = findViewById(R.id.btnBack);
        spPatient = findViewById(R.id.spPatient);
        rgReportType = findViewById(R.id.rgReportType);
        etFromDate = findViewById(R.id.etFromDate);
        etToDate = findViewById(R.id.etToDate);
        cbDietCharts = findViewById(R.id.cbDietCharts);
        cbNutrientAnalysis = findViewById(R.id.cbNutrientAnalysis);
        cbDoshaAnalysis = findViewById(R.id.cbDoshaAnalysis);
        cbRecommendations = findViewById(R.id.cbRecommendations);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnShare = findViewById(R.id.btnShare);

        btnBack.setOnClickListener(v -> finish());

        // Date pickers
        etFromDate.setOnClickListener(v -> showDatePicker(etFromDate));
        etToDate.setOnClickListener(v -> showDatePicker(etToDate));

        btnGenerate.setOnClickListener(v -> generateAndDownloadPdf());
        btnShare.setOnClickListener(v -> shareReport());

        loadPatients();
    }

    private void showDatePicker(EditText target) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            c.set(year, month, day);
            target.setText(dateFormat.format(c.getTime()));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void loadPatients() {
        VolleyHelper.getInstance(this).getPatients(new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    patients.clear();
                    JSONArray arr = response.optJSONArray("data");
                    if (arr == null)
                        arr = response.optJSONArray("patients");

                    List<String> names = new ArrayList<>();
                    if (arr != null) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            Patient p = new Patient();
                            p.id = obj.optInt("id");
                            p.firstName = obj.optString("first_name");
                            p.lastName = obj.optString("last_name");
                            p.email = obj.optString("email", "");
                            p.phone = obj.optString("phone", "");
                            patients.add(p);
                            names.add(p.getFullName());
                        }
                    }

                    spPatient.setAdapter(new ArrayAdapter<>(GenerateReportActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, names));

                } catch (Exception e) {
                    Toast.makeText(GenerateReportActivity.this, "Error loading patients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(GenerateReportActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getSelectedReportType() {
        int id = rgReportType.getCheckedRadioButtonId();
        if (id == R.id.rbDietSummary)
            return "Diet Summary";
        if (id == R.id.rbAssessment)
            return "Ayurvedic Assessment";
        if (id == R.id.rbProgress)
            return "Progress Report";
        if (id == R.id.rbComprehensive)
            return "Comprehensive Report";
        return "Diet Summary";
    }

    private void generateAndDownloadPdf() {
        if (patients.isEmpty() || spPatient.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Please select a patient", Toast.LENGTH_SHORT).show();
            return;
        }

        Patient selectedPatient = patients.get(spPatient.getSelectedItemPosition());
        btnGenerate.setEnabled(false);
        btnGenerate.setText("Loading data...");

        // Load all patient data then generate PDF
        loadPatientDataAndGeneratePdf(selectedPatient);
    }

    private void loadPatientDataAndGeneratePdf(Patient patient) {
        // Reset data
        prakritiData = null;
        vikritiData = null;
        lifestyleData = null;
        dietaryData = null;
        medicalHistoryData = null;
        dietChartsData = null;

        // Counter for tracking API calls
        final int[] completedCalls = { 0 };
        final int totalCalls = 6;

        // Load Prakriti
        String prakritiUrl = VolleyHelper.GET_PRAKRITI_URL + "?patient_id=" + patient.id;
        VolleyHelper.getInstance(this).getRequest(prakritiUrl, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optBoolean("success") && response.optBoolean("exists")) {
                    prakritiData = response.optJSONObject("data");
                }
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }

            @Override
            public void onError(String error) {
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }
        });

        // Load Vikriti
        String vikritiUrl = VolleyHelper.GET_VIKRITI_URL + "?patient_id=" + patient.id;
        VolleyHelper.getInstance(this).getRequest(vikritiUrl, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optBoolean("success") && response.optBoolean("exists")) {
                    vikritiData = response.optJSONObject("data");
                }
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }

            @Override
            public void onError(String error) {
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }
        });

        // Load Lifestyle
        String lifestyleUrl = VolleyHelper.GET_LIFESTYLE_URL + "?patient_id=" + patient.id;
        VolleyHelper.getInstance(this).getRequest(lifestyleUrl, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optBoolean("success") && response.optBoolean("exists")) {
                    lifestyleData = response.optJSONObject("data");
                }
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }

            @Override
            public void onError(String error) {
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }
        });

        // Load Dietary Habits
        String dietaryUrl = VolleyHelper.GET_DIETARY_HABITS_URL + "?patient_id=" + patient.id;
        VolleyHelper.getInstance(this).getRequest(dietaryUrl, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optBoolean("success") && response.optBoolean("exists")) {
                    dietaryData = response.optJSONObject("data");
                }
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }

            @Override
            public void onError(String error) {
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }
        });

        // Load Medical History
        String medHistUrl = VolleyHelper.GET_MEDICAL_HISTORY_URL + "?patient_id=" + patient.id;
        VolleyHelper.getInstance(this).getRequest(medHistUrl, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optBoolean("success") && response.optBoolean("exists")) {
                    medicalHistoryData = response.optJSONObject("data");
                }
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }

            @Override
            public void onError(String error) {
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }
        });

        // Load Diet Charts
        String dietChartsUrl = VolleyHelper.GET_DIET_CHARTS_URL + "?patient_id=" + patient.id;
        VolleyHelper.getInstance(this).getRequest(dietChartsUrl, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (response.optBoolean("success")) {
                    dietChartsData = response.optJSONArray("data");
                }
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }

            @Override
            public void onError(String error) {
                checkAndGeneratePdf(++completedCalls[0], totalCalls, patient);
            }
        });
    }

    private synchronized void checkAndGeneratePdf(int completed, int total, Patient patient) {
        if (completed >= total) {
            runOnUiThread(() -> {
                btnGenerate.setText("Generating PDF...");
                createAndSavePdf(patient);
            });
        }
    }

    private void createAndSavePdf(Patient patient) {
        try {
            // Create PDF document
            PdfDocument document = new PdfDocument();

            // Page dimensions (A4 at 72 DPI)
            int pageWidth = 595;
            int pageHeight = 842;
            int margin = 40;
            int contentWidth = pageWidth - (margin * 2);

            // Create page
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            // Colors
            int primaryColor = Color.parseColor("#0D2818");
            int accentColor = Color.parseColor("#1A5D4A");
            int sectionBg = Color.parseColor("#F5F5F5");

            // Paints
            Paint headerBgPaint = new Paint();
            headerBgPaint.setColor(accentColor);

            Paint whitePaint = new Paint();
            whitePaint.setColor(Color.WHITE);
            whitePaint.setTextSize(26);
            whitePaint.setFakeBoldText(true);
            whitePaint.setAntiAlias(true);

            Paint subtitlePaint = new Paint();
            subtitlePaint.setColor(Color.parseColor("#B8FFDD"));
            subtitlePaint.setTextSize(12);
            subtitlePaint.setAntiAlias(true);

            Paint sectionTitlePaint = new Paint();
            sectionTitlePaint.setColor(accentColor);
            sectionTitlePaint.setTextSize(14);
            sectionTitlePaint.setFakeBoldText(true);
            sectionTitlePaint.setAntiAlias(true);

            Paint textPaint = new Paint();
            textPaint.setColor(Color.parseColor("#333333"));
            textPaint.setTextSize(11);
            textPaint.setAntiAlias(true);

            Paint boldTextPaint = new Paint();
            boldTextPaint.setColor(Color.parseColor("#333333"));
            boldTextPaint.setTextSize(11);
            boldTextPaint.setFakeBoldText(true);
            boldTextPaint.setAntiAlias(true);

            Paint labelPaint = new Paint();
            labelPaint.setColor(Color.parseColor("#666666"));
            labelPaint.setTextSize(10);
            labelPaint.setAntiAlias(true);

            Paint sectionBgPaint = new Paint();
            sectionBgPaint.setColor(sectionBg);

            Paint linePaint = new Paint();
            linePaint.setColor(Color.parseColor("#E0E0E0"));
            linePaint.setStrokeWidth(1);

            Paint accentLinePaint = new Paint();
            accentLinePaint.setColor(accentColor);
            accentLinePaint.setStrokeWidth(3);

            // ===== HEADER SECTION =====
            canvas.drawRect(0, 0, pageWidth, 100, headerBgPaint);
            canvas.drawText("AyurNutrition", margin, 45, whitePaint);
            subtitlePaint.setTextSize(11);
            canvas.drawText("Ayurvedic Nutrition Management System", margin, 62, subtitlePaint);

            String reportType = getSelectedReportType();
            whitePaint.setTextSize(12);
            whitePaint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(reportType, pageWidth - margin, 50, whitePaint);
            whitePaint.setTextAlign(Paint.Align.LEFT);

            int yPos = 130;

            // ===== PATIENT INFO BOX =====
            canvas.drawRect(margin, yPos - 15, pageWidth - margin, yPos + 55, sectionBgPaint);
            canvas.drawRect(margin, yPos - 15, margin + 4, yPos + 55, accentLinePaint);

            sectionTitlePaint.setTextSize(12);
            canvas.drawText("PATIENT INFORMATION", margin + 15, yPos, sectionTitlePaint);
            yPos += 18;

            boldTextPaint.setTextSize(14);
            canvas.drawText(patient.getFullName(), margin + 15, yPos, boldTextPaint);
            yPos += 16;

            String today = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                    .format(Calendar.getInstance().getTime());
            labelPaint.setTextSize(10);
            canvas.drawText("Report Generated: " + today, margin + 15, yPos, labelPaint);

            if (etFromDate.getText().length() > 0 && etToDate.getText().length() > 0) {
                canvas.drawText("  |  Period: " + etFromDate.getText() + " to " + etToDate.getText(),
                        margin + 170, yPos, labelPaint);
            }

            yPos += 50;

            // ===== DOSHA ANALYSIS =====
            if (cbDoshaAnalysis.isChecked() && (prakritiData != null || vikritiData != null)) {
                canvas.drawRect(margin, yPos - 12, pageWidth - margin, yPos + 8, headerBgPaint);
                whitePaint.setTextSize(11);
                whitePaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("DOSHA CONSTITUTION", margin + 10, yPos + 3, whitePaint);
                yPos += 25;

                if (prakritiData != null) {
                    int vata = prakritiData.optInt("vata", 0);
                    int pitta = prakritiData.optInt("pitta", 0);
                    int kapha = prakritiData.optInt("kapha", 0);

                    boldTextPaint.setTextSize(11);
                    canvas.drawText("Prakriti (Natural Constitution)", margin + 10, yPos, boldTextPaint);
                    yPos += 15;

                    drawDoshaBar(canvas, margin + 10, yPos, 150, 12, vata, "Vata", Color.parseColor("#64B5F6"),
                            textPaint);
                    yPos += 20;
                    drawDoshaBar(canvas, margin + 10, yPos, 150, 12, pitta, "Pitta", Color.parseColor("#FF8A65"),
                            textPaint);
                    yPos += 20;
                    drawDoshaBar(canvas, margin + 10, yPos, 150, 12, kapha, "Kapha", Color.parseColor("#81C784"),
                            textPaint);
                    yPos += 25;
                }

                if (vikritiData != null) {
                    int vata = vikritiData.optInt("vata", 0);
                    int pitta = vikritiData.optInt("pitta", 0);
                    int kapha = vikritiData.optInt("kapha", 0);

                    boldTextPaint.setTextSize(11);
                    canvas.drawText("Vikriti (Current Imbalance)", margin + 10, yPos, boldTextPaint);
                    yPos += 15;

                    drawDoshaBar(canvas, margin + 10, yPos, 150, 12, vata, "Vata", Color.parseColor("#64B5F6"),
                            textPaint);
                    yPos += 20;
                    drawDoshaBar(canvas, margin + 10, yPos, 150, 12, pitta, "Pitta", Color.parseColor("#FF8A65"),
                            textPaint);
                    yPos += 20;
                    drawDoshaBar(canvas, margin + 10, yPos, 150, 12, kapha, "Kapha", Color.parseColor("#81C784"),
                            textPaint);
                    yPos += 25;
                }
            }

            // ===== DIET PLANS =====
            if (cbDietCharts.isChecked() && dietChartsData != null && dietChartsData.length() > 0) {
                canvas.drawRect(margin, yPos - 12, pageWidth - margin, yPos + 8, headerBgPaint);
                whitePaint.setTextSize(11);
                canvas.drawText("DIET PLANS", margin + 10, yPos + 3, whitePaint);
                yPos += 25;

                for (int i = 0; i < Math.min(dietChartsData.length(), 5); i++) {
                    JSONObject chart = dietChartsData.getJSONObject(i);
                    String goal = chart.optString("goal", "Diet Plan");
                    String duration = chart.optString("duration", "");
                    int calories = chart.optInt("target_calories", 0);
                    String status = chart.optString("status", "active");

                    canvas.drawRect(margin + 10, yPos - 8, pageWidth - margin - 10, yPos + 22, sectionBgPaint);
                    boldTextPaint.setTextSize(11);
                    canvas.drawText(goal, margin + 15, yPos + 4, boldTextPaint);
                    textPaint.setTextSize(10);
                    canvas.drawText(duration + " | " + calories + " kcal | " + status.toUpperCase(),
                            margin + 15, yPos + 16, textPaint);
                    yPos += 35;
                }
                yPos += 10;
            }

            // ===== LIFESTYLE FACTORS =====
            if (cbNutrientAnalysis.isChecked() && lifestyleData != null) {
                canvas.drawRect(margin, yPos - 12, pageWidth - margin, yPos + 8, headerBgPaint);
                whitePaint.setTextSize(11);
                canvas.drawText("LIFESTYLE FACTORS", margin + 10, yPos + 3, whitePaint);
                yPos += 25;

                String sleep = lifestyleData.optString("sleep_quality", "Not recorded");
                String activity = lifestyleData.optString("activity_level", "Not recorded");
                String stress = lifestyleData.optString("stress_level", "Not recorded");

                int colWidth = contentWidth / 3;
                drawInfoBox(canvas, margin + 10, yPos, colWidth - 20, "Sleep Quality", sleep, sectionBgPaint,
                        boldTextPaint, textPaint);
                drawInfoBox(canvas, margin + 10 + colWidth, yPos, colWidth - 20, "Activity Level", activity,
                        sectionBgPaint, boldTextPaint, textPaint);
                drawInfoBox(canvas, margin + 10 + colWidth * 2, yPos, colWidth - 20, "Stress Level", stress,
                        sectionBgPaint, boldTextPaint, textPaint);
                yPos += 50;
            }

            // ===== DIETARY HABITS =====
            if (dietaryData != null) {
                canvas.drawRect(margin, yPos - 12, pageWidth - margin, yPos + 8, headerBgPaint);
                whitePaint.setTextSize(11);
                canvas.drawText("DIETARY HABITS", margin + 10, yPos + 3, whitePaint);
                yPos += 25;

                String dietType = dietaryData.optString("diet_type", "Not recorded");
                String mealFreq = dietaryData.optString("meal_frequency", "3 meals");
                String waterIntake = dietaryData.optString("water_intake", "Adequate");

                int colWidth = contentWidth / 3;
                drawInfoBox(canvas, margin + 10, yPos, colWidth - 20, "Diet Type", dietType, sectionBgPaint,
                        boldTextPaint, textPaint);
                drawInfoBox(canvas, margin + 10 + colWidth, yPos, colWidth - 20, "Meal Frequency", mealFreq,
                        sectionBgPaint, boldTextPaint, textPaint);
                drawInfoBox(canvas, margin + 10 + colWidth * 2, yPos, colWidth - 20, "Water Intake", waterIntake,
                        sectionBgPaint, boldTextPaint, textPaint);
                yPos += 50;
            }

            // ===== RECOMMENDATIONS =====
            if (cbRecommendations.isChecked()) {
                canvas.drawRect(margin, yPos - 12, pageWidth - margin, yPos + 8, headerBgPaint);
                whitePaint.setTextSize(11);
                canvas.drawText("RECOMMENDATIONS", margin + 10, yPos + 3, whitePaint);
                yPos += 25;

                String[] recommendations = {
                        "Follow a balanced diet according to your dosha constitution",
                        "Maintain regular meal timings - eat at consistent times daily",
                        "Include seasonal and locally-sourced foods in your diet",
                        "Practice mindful eating - avoid distractions during meals",
                        "Stay hydrated with warm water throughout the day"
                };

                for (String rec : recommendations) {
                    canvas.drawCircle(margin + 18, yPos - 3, 3, headerBgPaint);
                    textPaint.setTextSize(10);
                    canvas.drawText(rec, margin + 28, yPos, textPaint);
                    yPos += 16;
                }
                yPos += 10;
            }

            // ===== FOOTER =====
            canvas.drawLine(margin, pageHeight - 60, pageWidth - margin, pageHeight - 60, linePaint);

            labelPaint.setTextAlign(Paint.Align.CENTER);
            labelPaint.setTextSize(9);
            canvas.drawText("This report is generated for informational purposes only.", pageWidth / 2, pageHeight - 45,
                    labelPaint);
            canvas.drawText("Please consult a qualified Ayurvedic practitioner for personalized advice.", pageWidth / 2,
                    pageHeight - 33, labelPaint);

            boldTextPaint.setTextAlign(Paint.Align.CENTER);
            boldTextPaint.setTextSize(10);
            boldTextPaint.setColor(accentColor);
            canvas.drawText("AyurNutrition - Ayurvedic Nutrition Management System", pageWidth / 2, pageHeight - 18,
                    boldTextPaint);

            document.finishPage(page);

            // Save PDF
            String fileName = "Report_" + patient.getFullName().replace(" ", "_") + "_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                            .format(Calendar.getInstance().getTime())
                    + ".pdf";

            // Capture PDF bytes BEFORE closing document (for email)
            byte[] pdfBytes = null;
            if (patient.email != null && !patient.email.isEmpty()) {
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    document.writeTo(baos);
                    pdfBytes = baos.toByteArray();
                } catch (Exception e) {
                    Log.e("GenerateReport", "Error capturing PDF bytes: " + e.getMessage());
                }
            }

            boolean saved = savePdfToDownloads(document, fileName);
            document.close();

            if (saved) {
                Toast.makeText(this, "PDF saved to Downloads: " + fileName, Toast.LENGTH_LONG).show();
                btnShare.setVisibility(android.view.View.VISIBLE);

                // Send report via email if patient has email
                if (pdfBytes != null) {
                    sendReportViaEmail(pdfBytes, patient, fileName);
                } else if (patient.email == null || patient.email.isEmpty()) {
                    Toast.makeText(this, "No email configured for this patient", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            btnGenerate.setEnabled(true);
            btnGenerate.setText("Generate Report");
        }
    }

    // Helper method to draw dosha progress bar
    private void drawDoshaBar(Canvas canvas, int x, int y, int width, int height, int percent, String label, int color,
            Paint textPaint) {
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#E0E0E0"));
        canvas.drawRect(x + 60, y - height / 2, x + 60 + width, y + height / 2, bgPaint);

        Paint fillPaint = new Paint();
        fillPaint.setColor(color);
        int fillWidth = (int) (width * percent / 100.0);
        canvas.drawRect(x + 60, y - height / 2, x + 60 + fillWidth, y + height / 2, fillPaint);

        textPaint.setTextSize(10);
        canvas.drawText(label, x, y + 4, textPaint);

        Paint percentPaint = new Paint();
        percentPaint.setColor(Color.parseColor("#333333"));
        percentPaint.setTextSize(10);
        percentPaint.setFakeBoldText(true);
        canvas.drawText(percent + "%", x + 65 + width, y + 4, percentPaint);
    }

    // Helper method to draw info box
    private void drawInfoBox(Canvas canvas, int x, int y, int width, String label, String value, Paint bgPaint,
            Paint labelPaint, Paint valuePaint) {
        canvas.drawRect(x, y - 5, x + width, y + 35, bgPaint);
        labelPaint.setTextSize(9);
        canvas.drawText(label, x + 8, y + 8, labelPaint);
        valuePaint.setTextSize(12);
        valuePaint.setFakeBoldText(true);
        canvas.drawText(value, x + 8, y + 24, valuePaint);
        valuePaint.setFakeBoldText(false);
    }

    private boolean savePdfToDownloads(PdfDocument document, String fileName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ use MediaStore
                ContentValues values = new ContentValues();
                values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
                values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
                values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                if (uri != null) {
                    OutputStream os = getContentResolver().openOutputStream(uri);
                    if (os != null) {
                        document.writeTo(os);
                        os.close();
                        return true;
                    }
                }
            } else {
                // Older Android versions
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(downloadsDir, fileName);
                FileOutputStream fos = new FileOutputStream(file);
                document.writeTo(fos);
                fos.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void shareReport() {
        if (patients.isEmpty() || spPatient.getSelectedItemPosition() < 0) {
            Toast.makeText(this, "Please generate a report first", Toast.LENGTH_SHORT).show();
            return;
        }

        Patient selectedPatient = patients.get(spPatient.getSelectedItemPosition());
        String reportType = getSelectedReportType();

        // Create email intent
        android.content.Intent emailIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Ayurvedic Nutrition Report - " + selectedPatient.getFullName());
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                "Dear " + selectedPatient.getFullName() + ",\n\n" +
                        "Please find attached your " + reportType + " report.\n\n" +
                        "Report Period: " + etFromDate.getText() + " to " + etToDate.getText() + "\n\n" +
                        "Best regards,\nYour Ayurvedic Diet Consultant");

        try {
            startActivity(android.content.Intent.createChooser(emailIntent, "Share Report via..."));
        } catch (Exception e) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sends the generated PDF report to patient's email via backend API
     */
    private void sendReportViaEmail(byte[] pdfBytes, Patient patient, String fileName) {
        try {
            // Encode to Base64
            String pdfBase64 = Base64.encodeToString(pdfBytes, Base64.DEFAULT);

            // Prepare request parameters
            Map<String, String> params = new HashMap<>();
            params.put("pdf_base64", pdfBase64);
            params.put("patient_email", patient.email);
            params.put("patient_name", patient.getFullName());
            params.put("report_type", getSelectedReportType());
            params.put("filename", fileName);

            // Show sending toast
            Toast.makeText(this, "Sending report to " + patient.email + "...", Toast.LENGTH_SHORT).show();

            // Send to backend
            VolleyHelper.getInstance(this).postRequest(
                    VolleyHelper.SEND_REPORT_EMAIL_URL,
                    params,
                    new VolleyHelper.ApiCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            boolean success = response.optBoolean("success", false);
                            String message = response.optString("message", "Email sent");

                            if (success) {
                                Toast.makeText(GenerateReportActivity.this,
                                        "✓ Report emailed to " + patient.email, Toast.LENGTH_LONG).show();
                            } else {
                                // Check if setup is required
                                if (response.optBoolean("setup_required", false)) {
                                    Toast.makeText(GenerateReportActivity.this,
                                            "Email service not configured. Please setup PHPMailer on server.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(GenerateReportActivity.this,
                                            "Failed to email report: " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("GenerateReport", "Email error: " + error);
                            Toast.makeText(GenerateReportActivity.this,
                                    "Could not send email: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (Exception e) {
            Log.e("GenerateReport", "Error preparing email: " + e.getMessage());
            Toast.makeText(this, "Error preparing email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
