package com.example.med.models;

import java.util.List;

public class Patient {
    private String id;
    private String name;
    private int age;
    private String gender;
    private String medicalHistory;
    private List<Appointment> appointments;

    public Patient(String id, String name, int age, String gender, String medicalHistory) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.medicalHistory = medicalHistory;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getMedicalHistory() { return medicalHistory; }
    public List<Appointment> getAppointments() { return appointments; }
    
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
} 