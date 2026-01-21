package com.saveetha.ayurnutrition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    RecyclerView rvRecipes;
    EditText etSearch;
    RecipeAdapter adapter;
    List<Recipe> recipeList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_library, container, false);
        rvRecipes = v.findViewById(R.id.rvRecipes);
        etSearch = v.findViewById(R.id.etSearch);
        rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }
        });

        loadRecipes();
        return v;
    }

    private void loadRecipes() {
        if (getContext() == null) return;

        VolleyHelper.getInstance(getContext()).getRecipes(new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if (!isAdded()) return;

                try {
                    recipeList.clear();
                    JSONArray dataArray;

                    if (response.has("data")) {
                        dataArray = response.getJSONArray("data");
                    } else if (response.has("recipes")) {
                        dataArray = response.getJSONArray("recipes");
                    } else {
                        return;
                    }

                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject obj = dataArray.getJSONObject(i);
                        Recipe r = new Recipe();
                        r.id = obj.optInt("id", 0);
                        r.title = obj.optString("title", "");
                        r.category = obj.optString("category", "");
                        r.prakriti = obj.optString("prakriti", "");
                        r.ingredients = obj.optString("ingredients", "");
                        r.method = obj.optString("method", "");
                        r.ayurvedic_notes = obj.optString("ayurvedic_notes", "");
                        r.image_url = obj.optString("image_url", "");
                        recipeList.add(r);
                    }

                    adapter = new RecipeAdapter(recipeList, getContext());
                    rvRecipes.setAdapter(adapter);

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error parsing recipes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                if (isAdded()) {
                    Toast.makeText(getContext(), "Failed to load recipes: " + error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
