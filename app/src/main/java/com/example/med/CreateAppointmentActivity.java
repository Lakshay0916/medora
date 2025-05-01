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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

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
        // TODO: Replace with actual patient list from database
        String[] patients = {"John Doe", "Jane Smith", "Mike Johnson"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, patients);
        spinnerPatient.setAdapter(adapter);
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
        // TODO: Implement the actual appointment saving logic here
        // This could involve saving to a local database or sending to a server

        // For now, just show a success message
        Toast.makeText(this, "Appointment created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}