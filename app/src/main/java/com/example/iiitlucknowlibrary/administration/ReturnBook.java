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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReturnBook extends AppCompatActivity {
    EditText returnBookID, returnStudentEnrollment;
    TextView returnBook;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_book);
        returnBook=findViewById(R.id.return_book_button);
        returnBookID=findViewById(R.id.return_book_id);
        returnStudentEnrollment=findViewById(R.id.return_book_student_enrollment);

        ProgressDialog progressDialog = new ProgressDialog(ReturnBook.this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);

        firebaseDatabase = FirebaseDatabase.getInstance();

        returnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String Enrollment = returnStudentEnrollment.getText().toString();
                String myReturnBookID=returnBookID.getText().toString();

                if(TextUtils.isEmpty(myReturnBookID) || TextUtils.isEmpty(Enrollment)){
                    progressDialog.dismiss();
                    Toast.makeText(ReturnBook.this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference databaseReference=firebaseDatabase.getReference().child("IssueBook").child(Enrollment);

                    databaseReference.child(myReturnBookID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                startActivity(new Intent(ReturnBook.this, ReturnBook.class));
                                Toast.makeText(ReturnBook.this, "Book Returned successfully", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(ReturnBook.this, "Error in Book Return", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}