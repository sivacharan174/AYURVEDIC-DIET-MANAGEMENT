package com.saveetha.ayurnutrition;

/**
 * Model class for Diet Plan/Chart
 */
public class DietPlan {
    public int id;
    public int patientId;
    public String duration;
    public String goal;
    public int targetCalories;
    public String notes;
    public String mealItems; // JSON string containing meal structure
    public String status;
    public String createdAt;

    public DietPlan() {
    }

    public String getFormattedDate() {
        if (createdAt == null || createdAt.isEmpty()) {
            return "Unknown date";
        }
        // Format: 2025-01-07 10:30:00 -> Jan 7, 2025
        try {
            String[] parts = createdAt.split(" ")[0].split("-");
            String[] months = { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
            int month = Integer.parseInt(parts[1]) - 1;
            int day = Integer.parseInt(parts[2]);
            return months[month] + " " + day + ", " + parts[0];
        } catch (Exception e) {
            return createdAt;
        }
    }

    public String getGoalEmoji() {
        if (goal == null)
            return "🥗";
        switch (goal.toLowerCase()) {
            case "weight loss":
                return "⚖️";
            case "weight gain":
                return "💪";
            case "maintenance":
                return "✅";
            case "dosha balancing":
                return "☯️";
            case "therapeutic":
                return "💊";
            default:
                return "🥗";
        }
    }

    public String getStatusColor() {
        if ("active".equalsIgnoreCase(status)) {
            return "#00E676"; // Green
        }
        return "#9E9E9E"; // Gray for completed/inactive
    }
}
