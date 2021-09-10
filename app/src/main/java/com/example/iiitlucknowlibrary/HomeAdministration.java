package com.example.iiitlucknowlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeAdministration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView logout;
    TextView add, issue, show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_administration);

        logout = findViewById(R.id.Logout);
        mAuth = FirebaseAuth.getInstance();
        add = findViewById(R.id.add_book);
        issue = findViewById(R.id.issue_book);
        show = findViewById(R.id.show_books);

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


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeAdministration.this, AddBook.class));
            }
        });

        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}