package com.example.iiitlucknowlibrary.UserPortal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;
import com.example.iiitlucknowlibrary.Book;
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
import java.util.HashMap;
public class MyBooks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

    }
}