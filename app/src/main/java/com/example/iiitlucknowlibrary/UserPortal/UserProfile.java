package com.example.iiitlucknowlibrary.UserPortal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.iiitlucknowlibrary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {
   private TextView email,name,roll;
   FirebaseAuth auth;
   FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        String uId = auth.getCurrentUser().getUid();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("user").child(uId).child("enrolment");
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("user").child(uId).child("email");
        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference("user").child(uId).child("name");
        roll = (TextView)findViewById(R.id.user_rollTV);
        email = (TextView)findViewById(R.id.user_emailTV);
        name = (TextView)findViewById(R.id.user_nameTV);
        Log.d("dkfaklfd", "onCreate: "+reference1.getKey());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String roll_no = snapshot.getValue().toString();
                roll.setText("Roll No: "+ roll_no);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user_email = snapshot.getValue().toString();
                email.setText("Email: "+ user_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user_name = snapshot.getValue().toString();
                name.setText("Name: "+ user_name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}