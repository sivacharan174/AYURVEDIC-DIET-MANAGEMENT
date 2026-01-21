package com.saveetha.ayurnutrition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MealItemAdapter extends RecyclerView.Adapter<MealItemAdapter.ViewHolder> {

    private List<Food> items;
    private Context context;
    private OnItemRemoveListener removeListener;

    public interface OnItemRemoveListener {
        void onRemove(int position);
    }

    public MealItemAdapter(Context context, List<Food> items, OnItemRemoveListener listener) {
        this.context = context;
        this.items = items;
        this.removeListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meal_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Food food = items.get(position);
        holder.tvFoodName.setText(food.name);
        holder.tvCalories.setText(food.calories + " kcal");

        // Show rasa or category in quantity field
        String details = "";
        if (food.rasa != null && !food.rasa.isEmpty()) {
            details = food.rasa;
        } else if (food.category != null && !food.category.isEmpty()) {
            details = food.category;
        } else {
            details = "1 serving";
        }
        holder.tvQuantity.setText(details);

        // Set food icon based on category
        String emoji = getFoodEmoji(food.category);
        holder.tvFoodIcon.setText(emoji);

        holder.btnRemove.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemove(position);
            }
        });
    }

    private String getFoodEmoji(String category) {
        if (category == null)
            return "🍽️";
        switch (category.toLowerCase()) {
            case "grains":
                return "🌾";
            case "vegetables":
                return "🥗";
            case "fruits":
                return "🍎";
            case "dairy":
                return "🥛";
            case "spices":
                return "🌶️";
            case "proteins":
                return "🍗";
            case "legumes":
                return "🫘";
            default:
                return "🍽️";
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateItems(List<Food> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName, tvCalories, tvQuantity, tvFoodIcon;
        ImageView btnRemove;

        ViewHolder(View v) {
            super(v);
            tvFoodName = v.findViewById(R.id.tvFoodName);
            tvCalories = v.findViewById(R.id.tvCalories);
            tvQuantity = v.findViewById(R.id.tvQuantity);
            tvFoodIcon = v.findViewById(R.id.tvFoodIcon);
            btnRemove = v.findViewById(R.id.btnRemove);
        }
    }
}
