package com.example.iiitlucknowlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iiitlucknowlibrary.Authentication.Login;
import com.example.iiitlucknowlibrary.Authentication.Registration;
import com.example.iiitlucknowlibrary.administration.AddBook;
import com.example.iiitlucknowlibrary.administration.IssueBook;
import com.example.iiitlucknowlibrary.administration.RemoveABook;
import com.example.iiitlucknowlibrary.administration.UpdateBook;
import com.google.firebase.auth.FirebaseAuth;

public class HomeAdministration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView logout;
    TextView add, issue, show, remove,update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_administration);

        logout = findViewById(R.id.Logout);
        mAuth = FirebaseAuth.getInstance();
        add = findViewById(R.id.add_book);
        issue = findViewById(R.id.issue_book);
        show = findViewById(R.id.show_books);
        remove = findViewById(R.id.remove_book);
        update = findViewById(R.id.update_book);

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
                startActivity(new Intent(HomeAdministration.this, IssueBook.class));
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeAdministration.this, Home.class));
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeAdministration.this, RemoveABook.class));
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeAdministration.this, UpdateBook.class));
            }
        });
    }
}