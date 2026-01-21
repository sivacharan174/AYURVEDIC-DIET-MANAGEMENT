package com.saveetha.ayurnutrition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.ViewHolder> {

    private Context context;
    private List<MedicalRecord> records;
    private OnRecordClickListener listener;

    public interface OnRecordClickListener {
        void onRecordClick(MedicalRecord record);
    }

    public MedicalRecordAdapter(Context context, List<MedicalRecord> records, OnRecordClickListener listener) {
        this.context = context;
        this.records = records;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medical_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicalRecord record = records.get(position);

        holder.tvVisitDate.setText(record.visitDate != null ? record.visitDate : "N/A");
        holder.tvVisitReason.setText(record.reason != null ? record.reason : "General checkup");
        holder.tvVisitType.setText(record.visitType != null ? record.visitType : "Visit");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecordClick(record);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void updateRecords(List<MedicalRecord> newRecords) {
        this.records = newRecords;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvVisitDate, tvVisitReason, tvVisitType;

        ViewHolder(View v) {
            super(v);
            tvVisitDate = v.findViewById(R.id.tvVisitDate);
            tvVisitReason = v.findViewById(R.id.tvVisitReason);
            tvVisitType = v.findViewById(R.id.tvVisitType);
        }
    }
}
