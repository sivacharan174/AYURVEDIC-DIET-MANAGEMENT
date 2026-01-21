package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class DigestiveStrengthActivity extends AppCompatActivity {

    RadioGroup rgStrength;
    Button btnNext;

    CheckBox cbBloating, cbGas, cbIndigestion, cbPain, cbBowel;

    int patientId;
    String strengthLevel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digestive_strength);

        rgStrength = findViewById(R.id.rgStrength);
        btnNext = findViewById(R.id.btnNext);

        cbBloating = findViewById(R.id.cbBloating);
        cbGas = findViewById(R.id.cbGas);
        cbIndigestion = findViewById(R.id.cbIndigestion);
        cbPain = findViewById(R.id.cbPain);
        cbBowel = findViewById(R.id.cbBowel);

        patientId = getIntent().getIntExtra("patient_id", 0);

        rgStrength.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbStrong) strengthLevel = "Strong";
            else if (checkedId == R.id.rbModerate) strengthLevel = "Moderate";
            else if (checkedId == R.id.rbWeak) strengthLevel = "Weak";
        });

        btnNext.setOnClickListener(v -> {
            if (strengthLevel.isEmpty()) {
                Toast.makeText(this, "Select digestive strength", Toast.LENGTH_SHORT).show();
            } else {
                saveDigestiveStrength();
            }
        });
    }

    private void saveDigestiveStrength() {
        StringBuilder symptoms = new StringBuilder();

        if (cbBloating.isChecked()) symptoms.append("Frequent bloating, ");
        if (cbGas.isChecked()) symptoms.append("Gas after meals, ");
        if (cbIndigestion.isChecked()) symptoms.append("Indigestion, ");
        if (cbPain.isChecked()) symptoms.append("Abdominal pain, ");
        if (cbBowel.isChecked()) symptoms.append("Irregular bowel movements, ");

        btnNext.setEnabled(false);

        VolleyHelper.getInstance(this).saveDigestive(patientId, strengthLevel, symptoms.toString(), new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                btnNext.setEnabled(true);
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        Intent i = new Intent(DigestiveStrengthActivity.this, RasaPreferencesActivity.class);
                        i.putExtra("patient_id", patientId);
                        startActivity(i);
                    } else {
                        Toast.makeText(DigestiveStrengthActivity.this, "Failed to save", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(DigestiveStrengthActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                btnNext.setEnabled(true);
                Toast.makeText(DigestiveStrengthActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
