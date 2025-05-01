package com.example.med;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.med.models.Patient;
import java.util.ArrayList;

public class PatientProfileActivity extends AppCompatActivity {
    private TextView tvName, tvAge, tvGender, tvMedicalHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        // Initialize views
        tvName = findViewById(R.id.tvName);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvMedicalHistory = findViewById(R.id.tvMedicalHistory);

        // Get patient ID from intent
        String patientId = getIntent().getStringExtra("patientId");

        // TODO: In a real app, fetch patient data from a database
        // For now, we'll use dummy data
        Patient patient = getDummyPatient(patientId);

        // Display patient information
        tvName.setText("Name: " + patient.getName());
        tvAge.setText("Age: " + patient.getAge());
        tvGender.setText("Gender: " + patient.getGender());
        tvMedicalHistory.setText("Medical History: " + patient.getMedicalHistory());
    }

    private Patient getDummyPatient(String patientId) {
        // This is just dummy data - in a real app, this would come from a database
        ArrayList<Patient> patients = new ArrayList<>();
        patients.add(new Patient("1", "John Doe", 35, "Male", "No major health issues"));
        patients.add(new Patient("2", "Jane Smith", 28, "Female", "Allergic to penicillin"));
        patients.add(new Patient("3", "Mike Johnson", 45, "Male", "Hypertension"));

        for (Patient patient : patients) {
            if (patient.getId().equals(patientId)) {
                return patient;
            }
        }
        return patients.get(0); // Return first patient if ID not found
    }
} 