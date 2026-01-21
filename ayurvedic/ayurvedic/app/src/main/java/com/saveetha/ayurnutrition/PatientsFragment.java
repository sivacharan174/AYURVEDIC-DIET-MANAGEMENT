package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PatientsFragment extends Fragment {

    RecyclerView rvPatients;
    EditText etSearch;
    FloatingActionButton btnAdd;
    PatientsAdapter adapter;
    List<Patient> patientList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patients, container, false);

        rvPatients = view.findViewById(R.id.rvPatients);
        etSearch = view.findViewById(R.id.etSearch);
        btnAdd = view.findViewById(R.id.btnAdd);

        rvPatients.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup add patient button
        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddPatientActivity.class));
        });

        fetchPatients();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int st, int c, int a) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                if (adapter != null)
                    adapter.filter(s.toString());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh patient list when returning to this fragment
        fetchPatients();
    }

    private void fetchPatients() {
        if (getContext() == null)
            return;

        VolleyHelper.getInstance(getContext()).getPatients(new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (!isAdded())
                    return;

                try {
                    patientList.clear();
                    JSONArray dataArray;

                    if (response.has("data")) {
                        dataArray = response.getJSONArray("data");
                    } else if (response.has("patients")) {
                        dataArray = response.getJSONArray("patients");
                    } else {
                        return;
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject obj = dataArray.getJSONObject(i);
                        Patient p = new Patient();
                        p.id = obj.optInt("id", 0);
                        p.firstName = obj.optString("first_name", "");
                        p.lastName = obj.optString("last_name", "");
                        p.age = obj.optString("age", "");
                        p.gender = obj.optString("gender", "");
                        patientList.add(p);
                    }

                    adapter = new PatientsAdapter(getContext(), patientList);
                    rvPatients.setAdapter(adapter);

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error parsing patients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Failed to load patients: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
