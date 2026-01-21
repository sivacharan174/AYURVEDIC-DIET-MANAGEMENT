package com.saveetha.ayurnutrition;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DietPlanAdapter extends RecyclerView.Adapter<DietPlanAdapter.ViewHolder> {

    private Context context;
    private List<DietPlan> dietPlans;
    private OnDietPlanClickListener listener;

    public interface OnDietPlanClickListener {
        void onDietPlanClick(DietPlan dietPlan);
    }

    public DietPlanAdapter(Context context, List<DietPlan> dietPlans, OnDietPlanClickListener listener) {
        this.context = context;
        this.dietPlans = dietPlans;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_diet_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DietPlan plan = dietPlans.get(position);

        holder.tvGoalEmoji.setText(plan.getGoalEmoji());
        holder.tvGoal.setText(plan.goal != null ? plan.goal + " Diet" : "Diet Plan");
        holder.tvDuration.setText(plan.duration != null ? plan.duration : "N/A");
        holder.tvCalories.setText(plan.targetCalories > 0 ? plan.targetCalories + " kcal" : "Not set");
        holder.tvDate.setText(plan.getFormattedDate());

        // Status
        if (plan.status != null && plan.status.equalsIgnoreCase("active")) {
            holder.tvStatus.setText("Active");
            holder.tvStatus.setTextColor(Color.parseColor("#00E676"));
            holder.tvStatus.setBackgroundResource(R.drawable.badge_bg_green);
        } else {
            holder.tvStatus.setText("Completed");
            holder.tvStatus.setTextColor(Color.parseColor("#9E9E9E"));
            holder.tvStatus.setBackgroundResource(R.drawable.badge_bg_gray);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDietPlanClick(plan);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dietPlans != null ? dietPlans.size() : 0;
    }

    public void updateData(List<DietPlan> newData) {
        this.dietPlans = newData;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGoalEmoji, tvGoal, tvDuration, tvCalories, tvDate, tvStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvGoalEmoji = itemView.findViewById(R.id.tvGoalEmoji);
            tvGoal = itemView.findViewById(R.id.tvGoal);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvCalories = itemView.findViewById(R.id.tvCalories);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
