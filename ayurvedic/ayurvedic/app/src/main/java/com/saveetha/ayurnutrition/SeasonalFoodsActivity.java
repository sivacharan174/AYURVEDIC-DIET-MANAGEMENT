package com.saveetha.ayurnutrition;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SeasonalFoodsActivity extends AppCompatActivity {

    ImageView btnBack;
    TextView tvCurrentSeason, tvSeasonDescription;
    Button btnSpring, btnSummer, btnMonsoon, btnAutumn, btnWinter;
    RecyclerView rvFoods;
    FoodAdapter adapter;
    List<Food> foodList = new ArrayList<>();
    String currentSeason = "winter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasonal_foods);

        btnBack = findViewById(R.id.btnBack);
        tvCurrentSeason = findViewById(R.id.tvCurrentSeason);
        tvSeasonDescription = findViewById(R.id.tvSeasonDescription);
        btnSpring = findViewById(R.id.btnSpring);
        btnSummer = findViewById(R.id.btnSummer);
        btnMonsoon = findViewById(R.id.btnMonsoon);
        btnAutumn = findViewById(R.id.btnAutumn);
        btnWinter = findViewById(R.id.btnWinter);
        rvFoods = findViewById(R.id.rvFoods);

        rvFoods.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodAdapter(this, foodList);
        rvFoods.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        // Season button handlers
        btnSpring.setOnClickListener(v -> selectSeason("spring"));
        btnSummer.setOnClickListener(v -> selectSeason("summer"));
        btnMonsoon.setOnClickListener(v -> selectSeason("monsoon"));
        btnAutumn.setOnClickListener(v -> selectSeason("autumn"));
        btnWinter.setOnClickListener(v -> selectSeason("winter"));

        // Detect current season
        detectCurrentSeason();
        selectSeason(currentSeason);
    }

    private void detectCurrentSeason() {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        if (month >= 2 && month <= 4) currentSeason = "spring";
        else if (month >= 5 && month <= 6) currentSeason = "summer";
        else if (month >= 7 && month <= 8) currentSeason = "monsoon";
        else if (month >= 9 && month <= 10) currentSeason = "autumn";
        else currentSeason = "winter";
    }

    private void selectSeason(String season) {
        currentSeason = season;
        updateSeasonUI();
        loadSeasonalFoods();
    }

    private void updateSeasonUI() {
        // Reset all button colors
        int inactiveColor = 0xFF2E5746;
        int activeColor = 0xFF4DC080;
        btnSpring.setBackgroundTintList(android.content.res.ColorStateList.valueOf(inactiveColor));
        btnSummer.setBackgroundTintList(android.content.res.ColorStateList.valueOf(inactiveColor));
        btnMonsoon.setBackgroundTintList(android.content.res.ColorStateList.valueOf(inactiveColor));
        btnAutumn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(inactiveColor));
        btnWinter.setBackgroundTintList(android.content.res.ColorStateList.valueOf(inactiveColor));

        // Update header and active button
        switch (currentSeason) {
            case "spring":
                btnSpring.setBackgroundTintList(android.content.res.ColorStateList.valueOf(activeColor));
                tvCurrentSeason.setText("Spring (Vasanta)");
                tvSeasonDescription.setText("Warming, dry season. Favor light, warm foods with pungent tastes.");
                break;
            case "summer":
                btnSummer.setBackgroundTintList(android.content.res.ColorStateList.valueOf(activeColor));
                tvCurrentSeason.setText("Summer (Grishma)");
                tvSeasonDescription.setText("Hot season. Favor cooling, hydrating foods with sweet tastes.");
                break;
            case "monsoon":
                btnMonsoon.setBackgroundTintList(android.content.res.ColorStateList.valueOf(activeColor));
                tvCurrentSeason.setText("Monsoon (Varsha)");
                tvSeasonDescription.setText("Wet season. Favor warm, digestible foods. Avoid raw foods.");
                break;
            case "autumn":
                btnAutumn.setBackgroundTintList(android.content.res.ColorStateList.valueOf(activeColor));
                tvCurrentSeason.setText("Autumn (Sharad)");
                tvSeasonDescription.setText("Cool, dry season. Favor sweet, bitter, astringent tastes.");
                break;
            case "winter":
                btnWinter.setBackgroundTintList(android.content.res.ColorStateList.valueOf(activeColor));
                tvCurrentSeason.setText("Winter (Hemanta)");
                tvSeasonDescription.setText("Cold season. Favor warm, heavy, nourishing foods.");
                break;
        }
    }

    private void loadSeasonalFoods() {
        String url = VolleyHelper.GET_SEASONAL_FOODS_URL + "?season=" + currentSeason;

        VolleyHelper.getInstance(this).getRequest(url, new VolleyHelper.ApiCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    foodList.clear();
                    JSONArray arr = response.optJSONArray("data");
                    if (arr != null) {
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            Food f = new Food();
                            f.id = obj.optInt("id");
                            f.name = obj.optString("name");
                            f.category = obj.optString("category");
                            f.calories = obj.optInt("calories");
                            f.rasa = obj.optString("rasa");
                            f.season = obj.optString("season");
                            foodList.add(f);
                        }
                    }
                    adapter.setFoods(foodList);
                } catch (Exception e) {
                    Toast.makeText(SeasonalFoodsActivity.this, "Error loading foods", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(SeasonalFoodsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
