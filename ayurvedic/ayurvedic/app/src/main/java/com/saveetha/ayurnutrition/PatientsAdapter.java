package com.saveetha.ayurnutrition;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.ViewHolder> {

    List<Patient> list, fullList;
    Context context;

    public PatientsAdapter(Context context, List<Patient> list) {
        this.context = context;
        this.list = list;
        this.fullList = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_patient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Patient p = list.get(pos);

        h.tvName.setText(p.getFullName());
        h.tvDetails.setText("Age: " + p.age + ", " + p.gender);
        h.tvLastVisit.setText("Last Visit: N/A");

        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, PatientProfileActivity.class);
            i.putExtra("patient_id", p.id);
            i.putExtra("name", p.getFullName());
            i.putExtra("age", p.age);
            i.putExtra("gender", p.gender);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filter(String text) {
        list.clear();
        if(text.isEmpty()){
            list.addAll(fullList);
        } else {
            for(Patient p : fullList){
                if(p.getFullName().toLowerCase().contains(text.toLowerCase())){
                    list.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails, tvLastVisit;

        ViewHolder(View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvDetails = v.findViewById(R.id.tvDetails);
            tvLastVisit = v.findViewById(R.id.tvLastVisit);
        }
    }
}
