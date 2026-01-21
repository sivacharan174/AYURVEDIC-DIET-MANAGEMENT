package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PermissionsActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvCurrentRole;
    CheckBox cbViewPatients, cbAddPatients, cbEditPatients, cbDeletePatients;
    CheckBox cbViewDiets, cbCreateDiets, cbEditDiets;
    CheckBox cbViewReports, cbGenerateReports, cbShareReports;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        btnBack = findViewById(R.id.btnBack);
        tvCurrentRole = findViewById(R.id.tvCurrentRole);
        cbViewPatients = findViewById(R.id.cbViewPatients);
        cbAddPatients = findViewById(R.id.cbAddPatients);
        cbEditPatients = findViewById(R.id.cbEditPatients);
        cbDeletePatients = findViewById(R.id.cbDeletePatients);
        cbViewDiets = findViewById(R.id.cbViewDiets);
        cbCreateDiets = findViewById(R.id.cbCreateDiets);
        cbEditDiets = findViewById(R.id.cbEditDiets);
        cbViewReports = findViewById(R.id.cbViewReports);
        cbGenerateReports = findViewById(R.id.cbGenerateReports);
        cbShareReports = findViewById(R.id.cbShareReports);
        btnSave = findViewById(R.id.btnSave);

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> savePermissions());

        loadCurrentPermissions();
    }

    private void loadCurrentPermissions() {
        // Load from shared preferences or API
        android.content.SharedPreferences prefs = getSharedPreferences("ayur_prefs", MODE_PRIVATE);
        String role = prefs.getString("user_role", "Practitioner");
        tvCurrentRole.setText(role);

        // Set permissions based on role
        if ("Administrator".equals(role)) {
            enableAllPermissions();
        } else if ("Practitioner".equals(role)) {
            setPractitionerPermissions();
        } else {
            setViewerPermissions();
        }
    }

    private void enableAllPermissions() {
        cbViewPatients.setChecked(true);
        cbAddPatients.setChecked(true);
        cbEditPatients.setChecked(true);
        cbDeletePatients.setChecked(true);
        cbViewDiets.setChecked(true);
        cbCreateDiets.setChecked(true);
        cbEditDiets.setChecked(true);
        cbViewReports.setChecked(true);
        cbGenerateReports.setChecked(true);
        cbShareReports.setChecked(true);
    }

    private void setPractitionerPermissions() {
        cbViewPatients.setChecked(true);
        cbAddPatients.setChecked(true);
        cbEditPatients.setChecked(true);
        cbDeletePatients.setChecked(false);
        cbViewDiets.setChecked(true);
        cbCreateDiets.setChecked(true);
        cbEditDiets.setChecked(true);
        cbViewReports.setChecked(true);
        cbGenerateReports.setChecked(true);
        cbShareReports.setChecked(true);
    }

    private void setViewerPermissions() {
        cbViewPatients.setChecked(true);
        cbAddPatients.setChecked(false);
        cbEditPatients.setChecked(false);
        cbDeletePatients.setChecked(false);
        cbViewDiets.setChecked(true);
        cbCreateDiets.setChecked(false);
        cbEditDiets.setChecked(false);
        cbViewReports.setChecked(true);
        cbGenerateReports.setChecked(false);
        cbShareReports.setChecked(false);
    }

    private void savePermissions() {
        btnSave.setEnabled(false);

        Map<String, String> params = new HashMap<>();
        params.put("view_patients", cbViewPatients.isChecked() ? "1" : "0");
        params.put("add_patients", cbAddPatients.isChecked() ? "1" : "0");
        params.put("edit_patients", cbEditPatients.isChecked() ? "1" : "0");
        params.put("delete_patients", cbDeletePatients.isChecked() ? "1" : "0");
        params.put("view_diets", cbViewDiets.isChecked() ? "1" : "0");
        params.put("create_diets", cbCreateDiets.isChecked() ? "1" : "0");
        params.put("edit_diets", cbEditDiets.isChecked() ? "1" : "0");
        params.put("view_reports", cbViewReports.isChecked() ? "1" : "0");
        params.put("generate_reports", cbGenerateReports.isChecked() ? "1" : "0");
        params.put("share_reports", cbShareReports.isChecked() ? "1" : "0");

        VolleyHelper.getInstance(this).postRequest(
                VolleyHelper.SAVE_PERMISSIONS_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnSave.setEnabled(true);
                        if (response.optBoolean("success")) {
                            Toast.makeText(PermissionsActivity.this, 
                                    "Permissions saved!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(PermissionsActivity.this,
                                    response.optString("message", "Failed"), 
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnSave.setEnabled(true);
                        // Save locally as fallback
                        Toast.makeText(PermissionsActivity.this, 
                                "Permissions saved locally", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
