package com.example.iiitlucknowlibrary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Registration extends AppCompatActivity {
    TextView txt_signin, txt_registration;
    EditText reg_name, reg_email, reg_enrolment, reg_pass, reg_re_pass;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txt_signin = findViewById(R.id.txt_signIn);
        txt_registration = findViewById(R.id.txt_register);

        reg_name = findViewById(R.id.reg_name);
        reg_email = findViewById(R.id.reg_email);
        reg_enrolment = findViewById(R.id.reg_enrolment);
        reg_pass = findViewById(R.id.reg_pass);
        reg_re_pass = findViewById(R.id.reg_re_pass);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        progressDialog = new ProgressDialog(Registration.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        txt_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this, Login.class));
            }
        });

        txt_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name = reg_name.getText().toString();
                String email = reg_email.getText().toString();
                String enrolment = reg_enrolment.getText().toString();
                String pass = reg_pass.getText().toString();
                String confirm_pass = reg_re_pass.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirm_pass)){
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                }else if(!email.endsWith("@gmail.com")){
                    progressDialog.dismiss();
                    reg_email.setError("Invalid Email");
                    Toast.makeText(Registration.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }else if(!pass.equals(confirm_pass)){
                    progressDialog.dismiss();
                    reg_re_pass.setError("Password is not matching");
                    Toast.makeText(Registration.this, "Password is matching", Toast.LENGTH_SHORT).show();
                }else if(pass.length()<6){
                    progressDialog.dismiss();
                    reg_pass.setError("Invalid Password");
                    Toast.makeText(Registration.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(mAuth.getUid());
                                Users users = new Users(mAuth.getUid(), name, email, enrolment);
                                databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull  Task<Void> task) {
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(Registration.this, Login.class));
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Your account is created successfully", Toast.LENGTH_SHORT).show();
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(Registration.this, "Error in creating user at final", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "Error in creating user", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }

}