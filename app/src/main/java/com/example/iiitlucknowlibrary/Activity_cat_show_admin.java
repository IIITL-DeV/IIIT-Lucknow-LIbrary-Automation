package com.example.iiitlucknowlibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.iiitlucknowlibrary.UserPortal.BookAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Activity_cat_show_admin extends AppCompatActivity {
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView = findViewById(R.id.booksRecyclerView);
        database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HashMap<String, ArrayList<Book>> book_map = new HashMap<String, ArrayList<Book>>();
        ArrayList<Book> list = new ArrayList<Book>();
        BookAdapter adapter = new BookAdapter(this,list, book_map);
        recyclerView.setAdapter(adapter);
        Iterator it = book_map.keySet().iterator();
        ArrayList<String> c_name = new ArrayList<String>();
        ArrayList<Integer> quantity = new ArrayList<Integer>();

//        while(it.hasNext()){
//            String s = (String)it.next();
//            ArrayList<Book> p = new ArrayList<Book>();
//            p = book_map.get(s);
//             c_name.add(s);
//             quantity.add(p.size());
//        }

        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book book;
                    book = dataSnapshot.getValue(Book.class);

                    String s = book.getCategory();
                    ArrayList<Book> temp = new ArrayList<Book>();
                    if(book_map.containsKey(s)){
                        temp = book_map.get(s);
                        temp.add(book);
                        book_map.put(s,temp);
                    }else {
                        temp.add(book);
                        list.add(book);
                        book_map.put(s, temp);
                        temp=null;
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
