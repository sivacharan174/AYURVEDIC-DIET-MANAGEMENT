package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PdfPreviewActivity extends AppCompatActivity {

    ImageView btnBack, btnShare;
    WebView webView;
    ProgressBar progressBar;
    Button btnDownload, btnEmail;
    String reportUrl;
    String patientEmail;
    String patientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_preview);

        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);
        btnDownload = findViewById(R.id.btnDownload);
        btnEmail = findViewById(R.id.btnEmail);

        // Get report URL from intent
        reportUrl = getIntent().getStringExtra("report_url");
        patientEmail = getIntent().getStringExtra("patient_email");
        patientName = getIntent().getStringExtra("patient_name");

        btnBack.setOnClickListener(v -> finish());
        btnShare.setOnClickListener(v -> shareReport());
        btnDownload.setOnClickListener(v -> downloadPdf());
        btnEmail.setOnClickListener(v -> emailToPatient());

        loadReport();
    }

    private void loadReport() {
        if (reportUrl == null || reportUrl.isEmpty()) {
            // Generate HTML preview
            String html = generateReportHtml();
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(reportUrl);
    }

    private String generateReportHtml() {
        String name = patientName != null ? patientName : "Patient";
        return "<!DOCTYPE html>" +
                "<html><head><style>" +
                "body { font-family: Arial; padding: 20px; background: #f5f5f5; }" +
                "h1 { color: #2E5746; }" +
                "h2 { color: #4DC080; border-bottom: 2px solid #4DC080; padding-bottom: 5px; }" +
                ".card { background: white; padding: 15px; margin: 10px 0; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }" +
                ".dosha { display: inline-block; padding: 5px 15px; margin: 5px; border-radius: 20px; }" +
                ".vata { background: #E3F2FD; color: #1976D2; }" +
                ".pitta { background: #FFEBEE; color: #D32F2F; }" +
                ".kapha { background: #E8F5E9; color: #388E3C; }" +
                "</style></head><body>" +
                "<h1>Ayurvedic Diet Report</h1>" +
                "<p>Patient: <strong>" + name + "</strong></p>" +
                "<p>Date: " + java.text.DateFormat.getDateInstance().format(new java.util.Date()) + "</p>" +
                "<div class='card'>" +
                "<h2>Constitution Assessment</h2>" +
                "<span class='dosha vata'>Vata: 30%</span>" +
                "<span class='dosha pitta'>Pitta: 40%</span>" +
                "<span class='dosha kapha'>Kapha: 30%</span>" +
                "</div>" +
                "<div class='card'>" +
                "<h2>Diet Recommendations</h2>" +
                "<ul>" +
                "<li>Favor warm, cooked foods</li>" +
                "<li>Include sweet, sour, and salty tastes</li>" +
                "<li>Avoid cold, raw foods</li>" +
                "<li>Regular meal timings recommended</li>" +
                "</ul>" +
                "</div>" +
                "<div class='card'>" +
                "<h2>Lifestyle Suggestions</h2>" +
                "<ul>" +
                "<li>Maintain regular sleep schedule</li>" +
                "<li>Practice gentle yoga or walking</li>" +
                "<li>Stay hydrated with warm water</li>" +
                "</ul>" +
                "</div>" +
                "</body></html>";
    }

    private void shareReport() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Ayurvedic Diet Report");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Please find the diet report attached.");
        startActivity(Intent.createChooser(shareIntent, "Share Report"));
    }

    private void downloadPdf() {
        Toast.makeText(this, "Downloading PDF...", Toast.LENGTH_SHORT).show();
        // In a full implementation, this would use DownloadManager
    }

    private void emailToPatient() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        if (patientEmail != null) {
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{patientEmail});
        }
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Ayurvedic Diet Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, 
                "Dear " + (patientName != null ? patientName : "Patient") + ",\n\n" +
                "Please find your personalized Ayurvedic diet report attached.\n\n" +
                "Best regards,\nYour Ayurvedic Consultant");
        
        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        } catch (Exception e) {
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }
}
