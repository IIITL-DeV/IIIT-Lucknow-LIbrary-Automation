package com.example.iiitlucknowlibrary.Authentication;

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

import com.example.iiitlucknowlibrary.UserHome;
import com.example.iiitlucknowlibrary.R;
import com.example.iiitlucknowlibrary.HomeAdministration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView txt_signIn, txt_signup;
    EditText email, pass;
    private FirebaseAuth mAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txt_signIn = findViewById(R.id.txt_signin);
        txt_signup = findViewById(R.id.txt_signup);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.login_email);
        pass = findViewById(R.id.login_pass);

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        txt_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String Email = email.getText().toString();
                String Pass = pass.getText().toString();

                if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Pass)){
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Enter valid data", Toast.LENGTH_SHORT).show();
                }else if(!Email.endsWith("@gmail.com")){
                    progressDialog.dismiss();
                    email.setError("Invalid Email");
                    Toast.makeText(Login.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }else if(Pass.length()<6){
                    progressDialog.dismiss();
                    pass.setError("Invalid Password");
                    Toast.makeText(Login.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                progressDialog.dismiss();
                                if(Email.equals("admin@gmail.com")){
                                    Toast.makeText(Login.this, "Welcome to administration page.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, HomeAdministration.class));
                                }
                                else{
                                    Toast.makeText(Login.this, "Welcome to student page", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this, UserHome.class));
                                }
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Error to login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });

    }
}