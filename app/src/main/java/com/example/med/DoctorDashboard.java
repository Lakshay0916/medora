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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoctorDashboard extends AppCompatActivity {
    private CardView cardProfile, cardPatients, cardAppointments;
    private TextView tvDoctorName, tvSpecialization;
    private RecyclerView recyclerViewTodayAppointments;
    private TodayAppointmentsAdapter todayAppointmentsAdapter;
    private List<Appointment> todayAppointments;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String name;

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String uid = user.getUid();
        
        // Fetch doctor's name
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                name = "Dr. " + documentSnapshot.getString("name");
                TextView doctorName = findViewById(R.id.tvDoctorName);
                doctorName.setText(name);
            }
        });

        // Fetch today's appointments
        fetchTodayAppointments(uid);
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

        // Set up RecyclerView
        recyclerViewTodayAppointments.setLayoutManager(new LinearLayoutManager(this));
        todayAppointments = new ArrayList<>();
        todayAppointmentsAdapter = new TodayAppointmentsAdapter(todayAppointments);
        recyclerViewTodayAppointments.setAdapter(todayAppointmentsAdapter);

        // Set click listeners
        cardProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDashboard.this, DoctorProfileActivity.class);
            startActivity(intent);
        });

        cardPatients.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDashboard.this, PatientListActivity.class);
            startActivity(intent);
        });

        cardAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDashboard.this, AppointmentsActivity.class);
            startActivity(intent);
        });
    }

    private void fetchTodayAppointments(String doctorId) {
        // Get today's date at midnight in local timezone
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date today = calendar.getTime();

        // Get tomorrow's date at midnight in local timezone
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();

        // Log the query parameters
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = dateFormat.format(today);
        Toast.makeText(this, "Fetching appointments for: " + todayStr, Toast.LENGTH_SHORT).show();

        // Query appointments for today
        db.collection("appointment")
            .whereEqualTo("doctorId", doctorId)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                todayAppointments.clear();
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(this, "No appointments found", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    try {
                        Appointment appointment = document.toObject(Appointment.class);
                        appointment.setId(document.getId());
                        
                        // Get the appointment date
                        Date appointmentDate = appointment.getDateTime();
                        
                        // Check if the appointment is today
                        if (isSameDay(appointmentDate, today)) {
                            todayAppointments.add(appointment);
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "Error parsing appointment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                
                // Sort appointments by time
                Collections.sort(todayAppointments, (a1, a2) -> a1.getDateTime().compareTo(a2.getDateTime()));
                
                todayAppointmentsAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Found " + todayAppointments.size() + " appointments for today", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to fetch appointments: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
    }

    private boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
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
            
            // Fetch patient name from Firestore
            db.collection("users").document(appointment.getPatientId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String patientName = documentSnapshot.getString("name");
                        holder.tvPatientName.setText(patientName);
                    }
                })
                .addOnFailureListener(e -> {
                    holder.tvPatientName.setText("Unknown Patient");
                });
            
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
} 