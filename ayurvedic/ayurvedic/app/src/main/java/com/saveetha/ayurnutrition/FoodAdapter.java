package com.saveetha.ayurnutrition;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    List<Food> list, originalList;
    Context context;
    OnFoodClickListener clickListener;
    SelectionChecker selectionChecker;

    // Interface for click callbacks
    public interface OnFoodClickListener {
        void onFoodClick(Food food);
    }

    // Interface to check if food is selected
    public interface SelectionChecker {
        boolean isSelected(int foodId);
    }

    public FoodAdapter(Context context, List<Food> list) {
        this.context = context;
        this.list = new ArrayList<>(list);
        this.originalList = new ArrayList<>(list);
        this.clickListener = null;
    }

    public FoodAdapter(Context context, List<Food> list, OnFoodClickListener listener) {
        this.context = context;
        this.list = new ArrayList<>(list);
        this.originalList = new ArrayList<>(list);
        this.clickListener = listener;
    }

    public void setSelectionChecker(SelectionChecker checker) {
        this.selectionChecker = checker;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Food f = list.get(pos);

        h.tvFoodName.setText(f.name);
        h.tvCalories.setText(f.calories + " kcal");
        h.tvCategory.setText(f.category);
        h.tvRasa.setText(f.rasa);
        h.tvDosha.setText("Good for: " + f.doshaEffect);

        // Show selection state
        if (selectionChecker != null && selectionChecker.isSelected(f.id)) {
            // Selected state - green border
            if (h.cardView != null) {
                h.cardView.setStrokeColor(0xFF00E676);
                h.cardView.setStrokeWidth(4);
            }
        } else {
            // Normal state
            if (h.cardView != null) {
                h.cardView.setStrokeColor(0x4000E676);
                h.cardView.setStrokeWidth(1);
            }
        }

        h.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onFoodClick(f);
            } else {
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.putExtra("food", new Gson().toJson(f));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void filter(String query) {
        list.clear();
        if (query.isEmpty()) {
            list.addAll(originalList);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Food f : originalList) {
                if (f.name != null && f.name.toLowerCase().contains(lowerQuery)) {
                    list.add(f);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterByCategory(String category) {
        list.clear();
        if (category.equals("All")) {
            list.addAll(originalList);
        } else {
            for (Food f : originalList) {
                if (f.category != null && f.category.equalsIgnoreCase(category)) {
                    list.add(f);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setFoods(List<Food> foods) {
        this.list = new ArrayList<>(foods);
        this.originalList = new ArrayList<>(foods);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName, tvCalories, tvCategory, tvRasa, tvDosha;
        MaterialCardView cardView;

        ViewHolder(View v) {
            super(v);
            tvFoodName = v.findViewById(R.id.tvFoodName);
            tvCalories = v.findViewById(R.id.tvCalories);
            tvCategory = v.findViewById(R.id.tvCategory);
            tvRasa = v.findViewById(R.id.tvRasa);
            tvDosha = v.findViewById(R.id.tvDosha);

            // Try to get the card view for selection styling
            if (v instanceof MaterialCardView) {
                cardView = (MaterialCardView) v;
            }
        }
    }
}
