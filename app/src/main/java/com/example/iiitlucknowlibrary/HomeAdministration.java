package com.example.iiitlucknowlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeAdministration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_administration);

        logout = findViewById(R.id.Logout);
        mAuth = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeAdministration.this, Login.class));
            }
        });

        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(HomeAdministration.this, Registration.class));
        }
        else if(mAuth.getCurrentUser().getEmail().equals("manishkumar787898@gmail.com")){
            startActivity(new Intent(HomeAdministration.this, HomeAdministration.class));
        }
    }
}