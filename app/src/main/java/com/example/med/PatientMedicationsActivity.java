package com.example.med;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class PatientMedicationsActivity extends AppCompatActivity {
    private RecyclerView medicationsRecyclerView;
    private MedicationAdapter medicationAdapter;
    private List<Medication> medications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medications);

        medicationsRecyclerView = findViewById(R.id.medicationsRecyclerView);
        medicationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        medications = getDummyMedications();
        medicationAdapter = new MedicationAdapter(medications);
        medicationsRecyclerView.setAdapter(medicationAdapter);
    }

    private List<Medication> getDummyMedications() {
        List<Medication> dummyMedications = new ArrayList<>();
        dummyMedications.add(new Medication("Aspirin", "81mg", "Once daily", "Blood thinning"));
        dummyMedications.add(new Medication("Metformin", "500mg", "Twice daily", "Diabetes management"));
        dummyMedications.add(new Medication("Lisinopril", "10mg", "Once daily", "Blood pressure control"));
        return dummyMedications;
    }

    private static class MedicationAdapter extends RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder> {
        private List<Medication> medications;

        MedicationAdapter(List<Medication> medications) {
            this.medications = medications;
        }

        @NonNull
        @Override
        public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_medication, parent, false);
            return new MedicationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
            Medication medication = medications.get(position);
            holder.medicationName.setText(medication.getName());
            holder.dosage.setText("Dosage: " + medication.getDosage());
            holder.frequency.setText("Frequency: " + medication.getFrequency());
            holder.purpose.setText("Purpose: " + medication.getPurpose());
        }

        @Override
        public int getItemCount() {
            return medications.size();
        }

        static class MedicationViewHolder extends RecyclerView.ViewHolder {
            TextView medicationName, dosage, frequency, purpose;

            MedicationViewHolder(View itemView) {
                super(itemView);
                medicationName = itemView.findViewById(R.id.textViewMedicationName);
                dosage = itemView.findViewById(R.id.textViewDosage);
                frequency = itemView.findViewById(R.id.textViewFrequency);
                purpose = itemView.findViewById(R.id.textViewPurpose);
            }
        }
    }

    private static class Medication {
        private String name;
        private String dosage;
        private String frequency;
        private String purpose;

        Medication(String name, String dosage, String frequency, String purpose) {
            this.name = name;
            this.dosage = dosage;
            this.frequency = frequency;
            this.purpose = purpose;
        }

        String getName() { return name; }
        String getDosage() { return dosage; }
        String getFrequency() { return frequency; }
        String getPurpose() { return purpose; }
    }
}