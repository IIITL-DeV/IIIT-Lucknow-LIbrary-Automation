package com.example.iiitlucknowlibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.service.autofill.UserData;
import android.widget.TextView;

import com.example.iiitlucknowlibrary.UserPortal.BookAdapter;
import com.example.iiitlucknowlibrary.administration.IssueBookModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;

public class MyBooks extends AppCompatActivity {
    DatabaseReference database1,database2;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
     String s,uId;
    TextView no_book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        mAuth = FirebaseAuth.getInstance();
         uId = mAuth.getCurrentUser().getUid();
        database1 = FirebaseDatabase.getInstance().getReference("user").child(uId).child("enrolment");

       recyclerView = findViewById(R.id.myBooksListRecyclerView);
        database2 = FirebaseDatabase.getInstance().getReference("IssueBook");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<IssueBookModel> list = new ArrayList<IssueBookModel>();
        MyBookAdapter myAdapter = new MyBookAdapter(this,list);
        recyclerView.setAdapter(myAdapter);
        no_book = (TextView)findViewById(R.id.no_books);
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    s = "";
                   // s = snapshot.getValue().toString();
                    if(s.isEmpty()){
                         no_book.setText("NO BOOK TO SHOW :(");
                    }else {
                        database2.addValueEventListener(new ValueEventListener() {
                            @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    IssueBookModel issued_book = dataSnapshot.getValue(IssueBookModel.class);
                                    String Enrollment = issued_book.getEnrollment();
                                    if (Enrollment.equalsIgnoreCase(s)) {
                                        list.add(issued_book);
                                    }
                                }
                                myAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}