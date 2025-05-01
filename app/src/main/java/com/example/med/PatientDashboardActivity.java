package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class PatientDashboardActivity extends AppCompatActivity {
    private CardView cardProfile, cardMedications, cardAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        // Initialize views
        cardProfile = findViewById(R.id.cardProfile);
        cardMedications = findViewById(R.id.cardMedications);
        cardAppointments = findViewById(R.id.cardAppointments);

        // Set click listeners
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboardActivity.this, PatientPersonalProfileActivity.class);
                startActivity(intent);
            }
        });

        cardMedications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboardActivity.this, PatientMedicationsActivity.class);
                startActivity(intent);
            }
        });

        cardAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientDashboardActivity.this, PatientAppointmentsActivity.class);
                startActivity(intent);
            }
        });
    }
}