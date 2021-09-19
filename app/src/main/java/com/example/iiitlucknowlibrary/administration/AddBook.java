package com.example.iiitlucknowlibrary.administration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iiitlucknowlibrary.Book;
import com.example.iiitlucknowlibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddBook extends AppCompatActivity {
    EditText addBookID, addBookName, addBookCategory, addBookAuthor, addBookQuantity;
    TextView add;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    CircleImageView book_image;
    Uri imageUri;
    public static String image_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        addBookID = findViewById(R.id.add_book_id);
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

        book_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String BookID = addBookID.getText().toString();
                String Name = addBookName.getText().toString();
                String Category = addBookCategory.getText().toString();
                String Author = addBookAuthor.getText().toString();
                String Quantity = addBookQuantity.getText().toString();
                if(TextUtils.isEmpty(BookID) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Category) || TextUtils.isEmpty(Author) || TextUtils.isEmpty(Quantity)){
                    progressDialog.dismiss();
                    Toast.makeText(AddBook.this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                }else if((Integer.parseInt(Quantity)<1)){
                    progressDialog.dismiss();
                    addBookQuantity.setError("Invalid Quantity");
                    Toast.makeText(AddBook.this, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("Books").child(BookID);
                    StorageReference storageReference = firebaseStorage.getReference().child("BookImages").child(Category).child(Name);
                    //Book book = new Book(Name, Author, Quantity, "Available");
                    if(imageUri!=null){
                        storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            image_uri = uri.toString();
                                            Book book = new Book(BookID, Name, Author, Category, Quantity, "Available", image_uri);
                                            databaseReference.setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    progressDialog.dismiss();
                                                    if(task.isSuccessful()){
                                                        startActivity(new Intent(AddBook.this, AddBook.class));
                                                        Toast.makeText(AddBook.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(AddBook.this, "Error in adding Book details", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(AddBook.this, "Errror in uploading images", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        image_uri = "https://firebasestorage.googleapis.com/v0/b/library-automation-9abd2.appspot.com/o/book.jpg?alt=media&token=c153d25f-22a6-48b7-b269-0948dd07ec74";
                        Book book = new Book(BookID, Name, Author, Category, Quantity, "Available", image_uri);
                        databaseReference.setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    startActivity(new Intent(AddBook.this, AddBook.class));
                                    Toast.makeText(AddBook.this, "Book added successfull", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(AddBook.this, "Error in adding book details", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                /*databaseReference.setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                //StorageReference storageReference*/
                }



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==10){
            if(data!=null){
                imageUri = data.getData();
                book_image.setImageURI(imageUri);
            }
        }
    }
}