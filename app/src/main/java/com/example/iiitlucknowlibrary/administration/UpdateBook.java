package com.example.iiitlucknowlibrary.administration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iiitlucknowlibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UpdateBook extends AppCompatActivity {
    EditText updateBookID,updateBookName,updateBookCategory,updateBookAuthor,updateQuantity;
    TextView update;

    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);
        updateBookID=findViewById(R.id.update_book_id);
        updateBookName=findViewById(R.id.update_book_name);
        updateBookCategory=findViewById(R.id.update_book_category);
        updateBookAuthor=findViewById(R.id.update_book_author);
        updateQuantity=findViewById(R.id.update_book_quantity);
        update=findViewById(R.id.txt_update);

        firebaseDatabase = FirebaseDatabase.getInstance();

        ProgressDialog progressDialog = new ProgressDialog(UpdateBook.this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String BookID = updateBookID.getText().toString();
                String Name = updateBookName.getText().toString();
                String Category = updateBookCategory.getText().toString();
                String Author = updateBookAuthor.getText().toString();
                String Quantity = updateQuantity.getText().toString();
                if(TextUtils.isEmpty(BookID) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Category) || TextUtils.isEmpty(Author) || TextUtils.isEmpty(Quantity)){
                    progressDialog.dismiss();
                    Toast.makeText(UpdateBook.this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                }else if((Integer.parseInt(Quantity)<1)){
                    progressDialog.dismiss();
                    updateQuantity.setError("Invalid Quantity");
                    Toast.makeText(UpdateBook.this, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("Books").child(BookID);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                HashMap hashMap=new HashMap();
                                hashMap.put("author",Author);
                                hashMap.put("category",Category);
                                hashMap.put("name",Name);
                                hashMap.put("quantity",Quantity);
                                databaseReference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        progressDialog.dismiss();
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(UpdateBook.this, UpdateBook.class));
                                            Toast.makeText(UpdateBook.this, "Book updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            progressDialog.dismiss();
                                            Toast.makeText(UpdateBook.this, "Error in updating Book details", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(UpdateBook.this, "Book Id doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateBook.this, "Getting error", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
    }
}