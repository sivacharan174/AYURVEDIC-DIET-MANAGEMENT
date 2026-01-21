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
 * Adapter for Assessment History RecyclerView
 */
public class AssessmentHistoryAdapter extends RecyclerView.Adapter<AssessmentHistoryAdapter.ViewHolder> {

    private List<AssessmentHistoryItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AssessmentHistoryItem item);
    }

    public AssessmentHistoryAdapter(List<AssessmentHistoryItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assessment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AssessmentHistoryItem item = items.get(position);

        holder.tvEmoji.setText(item.getEmoji());
        holder.tvType.setText(item.getAssessmentType());
        holder.tvDate.setText(item.getDate());
        holder.tvSummary.setText(item.getFormattedSummary());

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

    public void updateData(List<AssessmentHistoryItem> newItems) {
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
