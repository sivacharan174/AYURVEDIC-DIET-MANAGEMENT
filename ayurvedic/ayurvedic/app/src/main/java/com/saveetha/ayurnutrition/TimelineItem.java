package com.saveetha.ayurnutrition;

/**
 * Model class for Timeline items - consolidates all patient activities
 */
public class TimelineItem implements Comparable<TimelineItem> {
    private int id;
    private int patientId;
    private String category; // "Assessment", "Lifestyle", "Medical", "Diet"
    private String type; // Specific type like "Prakriti", "Vikriti", etc.
    private String emoji;
    private String fullDate; // Full datetime for sorting
    private String date; // Display date
    private String time; // Display time
    private String summary;
    private String jsonData;

    public TimelineItem() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getFullDate() {
        return fullDate;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public int compareTo(TimelineItem other) {
        // Sort by full date descending (newest first)
        if (fullDate == null)
            return 1;
        if (other.fullDate == null)
            return -1;
        return other.fullDate.compareTo(this.fullDate);
    }
}
