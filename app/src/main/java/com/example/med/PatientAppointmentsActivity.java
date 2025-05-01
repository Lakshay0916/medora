package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.med.models.Appointment;
import com.example.med.models.Doctor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatientAppointmentsActivity extends AppCompatActivity {
    private RecyclerView recyclerViewAppointments;
    private AppointmentAdapter appointmentAdapter;
    private List<Appointment> appointmentList;
    private String patientId = "1"; // In a real app, this would come from user authentication

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments);

        // Initialize RecyclerView
        recyclerViewAppointments = findViewById(R.id.recyclerViewAppointments);
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(this));

        // Get appointments for this patient
        appointmentList = getPatientAppointments(patientId);

        // Set up adapter
        appointmentAdapter = new AppointmentAdapter(appointmentList);
        recyclerViewAppointments.setAdapter(appointmentAdapter);
    }

    private List<Appointment> getPatientAppointments(String patientId) {
        List<Appointment> appointments = new ArrayList<>();

        // Create some dummy appointments for this patient
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        try {
            Date date1 = dateFormat.parse("2023-06-15 10:30");
            Date date2 = dateFormat.parse("2023-06-20 14:00");
            Date date3 = dateFormat.parse("2023-06-25 09:15");

            appointments.add(new Appointment("1", patientId, "1", date1, "Regular Checkup"));
            appointments.add(new Appointment("2", patientId, "1", date2, "Follow-up Consultation"));
            appointments.add(new Appointment("3", patientId, "2", date3, "Specialist Review"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return appointments;
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

            // Get doctor name from ID (in a real app, this would come from a database)
            String doctorName = getDoctorName(appointment.getDoctorId());

            holder.tvPatientName.setText("Dr. " + doctorName);
            holder.tvAppointmentTime.setText(dateFormat.format(appointment.getDateTime()));
            holder.tvAppointmentReason.setText(appointment.getReason());

            // Change the icon to indicate this is a doctor
            holder.btnViewPatient.setImageResource(R.drawable.doc1);
            holder.btnViewPatient.setContentDescription("View Doctor");

            holder.btnViewPatient.setOnClickListener(v -> {
                // In a real app, this would navigate to the doctor's profile
                // For now, we'll just show a toast or navigate to a placeholder
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

    private String getDoctorName(String doctorId) {
        // This is just dummy data - in a real app, this would come from a database
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("1", "John Smith", "Cardiologist", "john.smith@hospital.com", "+1 (555) 123-4567"));
        doctors.add(new Doctor("2", "Sarah Johnson", "Dermatologist", "sarah.johnson@hospital.com", "+1 (555) 234-5678"));

        for (Doctor doctor : doctors) {
            if (doctor.getId().equals(doctorId)) {
                return doctor.getName();
            }
        }

        return "Unknown Doctor";
    }
}