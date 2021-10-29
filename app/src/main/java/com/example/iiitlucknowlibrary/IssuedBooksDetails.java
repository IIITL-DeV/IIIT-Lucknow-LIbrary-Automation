package com.example.iiitlucknowlibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.iiitlucknowlibrary.UserPortal.MyBookAdapter;
import com.example.iiitlucknowlibrary.administration.IssueBookModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IssuedBooksDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issued_books_details);



        RecyclerView recyclerView = findViewById(R.id.myBooksListRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<IssueBookModel> book_list = new ArrayList<IssueBookModel>();
        MyBookAdapter myAdapter = new MyBookAdapter(this,book_list);
        recyclerView.setAdapter(myAdapter);
        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("IssueBook");
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()){
                    Log.d("aaa2", "onCreateView: "+dataSnapshot1.getKey());
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                        IssueBookModel issued_book = dataSnapshot2.getValue(IssueBookModel.class);
                        String s = issued_book.getIssueId();
                        s += ", ";
                        s += "Student Roll Number: ";
                        s += dataSnapshot1.getKey();
                        issued_book.setIssueId(s);
                        book_list.add(issued_book);
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