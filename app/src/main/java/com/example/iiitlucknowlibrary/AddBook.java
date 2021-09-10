package com.example.iiitlucknowlibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddBook extends AppCompatActivity {
    EditText addBookName, addBookCategory, addBookAuthor, addBookQuantity;
    TextView add;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    CircleImageView book_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        addBookName = findViewById(R.id.add_book_name);
        addBookAuthor = findViewById(R.id.add_book_author);
        addBookCategory = findViewById(R.id.add_book_category);
        addBookQuantity = findViewById(R.id.add_book_quantity);
        book_image = findViewById(R.id.book_image);
        add = findViewById(R.id.txt_add);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        ProgressDialog progressDialog = new ProgressDialog(AddBook.this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String Name = addBookName.getText().toString();
                String Category = addBookCategory.getText().toString();
                String Author = addBookAuthor.getText().toString();
                String Quantity = addBookQuantity.getText().toString();

                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Books").child(Category).child(Name);
                Book book = new Book(Name, Author, Quantity, "Available");
                databaseReference.setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            startActivity(new Intent(AddBook.this, AddBook.class));
                            Toast.makeText(AddBook.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AddBook.this, "Eroor in adding book", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //StorageReference storageReference
            }
        });
    }
}