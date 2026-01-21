package com.saveetha.ayurnutrition;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class SettingsFragment extends Fragment {

    View layoutAnalytics, layoutAlerts, layoutTasks, layoutReports;
    View layoutSeasonalFoods, layoutPermissions, layoutAddFood;
    MaterialButton btnLogout;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        // Bind views
        layoutAnalytics = v.findViewById(R.id.layoutAnalytics);
        layoutAlerts = v.findViewById(R.id.layoutAlerts);
        layoutTasks = v.findViewById(R.id.layoutTasks);
        layoutReports = v.findViewById(R.id.layoutReports);
        layoutSeasonalFoods = v.findViewById(R.id.layoutSeasonalFoods);
        layoutPermissions = v.findViewById(R.id.layoutPermissions);
        layoutAddFood = v.findViewById(R.id.layoutAddFood);
        btnLogout = v.findViewById(R.id.btnLogout);

        // Configure cards with their content
        configureCard(layoutAnalytics, "📊", R.string.settings_analytics_title,
                R.string.settings_analytics_subtitle, R.drawable.chip_accent);
        configureCard(layoutAlerts, "🔔", R.string.settings_alerts_title,
                R.string.settings_alerts_subtitle, R.drawable.chip_warning);
        configureCard(layoutTasks, "✓", R.string.settings_tasks_title,
                R.string.settings_tasks_subtitle, R.drawable.chip_accent);
        configureCard(layoutReports, "📄", R.string.settings_reports_title,
                R.string.settings_reports_subtitle, R.drawable.chip_accent);
        configureCard(layoutSeasonalFoods, "🍂", R.string.settings_seasonal_foods_title,
                0, R.drawable.chip_accent);
        configureCard(layoutPermissions, "🔒", R.string.settings_permissions_title,
                0, R.drawable.chip_accent);
        configureCard(layoutAddFood, "+", R.string.settings_add_food_title,
                0, R.drawable.avatar_circle);

        // Set click listeners
        layoutAnalytics.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), AnalyticsActivity.class));
        });

        layoutAlerts.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), ClinicalAlertsActivity.class));
        });

        layoutTasks.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), TasksActivity.class));
        });

        layoutReports.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), GenerateReportActivity.class));
        });

        layoutSeasonalFoods.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SeasonalFoodsActivity.class));
        });

        layoutPermissions.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), PermissionsActivity.class));
        });

        layoutAddFood.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), AddCustomFoodActivity.class));
        });

        btnLogout.setOnClickListener(view -> {
            // Clear login session (but keep has_seen_splash)
            if (getActivity() != null) {
                getActivity().getSharedPreferences("ayur_prefs", 0).edit()
                        .putBoolean("is_logged_in", false)
                        .remove("user_email")
                        .remove("user_id")
                        .apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return v;
    }

    /**
     * Configure a settings card with icon, title, optional subtitle, and background
     */
    private void configureCard(View card, String icon, int titleRes, int subtitleRes, int iconBgRes) {
        if (card == null)
            return;

        TextView tvIcon = card.findViewById(R.id.tvIcon);
        TextView tvTitle = card.findViewById(R.id.tvTitle);
        TextView tvSubtitle = card.findViewById(R.id.tvSubtitle);
        FrameLayout iconContainer = card.findViewById(R.id.iconContainer);

        if (tvIcon != null) {
            tvIcon.setText(icon);
            // Special styling for text icons like "+" and "✓"
            if (icon.equals("+") || icon.equals("✓")) {
                tvIcon.setTextColor(0xFF0D2818);
                tvIcon.setTypeface(tvIcon.getTypeface(), android.graphics.Typeface.BOLD);
                if (icon.equals("+")) {
                    tvIcon.setTextSize(24);
                }
            }
        }

        if (tvTitle != null) {
            tvTitle.setText(titleRes);
        }

        if (tvSubtitle != null) {
            if (subtitleRes != 0) {
                tvSubtitle.setText(subtitleRes);
                tvSubtitle.setVisibility(View.VISIBLE);
            } else {
                tvSubtitle.setVisibility(View.GONE);
            }
        }

        if (iconContainer != null) {
            iconContainer.setBackgroundResource(iconBgRes);
        }
    }
}