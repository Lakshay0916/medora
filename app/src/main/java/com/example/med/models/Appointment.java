package com.example.med.models;

import java.util.Date;

public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private Date dateTime;
    private String reason;

    // Required no-argument constructor for Firestore
    public Appointment() {}

    public Appointment(String id, String patientId, String doctorId, Date dateTime, String reason) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.reason = reason;
    }

    // Getters
    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public Date getDateTime() { return dateTime; }
    public String getReason() { return reason; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }
    public void setReason(String reason) { this.reason = reason; }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", patientId='" + patientId + '\'' +
                ", doctorId='" + doctorId + '\'' +
                ", dateTime=" + dateTime +
                ", reason='" + reason + '\'' +
                '}';
    }
} 