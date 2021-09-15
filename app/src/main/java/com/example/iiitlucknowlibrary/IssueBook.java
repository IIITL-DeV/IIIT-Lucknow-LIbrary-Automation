package com.example.iiitlucknowlibrary;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class IssueBook extends AppCompatActivity {
    EditText issueBookName, issueBookCategory, issueBookAuthor, issueStudentEmail,issueBookDate,returnBookDate;
    TextView issue;

    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book);
        issueBookName=findViewById(R.id.issue_book_name);
        issueBookCategory=findViewById(R.id.issue_book_category);
        issueBookAuthor=findViewById(R.id.issue_book_author);
        issueStudentEmail=findViewById(R.id.issue_book_student_enrollment);
        issueBookDate=findViewById(R.id.issue_date);
        returnBookDate=findViewById(R.id.return_date);

        issue=findViewById(R.id.issue_book_button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        ProgressDialog progressDialog = new ProgressDialog(IssueBook.this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);

        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String Name = issueBookName.getText().toString();
                String Category = issueBookCategory.getText().toString();
                String Author = issueBookAuthor.getText().toString();
                String Enrollment = issueStudentEmail.getText().toString();
                String IssueDate = issueBookDate.getText().toString();
                String ReturnDate = returnBookDate.getText().toString();

                if(TextUtils.isEmpty(Name) || TextUtils.isEmpty(Category) || TextUtils.isEmpty(Author)
                        || TextUtils.isEmpty(Enrollment) || TextUtils.isEmpty(IssueDate) || TextUtils.isEmpty(ReturnDate)){
                    progressDialog.dismiss();
                    Toast.makeText(IssueBook.this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference databaseReference = firebaseDatabase.getReference().child("IssueBook").child(Enrollment).child(Category).child(Name);
                    IssueBookModel issueBookModel=new IssueBookModel(Author,Name,IssueDate,ReturnDate);
                    //IssueBookModel(String author, String name, String issueDate, String returnDate);
                    databaseReference.setValue(issueBookModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                startActivity(new Intent(IssueBook.this, IssueBook.class));
                                Toast.makeText(IssueBook.this, "Book Issued successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(IssueBook.this, "Error in Book Issue", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}