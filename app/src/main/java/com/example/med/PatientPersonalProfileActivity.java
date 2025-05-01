package com.example.med;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PatientPersonalProfileActivity extends AppCompatActivity {
    private ImageView imageAvatar;
    private TextView textName, textAge, textGender;
    private TextView textEmail, textPhone, textAddress;
    private TextView textMedicalHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_personal_profile);

        // Initialize views
        imageAvatar = findViewById(R.id.imgPatientAvatar);
        textName = findViewById(R.id.tvName);
        textAge = findViewById(R.id.tvAge);
        textGender = findViewById(R.id.tvGender);
        textEmail = findViewById(R.id.tvEmail);
        textPhone = findViewById(R.id.tvPhone);
        textAddress = findViewById(R.id.tvAddress);
        textMedicalHistory = findViewById(R.id.tvMedicalHistory);

        // Set dummy data
        textName.setText("John Doe");
        textAge.setText("35 years");
        textGender.setText("Male");
        textEmail.setText("john.doe@example.com");
        textPhone.setText("+1 234 567 8900");
        textAddress.setText("123 Main St, City, State 12345");
        textMedicalHistory.setText("No major health issues. Regular check-ups only.");
    }
}