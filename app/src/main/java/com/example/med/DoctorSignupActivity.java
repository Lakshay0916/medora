package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class DoctorSignupActivity extends AppCompatActivity {
    private TextInputEditText etName, etEmail, etPhone, etPassword, etSpecialization, 
        etLicenseNumber, etExperience, etEducation, etCertifications;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etSpecialization = findViewById(R.id.etSpecialization);
        etLicenseNumber = findViewById(R.id.etLicenseNumber);
        etExperience = findViewById(R.id.etExperience);
        etEducation = findViewById(R.id.etEducation);
        etCertifications = findViewById(R.id.etCertifications);
        progressBar = findViewById(R.id.progressBar);

        // Set up register button click listener
        findViewById(R.id.btnRegister).setOnClickListener(v -> registerDoctor());
    }

    private void registerDoctor() {
        // Get input values
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String specialization = etSpecialization.getText().toString().trim();
        String licenseNumber = etLicenseNumber.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String education = etEducation.getText().toString().trim();
        String certifications = etCertifications.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required");
            return;
        }
        if (phone.length() < 10) {
            etPhone.setError("Please enter a valid phone number");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }
        if (TextUtils.isEmpty(specialization)) {
            etSpecialization.setError("Specialization is required");
            return;
        }
        if (TextUtils.isEmpty(licenseNumber)) {
            etLicenseNumber.setError("License number is required");
            return;
        }
        if (TextUtils.isEmpty(experience)) {
            etExperience.setError("Experience is required");
            return;
        }
        if (TextUtils.isEmpty(education)) {
            etEducation.setError("Education is required");
            return;
        }
        if (TextUtils.isEmpty(certifications)) {
            etCertifications.setError("Certifications are required");
            return;
        }

        // Show progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Create user with email and password
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User registration successful
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Create doctor data
                            Map<String, Object> doctor = new HashMap<>();
                            doctor.put("name", name);
                            doctor.put("email", email);
                            doctor.put("phone", phone);
                            doctor.put("specialization", specialization);
                            doctor.put("licenseNumber", licenseNumber);
                            doctor.put("experience", experience);
                            doctor.put("education", education);
                            doctor.put("certifications", certifications);
                            doctor.put("role", "doctor");

                            // Save doctor data to Firestore
                            db.collection("users").document(user.getUid())
                                    .set(doctor)
                                    .addOnSuccessListener(aVoid -> {
                                        // Registration successful
                                        Toast.makeText(DoctorSignupActivity.this, 
                                            "Registration successful!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(DoctorSignupActivity.this, 
                                            DoctorDashboard.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle Firestore error
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(DoctorSignupActivity.this, 
                                            "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Registration failed
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DoctorSignupActivity.this, 
                            "Registration failed: " + task.getException().getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                });
    }
} 