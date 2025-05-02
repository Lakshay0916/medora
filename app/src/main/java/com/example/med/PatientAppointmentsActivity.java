package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.med.models.Appointment;
import com.example.med.models.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientAppointmentsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewAppointments;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> appointmentList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        patientId = user.getUid();

        // Initialize RecyclerView
        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));

        // Initialize appointment list
        appointmentList = new ArrayList<>();
        appointmentAdapter = new AppointmentAdapter(appointmentList);
        recyclerViewAppointments.setAdapter(appointmentAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchAppointments();
    }

    private void fetchAppointments() {
        db.collection("appointment")
                .whereEqualTo("patientId", patientId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        appointmentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String doctorId = document.getString("doctorId");
                            Date dateTime = document.getDate("dateTime");
                            String reason = document.getString("reason");
                            String notes = document.getString("notes");

                            if (dateTime != null) {
                                Appointment appointment = new Appointment(id, patientId, doctorId, dateTime, reason);
                                appointmentList.add(appointment);
                            }
                        }
                        appointmentAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to fetch appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
        private List<Appointment> appointments;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());

        public AppointmentAdapter(List<Appointment> appointments) {
            this.appointments = appointments;
        }

        @NonNull
        @Override
        public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_appointment, parent, false);
            return new AppointmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
            Appointment appointment = appointments.get(position);

            // Fetch doctor name from Firestore
            db.collection("users").document(appointment.getDoctorId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String doctorName = documentSnapshot.getString("name");
                            String specialization = documentSnapshot.getString("specialization");
                            holder.tvPatientName.setText("Dr. " + doctorName + " - " + specialization);
                        }
                    });

            holder.tvAppointmentTime.setText(dateFormat.format(appointment.getDateTime()));
            holder.tvAppointmentReason.setText(appointment.getReason());

            // Change the icon to indicate this is a doctor
            holder.btnViewPatient.setImageResource(R.drawable.doc1);
            holder.btnViewPatient.setContentDescription("View Doctor");

            holder.btnViewPatient.setOnClickListener(v -> {
                Intent intent = new Intent(PatientAppointmentsActivity.this, DoctorProfileActivity.class);
                intent.putExtra("doctorId", appointment.getDoctorId());
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