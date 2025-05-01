//package com.example.med;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.med.models.Patient;
//import java.util.ArrayList;
//import java.util.List;
//
//public class PatientListActivity extends AppCompatActivity {
//    private RecyclerView recyclerView;
//    private PatientAdapter adapter;
//    private List<Patient> patientList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_patient_list);
//
//        recyclerView = findViewById(R.id.recyclerViewPatients);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Initialize with dummy data
//        patientList = new ArrayList<>();
//        patientList.add(new Patient("1", "John Doe", 35, "Male", "No major health issues"));
//        patientList.add(new Patient("2", "Jane Smith", 28, "Female", "Allergic to penicillin"));
//        patientList.add(new Patient("3", "Mike Johnson", 45, "Male", "Hypertension"));
//
//        adapter = new PatientAdapter(patientList);
//        recyclerView.setAdapter(adapter);
//    }
//
//    private class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
//        private List<Patient> patients;
//
//        public PatientAdapter(List<Patient> patients) {
//            this.patients = patients;
//        }
//
//        @Override
//        public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(android.R.layout.simple_list_item_1, parent, false);
//            return new PatientViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(PatientViewHolder holder, int position) {
//            Patient patient = patients.get(position);
//            holder.textView.setText(patient.getName());
//            holder.itemView.setOnClickListener(v -> {
//                Intent intent = new Intent(PatientListActivity.this, PatientProfileActivity.class);
//                intent.putExtra("patientId", patient.getId());
//                startActivity(intent);
//            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return patients.size();
//        }
//
//        class PatientViewHolder extends RecyclerView.ViewHolder {
//            TextView textView;
//
//            PatientViewHolder(View itemView) {
//                super(itemView);
//                textView = (TextView) itemView;
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
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.med.models.Patient;
import java.util.ArrayList;
import java.util.List;

public class PatientListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private List<Patient> patientList;
    private FloatingActionButton fabAddPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        recyclerView = findViewById(R.id.recyclerViewPatients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize FAB
        fabAddPatient = findViewById(R.id.fabAddPatient);
        fabAddPatient.setOnClickListener(v -> {
            Intent intent = new Intent(PatientListActivity.this, AddPatientActivity.class);
            startActivity(intent);
        });

        // Initialize with dummy data
        patientList = new ArrayList<>();
        patientList.add(new Patient("1", "John Doe", 35, "Male", "No major health issues"));
        patientList.add(new Patient("2", "Jane Smith", 28, "Female", "Allergic to penicillin"));
        patientList.add(new Patient("3", "Mike Johnson", 45, "Male", "Hypertension"));

        adapter = new PatientAdapter(patientList);
        recyclerView.setAdapter(adapter);
    }

    private class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
        private List<Patient> patients;

        public PatientAdapter(List<Patient> patients) {
            this.patients = patients;
        }

        @Override
        public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new PatientViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PatientViewHolder holder, int position) {
            Patient patient = patients.get(position);
            holder.textView.setText(patient.getName());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(PatientListActivity.this, PatientProfileActivity.class);
                intent.putExtra("patientId", patient.getId());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return patients.size();
        }

        class PatientViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            PatientViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }
}