package com.example.iiitlucknowlibrary.UserPortal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iiitlucknowlibrary.Book;
import com.example.iiitlucknowlibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookList extends AppCompatActivity {
     TextView txt;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
         Intent intent = getIntent();
         txt = (TextView)findViewById(R.id.bookList);
         String string = intent.getStringExtra("categoryName");
         txt.setText( string + " Books");
        RecyclerView recyclerView1 = findViewById(R.id.bookslistRecyclerView);
        database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new GridLayoutManager(this,2));

        ArrayList<Book> book_list = new ArrayList<Book>();
        BookListAdapter adapter1 = new BookListAdapter(this,book_list);
        recyclerView1.setAdapter(adapter1);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for(DataSnapshot snapshot1 :  snapshot.getChildren()){
                        Book book  = snapshot1.getValue(Book.class);
                        String s = book.getCategory();
                        if (s.equalsIgnoreCase(string)) {
                            book_list.add(book);
                        }
                    }
                    adapter1.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookList.this, "error "+error+" occurred", Toast.LENGTH_SHORT).show();
            }
        });


    }
}