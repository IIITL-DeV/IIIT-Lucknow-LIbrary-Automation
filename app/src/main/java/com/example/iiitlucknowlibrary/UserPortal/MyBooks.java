package com.example.iiitlucknowlibrary.UserPortal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.iiitlucknowlibrary.R;
import com.example.iiitlucknowlibrary.UserPortal.MyBookAdapter;
import com.example.iiitlucknowlibrary.administration.IssueBookModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MyBooks extends AppCompatActivity {
    DatabaseReference database1,database2;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    FirebaseStorage firebaseStorage;
     String s,uId;
    TextView no_book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
         uId = mAuth.getCurrentUser().getUid();
        database1 = FirebaseDatabase.getInstance().getReference("user").child(uId).child("enrolment");

       recyclerView = findViewById(R.id.myBooksListRecyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<IssueBookModel> book_list = new ArrayList<IssueBookModel>();
        MyBookAdapter myAdapter = new MyBookAdapter(this,book_list);
        recyclerView.setAdapter(myAdapter);
        no_book = (TextView)findViewById(R.id.no_books);
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    s = snapshot.getValue().toString();
                    if(s.isEmpty()){
                         no_book.setText("NO BOOK TO SHOW :(");
                    }else {
                        database2 = FirebaseDatabase.getInstance().getReference("IssueBook");
                        database2.addValueEventListener(new ValueEventListener() {
                            @Override
                             public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    IssueBookModel issued_book = dataSnapshot.getValue(IssueBookModel.class);
                                    book_list.add(issued_book);
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