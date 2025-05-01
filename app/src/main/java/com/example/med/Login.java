package com.example.med;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private boolean isPasswordVisible = false;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        ImageView togglePassword = findViewById(R.id.togglePassword);
        Button loginButton2 = findViewById(R.id.loginButton2);


        // Toggle password visibility
        togglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                togglePassword.setImageResource(R.drawable.ic_eye); // Change icon
            } else {
                passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                togglePassword.setImageResource(R.drawable.ic_eye_off);
            }
            isPasswordVisible = !isPasswordVisible;
        });

        // Handle Login Click
        loginButton2.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().toLowerCase().trim();
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        assert user != null;
                        String uid = user.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot ->
                        {
                           if(documentSnapshot.exists()){
                               String role = documentSnapshot.getString("role");
                               if(role.equals("doctor")){
                                   Intent intent = new Intent(Login.this, DoctorDashboard.class);
                                   startActivity(intent);
                                   finish();
                               }else if(role.equals("patient")) {
                                   Intent intent = new Intent(Login.this, PatientDashboardActivity.class);
                                   startActivity(intent);
                                   finish();
                               }else{
                                   Toast.makeText(Login.this,"Role not authorized",Toast.LENGTH_SHORT).show();
                               }
                           }else {
                               Toast.makeText(Login.this,"User Not Found",Toast.LENGTH_SHORT).show();
                           }
                        });

                    }else{
                        Toast.makeText(Login.this,"Incorrect Email or password",Toast.LENGTH_SHORT).show();
                    }
                }
            });


        });

        // Navigate to SignUp Activity

    }
}
