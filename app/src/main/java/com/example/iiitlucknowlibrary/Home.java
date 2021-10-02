package com.example.iiitlucknowlibrary;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iiitlucknowlibrary.Authentication.Login;
import com.example.iiitlucknowlibrary.Authentication.Registration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
public class Home extends AppCompatActivity {
    ImageView logout;
    DatabaseReference database;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        logout = findViewById(R.id.Logout);
        mAuth = FirebaseAuth.getInstance();
        RecyclerView recyclerView = findViewById(R.id.booksRecyclerView);
        database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HashMap<String, ArrayList<Book>> book_map = new HashMap<String, ArrayList<Book>>();
        ArrayList<Book> list = new ArrayList<Book>();
        BookAdapter adapter = new BookAdapter(this,list,book_map);
        recyclerView.setAdapter(adapter);
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
        else if(mAuth.getCurrentUser().getEmail().equals("admin@gmail.com")){
            startActivity(new Intent(Home.this, HomeAdministration.class));
        }
    }
}
