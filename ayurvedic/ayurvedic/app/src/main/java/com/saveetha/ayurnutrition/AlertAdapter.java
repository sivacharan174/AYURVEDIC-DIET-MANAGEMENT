package com.saveetha.ayurnutrition;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {

    private List<Alert> alerts;
    private Context context;

    public AlertAdapter(Context context, List<Alert> alerts) {
        this.context = context;
        this.alerts = alerts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_alert, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Alert a = alerts.get(pos);

        h.tvTitle.setText(a.title);
        h.tvPatient.setText("Patient: " + a.patientName);
        h.tvSeverity.setText(a.type);
        h.tvDate.setText(a.time);
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPatient, tvSeverity, tvDate;

        ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvAlertTitle);
            tvPatient = v.findViewById(R.id.tvPatientName);
            tvSeverity = v.findViewById(R.id.tvSeverity);
            tvDate = v.findViewById(R.id.tvDate);
        }
    }
}
