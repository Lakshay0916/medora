package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.Calendar;
import java.util.Date;

public class PatientDashboardActivity extends AppCompatActivity {
    private CardView cardProfile, cardMedications, cardAppointments;
    private TextView tvWelcome, tvAppointmentCount, tvMedicationCount;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize views
        cardProfile = findViewById(R.id.cardProfile);
        cardMedications = findViewById(R.id.cardMedications);
        cardAppointments = findViewById(R.id.cardAppointments);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvAppointmentCount = findViewById(R.id.tvAppointmentCount);
        tvMedicationCount = findViewById(R.id.tvMedicationCount);

        // Set click listeners
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboardActivity.this, PatientPersonalProfileActivity.class);
            startActivity(intent);
        });

        cardMedications.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboardActivity.this, PatientMedicationsActivity.class);
            startActivity(intent);
        });

        cardAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(PatientDashboardActivity.this, PatientAppointmentsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchPatientData();
        fetchAppointmentCount();
        fetchMedicationCount();
    }

    private void fetchPatientData() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = user.getUid();
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        tvWelcome.setText("Welcome, " + name);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch patient data", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchAppointmentCount() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        // Get current date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date today = calendar.getTime();

        db.collection("appointment")
                .whereEqualTo("patientId", userId)
                .whereGreaterThanOrEqualTo("dateTime", today)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = task.getResult().size();
                        tvAppointmentCount.setText(count + " upcoming appointments");
                    } else {
                        Toast.makeText(this, "Failed to fetch appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchMedicationCount() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        String userId = user.getUid();
        db.collection("medications")
                .whereEqualTo("patientId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = task.getResult().size();
                        tvMedicationCount.setText(count + " medications");
                    } else {
                        Toast.makeText(this, "Failed to fetch medications", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}