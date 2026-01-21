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
 * Modern Timeline Adapter with vertical line design
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private List<TimelineItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TimelineItem item);
    }

    public TimelineAdapter(List<TimelineItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeline, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        TimelineItem item = items.get(position);

        h.tvEmoji.setText(item.getEmoji());
        h.tvType.setText(item.getType());
        h.tvDate.setText(item.getDate());
        h.tvTime.setText(item.getTime());
        h.tvSummary.setText(item.getSummary());

        // Hide top line for first item
        if (position == 0) {
            h.lineTop.setVisibility(View.INVISIBLE);
        } else {
            h.lineTop.setVisibility(View.VISIBLE);
        }

        // Hide bottom line for last item
        if (position == items.size() - 1) {
            h.lineBottom.setVisibility(View.INVISIBLE);
        } else {
            h.lineBottom.setVisibility(View.VISIBLE);
        }

        h.cardContent.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateData(List<TimelineItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View lineTop, lineBottom, dot;
        MaterialCardView cardContent;
        TextView tvEmoji, tvType, tvDate, tvTime, tvSummary;

        ViewHolder(View v) {
            super(v);
            lineTop = v.findViewById(R.id.lineTop);
            lineBottom = v.findViewById(R.id.lineBottom);
            dot = v.findViewById(R.id.dot);
            cardContent = v.findViewById(R.id.cardContent);
            tvEmoji = v.findViewById(R.id.tvEmoji);
            tvType = v.findViewById(R.id.tvType);
            tvDate = v.findViewById(R.id.tvDate);
            tvTime = v.findViewById(R.id.tvTime);
            tvSummary = v.findViewById(R.id.tvSummary);
        }
    }
}
