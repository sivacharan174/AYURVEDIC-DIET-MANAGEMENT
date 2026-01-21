package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecipeDetailActivity extends AppCompatActivity {

    ImageView imgRecipe;
    TextView tvTitle, tvIngredients, tvMethod, tvNotes;
    Button btnAddRecipe;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_recipe_detail);

        // Bind views
        imgRecipe = findViewById(R.id.imgRecipe);
        tvTitle = findViewById(R.id.tvTitle);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvMethod = findViewById(R.id.tvMethod);
        tvNotes = findViewById(R.id.tvNotes);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);

        // Parse recipe from intent
        String recipeJson = getIntent().getStringExtra("recipe");
        if (recipeJson != null) {
            recipe = new Gson().fromJson(recipeJson, Recipe.class);

            tvTitle.setText(recipe.title);
            tvIngredients.setText(recipe.ingredients);
            tvMethod.setText(recipe.method);
            tvNotes.setText(recipe.ayurvedic_notes);
            Glide.with(this)
                    .load(recipe.image_url)
                    .into(imgRecipe);
        }

        // Add Recipe button handler
        btnAddRecipe.setOnClickListener(v -> addRecipeToDiet());
    }

    private void addRecipeToDiet() {
        if (recipe == null) {
            Toast.makeText(this, "No recipe selected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get patient_id from shared preferences or pass it through intent
        int patientId = getIntent().getIntExtra("patient_id", 0);
        
        if (patientId == 0) {
            // Just return the recipe data to be used later
            Intent resultIntent = new Intent();
            resultIntent.putExtra("recipe", new Gson().toJson(recipe));
            setResult(RESULT_OK, resultIntent);
            Toast.makeText(this, recipe.title + " added to diet plan!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // If patient_id is available, save to server
        Map<String, String> params = new HashMap<>();
        params.put("patient_id", String.valueOf(patientId));
        params.put("recipe_id", String.valueOf(recipe.id));
        params.put("recipe_name", recipe.title);

        btnAddRecipe.setEnabled(false);

        VolleyHelper.getInstance(this).postRequest(
                VolleyHelper.ADD_RECIPE_TO_DIET_URL,
                params,
                new VolleyHelper.ApiCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        btnAddRecipe.setEnabled(true);
                        try {
                            if (response.getBoolean("success")) {
                                Toast.makeText(RecipeDetailActivity.this,
                                        recipe.title + " added to diet plan!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(RecipeDetailActivity.this,
                                        response.optString("message", "Failed to add"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(RecipeDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        btnAddRecipe.setEnabled(true);
                        // Fall back to just returning the data
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("recipe", new Gson().toJson(recipe));
                        setResult(RESULT_OK, resultIntent);
                        Toast.makeText(RecipeDetailActivity.this, 
                                recipe.title + " added!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
