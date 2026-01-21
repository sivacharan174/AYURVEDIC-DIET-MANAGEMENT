package com.saveetha.ayurnutrition;

public class MedicalRecord {
    public int id;
    public int patientId;
    public String visitDate;
    public String visitType; // "Initial", "Follow-up", "Emergency"
    public String reason;
    public String diagnosis;
    public String prescription;
    public String notes;
    public String createdAt;

    public MedicalRecord() {
    }

    public MedicalRecord(int id, int patientId, String visitDate, String visitType,
            String reason, String diagnosis, String prescription, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.visitDate = visitDate;
        this.visitType = visitType;
        this.reason = reason;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
    }
}
