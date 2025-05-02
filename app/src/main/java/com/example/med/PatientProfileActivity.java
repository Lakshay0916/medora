package com.example.med;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientProfileActivity extends AppCompatActivity {
    private TextView tvName, tvAge, tvGender, tvMedicalHistory, tvEmail, tvMobile, tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);

        // Link UI elements
        tvName = findViewById(R.id.tvName);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvMedicalHistory = findViewById(R.id.tvMedicalHistory);
        tvEmail = findViewById(R.id.tvEmail);
        tvMobile = findViewById(R.id.tvMobile);
        tvAddress = findViewById(R.id.tvAddress);

        String patientId = getIntent().getStringExtra("patientId");

        if (patientId == null || patientId.isEmpty()) {
            Toast.makeText(this, "Invalid Patient ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchPatientFromFirestore(patientId);
    }

    private void fetchPatientFromFirestore(String patientId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(patientId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        if (document.exists()) {
                            tvName.setText("Name: " + document.getString("name"));
                            tvAge.setText("Age: " + document.getString("age"));
                            tvGender.setText("Gender: " + document.getString("gender"));
                            tvMedicalHistory.setText("Medical History: " + document.getString("medicalHistory"));
                            tvEmail.setText("Email: " + document.getString("email"));
                            tvMobile.setText("Mobile: " + document.getString("mobile"));
                            tvAddress.setText("Address: " + document.getString("address"));
                        } else {
                            Toast.makeText(PatientProfileActivity.this, "Patient not found", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PatientProfileActivity.this, "Failed to load patient", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
