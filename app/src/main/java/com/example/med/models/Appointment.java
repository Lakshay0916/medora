package com.example.med.models;

import java.util.Date;

public class Appointment {
    private String id;
    private String patientId;
    private String doctorId;
    private Date dateTime;
    private String reason;

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
} 