package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class RasaPreferencesActivity extends AppCompatActivity {

    CheckBox cbSweet, cbSour, cbSalty, cbPungent, cbBitter, cbAstringent;
    CheckBox cbAvoidSour, cbAvoidPungent;
    Button btnNext;
    int patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rasa_preferences);

        patientId = getIntent().getIntExtra("patient_id", 0);

        // Preferences checkboxes
        cbSweet = findViewById(R.id.cbSweet);
        cbSour = findViewById(R.id.cbSour);
        cbSalty = findViewById(R.id.cbSalty);
        cbPungent = findViewById(R.id.cbPungent);
        cbBitter = findViewById(R.id.cbBitter);
        cbAstringent = findViewById(R.id.cbAstringent);

        // Avoid checkboxes
        cbAvoidSour = findViewById(R.id.cbAvoidSour);
        cbAvoidPungent = findViewById(R.id.cbAvoidPungent);

        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> saveRasa());
    }

    private void saveRasa() {
        btnNext.setEnabled(false);

        VolleyHelper.getInstance(this).saveRasa(
                patientId,
                cbSweet.isChecked() ? 1 : 0,
                cbSour.isChecked() ? 1 : 0,
                cbSalty.isChecked() ? 1 : 0,
                cbPungent.isChecked() ? 1 : 0,
                cbBitter.isChecked() ? 1 : 0,
                cbAstringent.isChecked() ? 1 : 0,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnNext.setEnabled(true);
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(RasaPreferencesActivity.this,
                                        "Rasa preferences saved", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RasaPreferencesActivity.this, FoodContraindicationsActivity.class);
                                intent.putExtra("patient_id", patientId);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RasaPreferencesActivity.this,
                                        "Failed to save preferences", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(RasaPreferencesActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnNext.setEnabled(true);
                        Toast.makeText(RasaPreferencesActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
