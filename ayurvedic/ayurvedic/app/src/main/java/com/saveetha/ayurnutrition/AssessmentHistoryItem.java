package com.saveetha.ayurnutrition;

/**
 * Model class for Assessment History items
 */
public class AssessmentHistoryItem {
    private int id;
    private int patientId;
    private String assessmentType; // "Prakriti", "Vikriti", "Agni"
    private String date;
    private String summary;

    // For Prakriti/Vikriti
    private int vata;
    private int pitta;
    private int kapha;

    // For Agni
    private String agniType;

    public AssessmentHistoryItem() {
    }

    public AssessmentHistoryItem(int id, int patientId, String assessmentType, String date) {
        this.id = id;
        this.patientId = patientId;
        this.assessmentType = assessmentType;
        this.date = date;
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

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
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

    public int getVata() {
        return vata;
    }

    public void setVata(int vata) {
        this.vata = vata;
    }

    public int getPitta() {
        return pitta;
    }

    public void setPitta(int pitta) {
        this.pitta = pitta;
    }

    public int getKapha() {
        return kapha;
    }

    public void setKapha(int kapha) {
        this.kapha = kapha;
    }

    public String getAgniType() {
        return agniType;
    }

    public void setAgniType(String agniType) {
        this.agniType = agniType;
    }

    public String getEmoji() {
        switch (assessmentType) {
            case "Prakriti":
                return "🧘";
            case "Vikriti":
                return "⚖️";
            case "Agni":
                return "🔥";
            default:
                return "📋";
        }
    }

    public String getFormattedSummary() {
        if ("Agni".equals(assessmentType)) {
            return agniType != null ? agniType : "Not assessed";
        } else {
            return "V:" + vata + "% P:" + pitta + "% K:" + kapha + "%";
        }
    }
}
