package com.example.med.models;

public class Medication {
    private String name;
    private String dosage;
    private String frequency;
    private String purpose;

    public Medication(String name, String dosage, String frequency, String purpose) {
        this.name = name;
        this.dosage = dosage;
        this.frequency = frequency;
        this.purpose = purpose;
    }

    public String getName() {
        return name;
    }

    public String getDosage() {
        return dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getPurpose() {
        return purpose;
    }
}