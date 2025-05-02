//package com.example.med;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.med.models.Appointment;
//import com.example.med.models.Patient;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//public class AppointmentsActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private AppointmentAdapter adapter;
//    private List<Appointment> appointmentList;
//    private FirebaseFirestore db;
//    private FirebaseAuth auth;
//    private String doctorId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_appointments);
//
//        // Initialize Firebase
//        db = FirebaseFirestore.getInstance();
//        auth = FirebaseAuth.getInstance();
//        FirebaseUser user = auth.getCurrentUser();
//        if (user == null) {
//            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
//            finish();
//            return;
//        }
//        doctorId = user.getUid();
//
//        // Initialize views
//        recyclerView = findViewById(R.id.recyclerViewAppointments);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Initialize appointment list
//        appointmentList = new ArrayList<>();
//        adapter = new AppointmentAdapter(appointmentList);
//        recyclerView.setAdapter(adapter);
//
//        // Set up FAB
//        FloatingActionButton fabAddAppointment = findViewById(R.id.fabAddAppointment);
//        fabAddAppointment.setOnClickListener(v -> {
//            Intent intent = new Intent(AppointmentsActivity.this, CreateAppointmentActivity.class);
//            startActivity(intent);
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        fetchAppointments();
//    }
//
//    private void fetchAppointments() {
//        db.collection("appointment")
//                .whereEqualTo("doctorId", doctorId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        appointmentList.clear();
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            String id = document.getId();
//                            String patientId = document.getString("patientId");
//                            Date dateTime = document.getDate("dateTime");
//                            String reason = document.getString("reason");
//                            String notes = document.getString("notes");
//
//                            if (dateTime != null) {
//                                Appointment appointment = new Appointment(id, patientId, doctorId, dateTime, reason);
//                                appointmentList.add(appointment);
//                            }
//                        }
//                        adapter.notifyDataSetChanged();
//                    } else {
//                        Toast.makeText(this, "Failed to fetch appointments", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
//        private List<Appointment> appointments;
//        private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
//
//        public AppointmentAdapter(List<Appointment> appointments) {
//            this.appointments = appointments;
//        }
//
//        @Override
//        public AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_appointment, parent, false);
//            return new AppointmentViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(AppointmentViewHolder holder, int position) {
//            Appointment appointment = appointments.get(position);
//
//            // Fetch patient name from Firestore
//            db.collection("users").document(appointment.getPatientId())
//                    .get()
//                    .addOnSuccessListener(documentSnapshot -> {
//                        if (documentSnapshot.exists()) {
//                            String patientName = documentSnapshot.getString("name");
//                            holder.tvPatientName.setText(patientName);
//                        }
//                    });
//
//            holder.tvAppointmentTime.setText(dateFormat.format(appointment.getDateTime()));
//            holder.tvAppointmentReason.setText(appointment.getReason());
//
//            holder.btnViewPatient.setOnClickListener(v -> {
//                Intent intent = new Intent(AppointmentsActivity.this, PatientProfileActivity.class);
//                intent.putExtra("patientId", appointment.getPatientId());
//                startActivity(intent);
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return appointments.size();
//        }
//
//        class AppointmentViewHolder extends RecyclerView.ViewHolder {
//            TextView tvPatientName, tvAppointmentTime, tvAppointmentReason;
//            ImageButton btnViewPatient;
//
//            AppointmentViewHolder(View itemView) {
//                super(itemView);
//                tvPatientName = itemView.findViewById(R.id.tvPatientName);
//                tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
//                tvAppointmentReason = itemView.findViewById(R.id.tvAppointmentReason);
//                btnViewPatient = itemView.findViewById(R.id.btnViewPatient);
//            }
//        }
//    }
//}


package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.med.models.Appointment;
import com.example.med.models.Patient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        doctorId = user.getUid();

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize appointment list
        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(appointmentList);
        recyclerView.setAdapter(adapter);

        // Set up FAB
        FloatingActionButton fabAddAppointment = findViewById(R.id.fabAddAppointment);
        fabAddAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentsActivity.this, CreateAppointmentActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchAppointments();
    }

    private void fetchAppointments() {
        db.collection("appointment")
                .whereEqualTo("doctorId", doctorId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        appointmentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String patientId = document.getString("patientId");
                            Date dateTime = document.getDate("dateTime");
                            String reason = document.getString("reason");
                            String notes = document.getString("notes");

                            if (dateTime != null) {
                                Appointment appointment = new Appointment(id, patientId, doctorId, dateTime, reason);
                                appointmentList.add(appointment);
                            }
                        }
                        adapter.notifyDataSetChanged();
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
                    });

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
}