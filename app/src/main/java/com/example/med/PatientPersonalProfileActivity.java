package com.example.med;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientPersonalProfileActivity extends AppCompatActivity {
    private ImageView imageAvatar;
    private TextView textName, textAge, textGender;
    private TextView textEmail, textPhone, textAddress;
    private TextView textMedicalHistory;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_personal_profile);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize views
        imageAvatar = findViewById(R.id.imgPatientAvatar);
        textName = findViewById(R.id.tvName);
        textAge = findViewById(R.id.tvAge);
        textGender = findViewById(R.id.tvGender);
        textEmail = findViewById(R.id.tvEmail);
        textPhone = findViewById(R.id.tvPhone);
        textAddress = findViewById(R.id.tvAddress);
        textMedicalHistory = findViewById(R.id.tvMedicalHistory);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchPatientData();
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
                        // Set patient information
                        textName.setText(documentSnapshot.getString("name"));
                        textAge.setText(documentSnapshot.getString("age") + " years");
                        textGender.setText(documentSnapshot.getString("gender"));
                        textEmail.setText(documentSnapshot.getString("email"));
                        textPhone.setText(documentSnapshot.getString("mobile"));
                        textAddress.setText(documentSnapshot.getString("address"));
                        textMedicalHistory.setText(documentSnapshot.getString("medicalHistory"));
                    } else {
                        Toast.makeText(this, "Patient data not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch patient data", Toast.LENGTH_SHORT).show();
                });
    }
}