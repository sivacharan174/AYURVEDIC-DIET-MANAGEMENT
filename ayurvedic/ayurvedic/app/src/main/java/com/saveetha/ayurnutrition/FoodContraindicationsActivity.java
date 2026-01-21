package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class FoodContraindicationsActivity extends AppCompatActivity {

    CheckBox cbMilk, cbHoney, cbFruits, cbCurd, cbRadish;
    Button btnNext;
    int patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_contraindications);

        patientId = getIntent().getIntExtra("patient_id", 0);

        cbMilk = findViewById(R.id.cbMilk);
        cbHoney = findViewById(R.id.cbHoney);
        cbFruits = findViewById(R.id.cbFruits);
        cbCurd = findViewById(R.id.cbCurd);
        cbRadish = findViewById(R.id.cbRadish);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> saveFoodContraindications());
    }

    private void saveFoodContraindications() {
        btnNext.setEnabled(false);

        VolleyHelper.getInstance(this).saveFood(
                patientId,
                cbMilk.isChecked() ? 1 : 0,
                cbHoney.isChecked() ? 1 : 0,
                cbFruits.isChecked() ? 1 : 0,
                cbCurd.isChecked() ? 1 : 0,
                cbRadish.isChecked() ? 1 : 0,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnNext.setEnabled(true);
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(FoodContraindicationsActivity.this,
                                        "Assessment complete!", Toast.LENGTH_SHORT).show();
                                // Return to Dashboard after completing the assessment flow
                                Intent intent = new Intent(FoodContraindicationsActivity.this, DashboardActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(FoodContraindicationsActivity.this,
                                        "Failed to save", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(FoodContraindicationsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnNext.setEnabled(true);
                        Toast.makeText(FoodContraindicationsActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
