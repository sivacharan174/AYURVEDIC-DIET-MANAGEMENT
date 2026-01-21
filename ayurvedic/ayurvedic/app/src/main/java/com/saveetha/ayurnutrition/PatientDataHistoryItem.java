package com.saveetha.ayurnutrition;

/**
 * Model class for Patient Data History items (Lifestyle, Medical History,
 * Dietary Habits)
 */
public class PatientDataHistoryItem {
    private int id;
    private int patientId;
    private String dataType; // "Lifestyle", "Medical History", "Dietary Habits"
    private String date;
    private String summary;

    // Store raw JSON data for detail view
    private String jsonData;

    public PatientDataHistoryItem() {
    }

    public PatientDataHistoryItem(int id, int patientId, String dataType, String date, String summary) {
        this.id = id;
        this.patientId = patientId;
        this.dataType = dataType;
        this.date = date;
        this.summary = summary;
    }

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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getEmoji() {
        switch (dataType) {
            case "Lifestyle":
                return "🏃";
            case "Medical History":
                return "🏥";
            case "Dietary Habits":
                return "🍽️";
            default:
                return "📋";
        }
    }
}
