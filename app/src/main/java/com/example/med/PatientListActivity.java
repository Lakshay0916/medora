package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.med.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private List<Patient> patientList;
    private FloatingActionButton fabAddPatient;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        recyclerView = findViewById(R.id.recyclerViewPatients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAddPatient = findViewById(R.id.fabAddPatient);
        fabAddPatient.setOnClickListener(v -> {
            Intent intent = new Intent(PatientListActivity.this, AddPatientActivity.class);
            startActivity(intent);
        });

        // Initialize empty list and adapter before fetching
        patientList = new ArrayList<>();
        adapter = new PatientAdapter(patientList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;
        uid = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("role", "patient")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            patientList.clear();
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                patientList.add(new Patient(
                                        snapshot.getId(),
                                        snapshot.getString("name"),
                                        Integer.parseInt(snapshot.getString("age")),
                                        snapshot.getString("gender"),
                                        snapshot.getString("medicalHistory")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
        private List<Patient> patients;

        public PatientAdapter(List<Patient> patients) {
            this.patients = patients;
        }

        @NonNull
        @Override
        public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new PatientViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
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