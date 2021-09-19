package com.example.iiitlucknowlibrary.administration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iiitlucknowlibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RemoveABook extends AppCompatActivity {
    EditText BookId;
    TextView btn;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_abook);

        BookId = findViewById(R.id.remove_book_id);
        btn = findViewById(R.id.txt_remove);

        ProgressDialog progressDialog = new ProgressDialog(RemoveABook.this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);

        firebaseDatabase = FirebaseDatabase.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String BookID = BookId.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child("Books").child(BookID);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            progressDialog.dismiss();
                            Toast.makeText(RemoveABook.this, "Book is successfully Removed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressDialog.dismiss();
                        Log.e("RemoveABook", "onCancelled", databaseError.toException());
                        Toast.makeText(RemoveABook.this, "Error in removing book", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}