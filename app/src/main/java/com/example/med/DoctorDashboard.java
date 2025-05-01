package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.med.models.Appointment;
import com.example.med.models.Doctor;
import com.example.med.models.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoctorDashboard extends AppCompatActivity {
    private CardView cardProfile, cardPatients, cardAppointments;
    private TextView tvDoctorName, tvSpecialization;
    private RecyclerView recyclerViewTodayAppointments;
    private TodayAppointmentsAdapter todayAppointmentsAdapter;
    private List<Appointment> todayAppointments;
    String name;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot ->
        {
            if (documentSnapshot.exists()){
                name ="Dr. "+ documentSnapshot.getString("name");
                Toast.makeText(DoctorDashboard.this,name,Toast.LENGTH_SHORT).show();
                TextView doctorName = findViewById(R.id.tvDoctorName);
                doctorName.setText(name);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        // Initialize views
        cardProfile = findViewById(R.id.cardProfile);
        cardPatients = findViewById(R.id.cardPatients);
        cardAppointments = findViewById(R.id.cardAppointments);

        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvSpecialization = findViewById(R.id.tvSpecialization);
        recyclerViewTodayAppointments = findViewById(R.id.recyclerViewTodayAppointments);

        // Set up doctor info
        Doctor doctor = getDummyDoctor();
        tvDoctorName.setText(doctor.getName());
        tvSpecialization.setText(doctor.getSpecialization());

        // Set up today's appointments
        recyclerViewTodayAppointments.setLayoutManager(new LinearLayoutManager(this));
        todayAppointments = getTodayAppointments();
        todayAppointmentsAdapter = new TodayAppointmentsAdapter(todayAppointments);
        recyclerViewTodayAppointments.setAdapter(todayAppointmentsAdapter);

        // Set click listeners
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this, DoctorProfileActivity.class);
                startActivity(intent);
            }
        });

        cardPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this, PatientListActivity.class);
                startActivity(intent);
            }
        });

        cardAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorDashboard.this, AppointmentsActivity.class);
                startActivity(intent);
            }
        });


    }

    private Doctor getDummyDoctor() {
        // This is just dummy data - in a real app, this would come from a database
        return new Doctor("1", "Dr. John Smith", "Cardiologist", "john.smith@hospital.com", "+1 (555) 123-4567");
    }

    private List<Appointment> getTodayAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        
        // Create some dummy appointments for today
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        
        try {
            Date date1 = dateFormat.parse("2023-06-15 10:30");
            Date date2 = dateFormat.parse("2023-06-15 14:00");
            
            appointments.add(new Appointment("1", "1", "1", date1, "Regular Checkup"));
            appointments.add(new Appointment("2", "2", "1", date2, "Follow-up"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return appointments;
    }

    private class TodayAppointmentsAdapter extends RecyclerView.Adapter<TodayAppointmentsAdapter.AppointmentViewHolder> {
        private List<Appointment> appointments;
        private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        public TodayAppointmentsAdapter(List<Appointment> appointments) {
            this.appointments = appointments;
        }

        @Override
        public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_appointment, parent, false);
            return new AppointmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AppointmentViewHolder holder, int position) {
            Appointment appointment = appointments.get(position);
            
            // Get patient name from ID (in a real app, this would come from a database)
            String patientName = getPatientName(appointment.getPatientId());
            
            holder.tvPatientName.setText(patientName);
            holder.tvAppointmentTime.setText(timeFormat.format(appointment.getDateTime()));
            holder.tvAppointmentReason.setText(appointment.getReason());
            
            holder.btnViewPatient.setOnClickListener(v -> {
                Intent intent = new Intent(DoctorDashboard.this, PatientProfileActivity.class);
                intent.putExtra("patientId", appointment.getPatientId());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return appointments.size();
        }

        class AppointmentViewHolder extends RecyclerView.ViewHolder {
            TextView tvPatientName, tvAppointmentTime, tvAppointmentReason;
            ImageButton btnViewPatient;

            AppointmentViewHolder(View itemView) {
                super(itemView);
                tvPatientName = itemView.findViewById(R.id.tvPatientName);
                tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
                tvAppointmentReason = itemView.findViewById(R.id.tvAppointmentReason);
                btnViewPatient = itemView.findViewById(R.id.btnViewPatient);
            }
        }
    }

    private String getPatientName(String patientId) {
        // This is just dummy data - in a real app, this would come from a database
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient("1", "John Doe", 35, "Male", "No major health issues"));
        patients.add(new Patient("2", "Jane Smith", 28, "Female", "Allergic to penicillin"));
        patients.add(new Patient("3", "Mike Johnson", 45, "Male", "Hypertension"));
        
        for (Patient patient : patients) {
            if (patient.getId().equals(patientId)) {
                return patient.getName();
            }
        }
        
        return "Unknown Patient";
    }
} 