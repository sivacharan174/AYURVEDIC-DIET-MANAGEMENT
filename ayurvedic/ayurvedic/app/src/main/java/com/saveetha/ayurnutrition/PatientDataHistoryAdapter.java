package com.saveetha.ayurnutrition;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

/**
 * Generic adapter for Patient Data History RecyclerView (Lifestyle, Medical
 * History, Dietary Habits)
 */
public class PatientDataHistoryAdapter extends RecyclerView.Adapter<PatientDataHistoryAdapter.ViewHolder> {

    private List<PatientDataHistoryItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PatientDataHistoryItem item);
    }

    public PatientDataHistoryAdapter(List<PatientDataHistoryItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient_data_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PatientDataHistoryItem item = items.get(position);

        holder.tvEmoji.setText(item.getEmoji());
        holder.tvType.setText(item.getDataType());
        holder.tvDate.setText(item.getDate());
        holder.tvSummary.setText(item.getSummary());

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateData(List<PatientDataHistoryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tvEmoji, tvType, tvDate, tvSummary;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            tvEmoji = itemView.findViewById(R.id.tvEmoji);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSummary = itemView.findViewById(R.id.tvSummary);
        }
    }
}
