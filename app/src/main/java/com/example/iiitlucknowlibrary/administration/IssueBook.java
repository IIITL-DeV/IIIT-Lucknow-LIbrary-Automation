package com.example.iiitlucknowlibrary.administration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;

public class IssueBook extends AppCompatActivity {
    EditText issueBookID, issueStudentEnrollment,issueBookDate,returnBookDate;
    TextView issue;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    boolean f = true, f1;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_book);
        issueBookID=findViewById(R.id.issue_book_id);
        issueStudentEnrollment=findViewById(R.id.issue_book_student_enrollment);
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
                String issueID=issueBookID.getText().toString();
                String Enrollment = issueStudentEnrollment.getText().toString();
                String IssueDate = issueBookDate.getText().toString();
                String ReturnDate = returnBookDate.getText().toString();

                if(TextUtils.isEmpty(issueID) || TextUtils.isEmpty(Enrollment) ||
                        TextUtils.isEmpty(IssueDate) || TextUtils.isEmpty(ReturnDate)){
                    progressDialog.dismiss();
                    Toast.makeText(IssueBook.this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Books").child(issueID);
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            if(snapshot1.exists()){
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("IssueBook").child(Enrollment).child(issueID);
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot000) {
                                        if(snapshot000.exists()){
                                            progressDialog.dismiss();
                                            Toast.makeText(IssueBook.this, "This book is already issued to this student", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            reference1.child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                    int count = Integer.parseInt(snapshot2.getValue().toString());
                                                    if(count==0){
                                                        progressDialog.dismiss();
                                                        Toast.makeText(IssueBook.this, "Book is not available right now.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        DatabaseReference reference2 = firebaseDatabase.getReference().child("IssueBook").child(Enrollment).child(issueID);
                                                        IssueBookModel issueBookModel=new IssueBookModel(issueID,IssueDate,ReturnDate,Enrollment);
                                                        reference2.setValue(issueBookModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                progressDialog.dismiss();
                                                                if(task.isSuccessful()){
                                                                    //int a = count-1;

                                                                    reference1.child("quantity").setValue(""+(count-1));
                                                                    if(count==1){
                                                                        reference1.child("status").setValue("Not Available");
                                                                    }
                                                                    startActivity(new Intent(IssueBook.this, IssueBook.class));
                                                                    Toast.makeText(IssueBook.this, "Book Issued successfully", Toast.LENGTH_SHORT).show();
                                                                    //f = false;

                                                                }
                                                                else{
                                                                    Toast.makeText(IssueBook.this, "Error in Book Issue", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(IssueBook.this, "Error2: "+error, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(IssueBook.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(IssueBook.this, "Book doesn't exist.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(IssueBook.this, "Error1: "+error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}