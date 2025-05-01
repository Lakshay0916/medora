package com.example.med.models;

public class Doctor {
    private String id;
    private String name;
    private String specialization;
    private String email;
    private String phone;

    public Doctor(String id, String name, String specialization, String email, String phone) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.email = email;
        this.phone = phone;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
} 