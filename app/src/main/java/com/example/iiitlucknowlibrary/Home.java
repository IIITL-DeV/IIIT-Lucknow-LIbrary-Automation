package com.example.iiitlucknowlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class Home extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        logout = findViewById(R.id.Logout);
        mAuth = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home.this, Login.class));
            }
        });

        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(Home.this, Registration.class));
        }
        else if(mAuth.getCurrentUser().getEmail().equals("krishankg2001@gmail.com")){
            startActivity(new Intent(Home.this, HomeAdministration.class));
        }
    }
}