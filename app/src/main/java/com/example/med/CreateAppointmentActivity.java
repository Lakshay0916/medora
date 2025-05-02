package com.example.med;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class CreateAppointmentActivity extends AppCompatActivity {

    private AutoCompleteTextView spinnerPatient;
    private TextInputEditText etDate;
    private TextInputEditText etTime;
    private TextInputEditText etPurpose;
    private TextInputEditText etNotes;
    private MaterialButton btnCreateAppointment;

    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    private List<String> patientNames = new ArrayList<>();
    private List<String> patientIds = new ArrayList<>();
    private String selectedPatientId = null;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        initializeViews();
        setupPatientSpinner();
        setupDatePicker();
        setupTimePicker();
        setupSubmitButton();
    }

    private void initializeViews() {
        spinnerPatient = findViewById(R.id.spinnerPatient);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        etPurpose = findViewById(R.id.etPurpose);
        etNotes = findViewById(R.id.etNotes);
        btnCreateAppointment = findViewById(R.id.btnCreateAppointment);
    }

    private void setupPatientSpinner() {
        // Fetch patients from Firestore
        db.collection("users")
                .whereEqualTo("role", "patient")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            patientNames.clear();
                            patientIds.clear();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                String name = snapshot.getString("name");
                                String id = snapshot.getId();
                                patientNames.add(name);
                                patientIds.add(id);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateAppointmentActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, patientNames);
                            spinnerPatient.setAdapter(adapter);
                        }
                    }
                });

        spinnerPatient.setOnItemClickListener((parent, view, position, id) -> {
            selectedPatientId = patientIds.get(position);
        });
    }

    private void setupDatePicker() {
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(dateFormatter.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void setupTimePicker() {
        etTime.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        etTime.setText(timeFormatter.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        });
    }

    private void setupSubmitButton() {
        btnCreateAppointment.setOnClickListener(v -> {
            if (validateForm()) {
                saveAppointment();
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validate Patient Selection
        if (TextUtils.isEmpty(spinnerPatient.getText())) {
            spinnerPatient.setError("Please select a patient");
            isValid = false;
        }

        // Validate Date
        if (TextUtils.isEmpty(etDate.getText())) {
            etDate.setError("Please select a date");
            isValid = false;
        }

        // Validate Time
        if (TextUtils.isEmpty(etTime.getText())) {
            etTime.setError("Please select a time");
            isValid = false;
        }

        // Validate Purpose
        if (TextUtils.isEmpty(etPurpose.getText())) {
            etPurpose.setError("Please enter the purpose of visit");
            isValid = false;
        }

        return isValid;
    }

    private void saveAppointment() {
        // Get doctor ID
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }
        String doctorId = user.getUid();

        // Get patient ID
        if (selectedPatientId == null) {
            int pos = patientNames.indexOf(spinnerPatient.getText().toString());
            if (pos != -1) {
                selectedPatientId = patientIds.get(pos);
            } else {
                Toast.makeText(this, "Invalid patient selected", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Get date and time
        String dateStr = etDate.getText().toString();
        String timeStr = etTime.getText().toString();
        String purpose = etPurpose.getText().toString();
        String notes = etNotes.getText() != null ? etNotes.getText().toString() : "";

        // Combine date and time into a Date object
        java.util.Date dateTime;
        try {
            SimpleDateFormat fullFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            dateTime = fullFormat.parse(dateStr + " " + timeStr);
            
            // Verify the date is not in the past
            if (dateTime.before(new Date())) {
                Toast.makeText(this, "Cannot create appointment in the past", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Invalid date/time format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare appointment data
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("doctorId", doctorId);
        appointment.put("patientId", selectedPatientId);
        appointment.put("dateTime", dateTime);
        appointment.put("reason", purpose);
        appointment.put("notes", notes);
        appointment.put("status", "scheduled"); // Add status field
        appointment.put("createdAt", new Date()); // Add creation timestamp

        // Save to Firestore
        db.collection("appointment")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(CreateAppointmentActivity.this, 
                            "Appointment created successfully for " + dateFormatter.format(dateTime), 
                            Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CreateAppointmentActivity.this, 
                            "Failed to create appointment: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
    }
}