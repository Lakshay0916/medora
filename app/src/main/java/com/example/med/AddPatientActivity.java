package com.example.med;

import android.app.DatePickerDialog;
import android.os.Bundle;   
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddPatientActivity extends AppCompatActivity {

    private TextInputEditText etFirstName;
    private TextInputEditText etLastName;
    private TextInputEditText etDateOfBirth;
    private AutoCompleteTextView spinnerGender;
    private TextInputEditText etEmail;
    private TextInputEditText etPhone;
    private TextInputEditText etAddress;
    private AutoCompleteTextView spinnerBloodGroup;
    private TextInputEditText etAllergies;
    private TextInputEditText etMedicalHistory;
    private MaterialButton btnAddPatient;

    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        initializeViews();
        setupSpinners();
        setupDatePicker();
        setupSubmitButton();
    }

    private void initializeViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etDateOfBirth = findViewById(R.id.etDateOfBirth);
        spinnerGender = findViewById(R.id.spinnerGender);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        etAllergies = findViewById(R.id.etAllergies);
        etMedicalHistory = findViewById(R.id.etMedicalHistory);
        btnAddPatient = findViewById(R.id.btnAddPatient);
    }

    private void setupSpinners() {
        String[] genders = {"Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, genders);
        spinnerGender.setAdapter(genderAdapter);

        String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, bloodGroups);
        spinnerBloodGroup.setAdapter(bloodGroupAdapter);
    }

    private void setupDatePicker() {
        etDateOfBirth.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDateOfBirth.setText(dateFormatter.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void setupSubmitButton() {
        btnAddPatient.setOnClickListener(v -> {
            if (validateForm()) {
                savePatientData();
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (TextUtils.isEmpty(etFirstName.getText())) {
            etFirstName.setError("First name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(etLastName.getText())) {
            etLastName.setError("Last name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(etDateOfBirth.getText())) {
            etDateOfBirth.setError("Date of birth is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(spinnerGender.getText())) {
            spinnerGender.setError("Gender is required");
            isValid = false;
        }

        String email = etEmail.getText() != null ? etEmail.getText().toString() : "";
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            isValid = false;
        }

        if (TextUtils.isEmpty(etPhone.getText())) {
            etPhone.setError("Phone number is required");
            isValid = false;
        }

        return isValid;
    }

    private void savePatientData() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String fullName = firstName + " " + lastName;
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String gender = spinnerGender.getText().toString().trim();
        String dob = etDateOfBirth.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String medicalHistory = etMedicalHistory.getText().toString().trim();
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, "123456")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = task.getResult().getUser().getUid();

                        HashMap<Object, Object> userMap = new HashMap<>();
                        userMap.put("name", fullName);
                        userMap.put("email", email);
                        userMap.put("mobile", phone);
                        userMap.put("role", "patient");
                        userMap.put("gender", gender);
                        userMap.put("age", String.valueOf(calculateAge(dob)));
                        userMap.put("address", address);
                        userMap.put("medicalHistory", medicalHistory);
                        FirebaseFirestore.getInstance().collection("users")
                                .document(uid)
                                .set(userMap)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Patient added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to add patient to Firestore", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Auth failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
    private int calculateAge(String dob) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date dateOfBirth = sdf.parse(dob);
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(dateOfBirth);

            Calendar today = Calendar.getInstance();

            int age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
