package com.saveetha.ayurnutrition;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    List<Recipe> list;
    List<Recipe> originalList;
    Context context;

    public RecipeAdapter(List<Recipe> list, Context context) {
        this.list = new ArrayList<>(list);
        this.originalList = new ArrayList<>(list);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int i) {
        Recipe r = list.get(i);
        h.tvTitle.setText(r.title);
        h.tvCategory.setText(r.category);
        h.tvPrakriti.setText(r.prakriti);
        Glide.with(context)
                .load(r.image_url)
                .into(h.imgRecipe);

        h.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("recipe", new Gson().toJson(r));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filter(String query) {
        list.clear();
        if (query.isEmpty()) {
            list.addAll(originalList);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Recipe recipe : originalList) {
                if ((recipe.title != null && recipe.title.toLowerCase().contains(lowerQuery)) ||
                    (recipe.category != null && recipe.category.toLowerCase().contains(lowerQuery)) ||
                    (recipe.prakriti != null && recipe.prakriti.toLowerCase().contains(lowerQuery))) {
                    list.add(recipe);
                }
            }
        }
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvPrakriti;
        ImageView imgRecipe;

        ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvCategory = v.findViewById(R.id.tvCategory);
            tvPrakriti = v.findViewById(R.id.tvPrakriti);
            imgRecipe = v.findViewById(R.id.imgRecipe);
        }
    }
}
