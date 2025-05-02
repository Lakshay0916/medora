package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.med.models.Doctor;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class DoctorProfileActivity extends AppCompatActivity {
    private ImageView imgDoctorProfile;
    private TextView tvDoctorName, tvSpecialization, tvEmail, tvPhone, tvExperience, tvEducation, tvCertifications;
    private String name, education, email, mobile, specialization, certifications, experience, uid;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot ->
        {
           if (documentSnapshot.exists()){
               name = documentSnapshot.getString("name");
               education = documentSnapshot.getString("education");
               email = documentSnapshot.getString("email");
               mobile = documentSnapshot.getString("mobile");
               specialization = documentSnapshot.getString("specialization");
               certifications = documentSnapshot.getString("certifications");
               experience = documentSnapshot.getString("experience");
//               Toast.makeText(DoctorProfileActivity.this,name,Toast.LENGTH_SHORT).show();
               TextView doctorName = findViewById(R.id.tvDoctorName);
               doctorName.setText(name);
               TextView specialization1 = findViewById(R.id.tvSpecialization);
               specialization1.setText(specialization);
               TextView email1 = findViewById(R.id.tvEmail);
               email1.setText(email);
               TextView phone1 = findViewById(R.id.tvPhone);
               phone1.setText("+91 "+mobile);
               TextView education1 = findViewById(R.id.tvEducation);
               education1.setText(education);
               TextView experience1 = findViewById(R.id.tvExperience);
               experience1.setText(experience + " years");
               TextView cert1 = findViewById(R.id.tvCertifications);
               cert1.setText(certifications);

           }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        // Initialize views
        imgDoctorProfile = findViewById(R.id.imgDoctorProfile);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvSpecialization = findViewById(R.id.tvSpecialization);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvExperience = findViewById(R.id.tvExperience);
        tvEducation = findViewById(R.id.tvEducation);
        tvCertifications = findViewById(R.id.tvCertifications);

        // Get doctor data (in a real app, this would come from a database)
        Doctor doctor = getDummyDoctor();

        // Display doctor information
        tvDoctorName.setText(doctor.getName());
        tvSpecialization.setText(specialization);
        tvEmail.setText(doctor.getEmail());
        tvPhone.setText(doctor.getPhone());
        tvExperience.setText("Experience: " + experience + " years");
        tvEducation.setText("Education: "+ education);
        tvCertifications.setText("Certifications: "+ certifications);

        Button logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(DoctorProfileActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private Doctor getDummyDoctor() {
        // This is just dummy data - in a real app, this would come from a database
        return new Doctor(uid, name, specialization, email, "+91 "+ mobile);
    }
} 