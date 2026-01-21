package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Shows full details of a specific assessment when clicked from history list
 */
public class AssessmentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        ImageView btnBack = findViewById(R.id.btnBack);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDate = findViewById(R.id.tvDate);

        // Get data from intent
        String type = getIntent().getStringExtra("type");
        String date = getIntent().getStringExtra("date");
        int vata = getIntent().getIntExtra("vata", 0);
        int pitta = getIntent().getIntExtra("pitta", 0);
        int kapha = getIntent().getIntExtra("kapha", 0);
        String agniType = getIntent().getStringExtra("agni_type");

        btnBack.setOnClickListener(v -> finish());

        // Set title based on type
        String emoji = "📋";
        switch (type) {
            case "Prakriti":
                emoji = "🧘";
                break;
            case "Vikriti":
                emoji = "⚖️";
                break;
            case "Agni":
                emoji = "🔥";
                break;
        }
        tvTitle.setText(emoji + " " + type + " Assessment");
        tvDate.setText("Recorded: " + date);

        // Display content based on type
        LinearLayout contentContainer = findViewById(R.id.contentContainer);

        if ("Agni".equals(type)) {
            // Show Agni details
            TextView tvAgniType = findViewById(R.id.tvAgniType);
            TextView tvAgniDesc = findViewById(R.id.tvAgniDescription);
            LinearLayout agniSection = findViewById(R.id.agniSection);
            LinearLayout doshaSection = findViewById(R.id.doshaSection);

            if (agniSection != null)
                agniSection.setVisibility(android.view.View.VISIBLE);
            if (doshaSection != null)
                doshaSection.setVisibility(android.view.View.GONE);

            if (tvAgniType != null)
                tvAgniType.setText(agniType != null ? agniType : "Not specified");
            if (tvAgniDesc != null)
                tvAgniDesc.setText(getAgniDescription(agniType));
        } else {
            // Show Prakriti/Vikriti dosha percentages
            TextView tvVata = findViewById(R.id.tvVata);
            TextView tvPitta = findViewById(R.id.tvPitta);
            TextView tvKapha = findViewById(R.id.tvKapha);
            TextView tvDominant = findViewById(R.id.tvDominant);
            LinearLayout agniSection = findViewById(R.id.agniSection);
            LinearLayout doshaSection = findViewById(R.id.doshaSection);

            if (agniSection != null)
                agniSection.setVisibility(android.view.View.GONE);
            if (doshaSection != null)
                doshaSection.setVisibility(android.view.View.VISIBLE);

            if (tvVata != null)
                tvVata.setText(vata + "%");
            if (tvPitta != null)
                tvPitta.setText(pitta + "%");
            if (tvKapha != null)
                tvKapha.setText(kapha + "%");

            // Determine dominant dosha
            String dominant = "Balanced";
            if (vata > pitta && vata > kapha)
                dominant = "Vata Dominant";
            else if (pitta > vata && pitta > kapha)
                dominant = "Pitta Dominant";
            else if (kapha > vata && kapha > pitta)
                dominant = "Kapha Dominant";

            if (tvDominant != null)
                tvDominant.setText(dominant);
        }
    }

    private String getAgniDescription(String agniType) {
        if (agniType == null)
            return "";
        switch (agniType) {
            case "Sama Agni":
                return "Balanced digestive fire. Digests food properly without issues. Ideal state.";
            case "Vishama Agni":
                return "Irregular digestive fire (Vata). Variable appetite, gas, bloating, constipation.";
            case "Tikshna Agni":
                return "Sharp digestive fire (Pitta). Strong appetite, heartburn, acid reflux.";
            case "Manda Agni":
                return "Slow digestive fire (Kapha). Weak appetite, heaviness, slow metabolism.";
            default:
                return "";
        }
    }
}
