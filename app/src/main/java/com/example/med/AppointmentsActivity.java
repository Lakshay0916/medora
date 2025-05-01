package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.med.models.Appointment;
import com.example.med.models.Patient;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        findViewById(R.id.fabAddAppointment).setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentsActivity.this, CreateAppointmentActivity.class);
            startActivity(intent);
        });


        recyclerView = findViewById(R.id.recyclerViewAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize with dummy data
        appointmentList = getDummyAppointments();

        adapter = new AppointmentAdapter(appointmentList);
        recyclerView.setAdapter(adapter);
    }

    private List<Appointment> getDummyAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        
        // Create some dummy appointments
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        
        try {
            Date date1 = dateFormat.parse("2023-06-15 10:30");
            Date date2 = dateFormat.parse("2023-06-15 14:00");
            Date date3 = dateFormat.parse("2023-06-16 09:15");
            
            appointments.add(new Appointment("1", "1", "1", date1, "Regular Checkup"));
            appointments.add(new Appointment("2", "2", "1", date2, "Follow-up"));
            appointments.add(new Appointment("3", "3", "1", date3, "Consultation"));
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
            holder.tvAppointmentTime.setText(dateFormat.format(appointment.getDateTime()));
            holder.tvAppointmentReason.setText(appointment.getReason());
            
            holder.btnViewPatient.setOnClickListener(v -> {
                Intent intent = new Intent(AppointmentsActivity.this, PatientProfileActivity.class);
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