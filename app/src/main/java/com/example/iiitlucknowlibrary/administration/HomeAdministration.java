package com.example.iiitlucknowlibrary.administration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iiitlucknowlibrary.Authentication.Login;
import com.example.iiitlucknowlibrary.Authentication.Registration;
import com.example.iiitlucknowlibrary.Book;
import com.example.iiitlucknowlibrary.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdministration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView logout;
    CardView add, issue, show, remove, update,returnBtn, details, history;
    Dialog myDialog1, myDialog2, myDialog3, myDialog4, myDialog5;
    ProgressDialog progressDialog;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    Uri imageUri;
    public static String image_uri;
    CircleImageView book_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_administration);

        logout = findViewById(R.id.Logout);
        mAuth = FirebaseAuth.getInstance();
        add = findViewById(R.id.add_book);
        issue = findViewById(R.id.issue_book);
        show = findViewById(R.id.show_books);
        remove = findViewById(R.id.remove_book);
        update = findViewById(R.id.update_book);
        returnBtn=findViewById(R.id.return_book);
        details = findViewById(R.id.detils);
        history = findViewById(R.id.history);


        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(HomeAdministration.this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);

        myDialog1 = new Dialog(this);
        myDialog2 = new Dialog(this);
        myDialog3 = new Dialog(this);
        myDialog4 = new Dialog(this);
        myDialog5 = new Dialog(this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                FirebaseAuth.getInstance().signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("297181861064-7ahb7gh8b3tacknplv05ak57avgte8oa.apps.googleusercontent.com")
                        .requestEmail()
                        .build();
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(HomeAdministration.this, gso);
                mGoogleSignInClient.signOut();
                startActivity(new Intent(HomeAdministration.this, Login.class));
            }
        });

        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(HomeAdministration.this, Registration.class));
        }

        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopupForIssueBook();
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("admin", "onClick: "+"check : 1");
                ShowPopupForReturnBook();
                //startActivity(new Intent(HomeAdministration.this, ReturnBook.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopupForAddBook();
                //startActivity(new Intent(HomeAdministration.this, AddBook.class));
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopupForRemoveBook();
                //startActivity(new Intent(HomeAdministration.this, RemoveABook.class));
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopupForUpdateBook();
                //startActivity(new Intent(HomeAdministration.this, UpdateBook.class));
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeAdministration.this, Activity_cat_show_admin.class));
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeAdministration.this, IssuedBooksDetails.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeAdministration.this, History.class));
            }
        });



    }


    // Issue Book Pop-up Window
    public void ShowPopupForIssueBook() {
        //EditText reg_name, reg_email, reg_enrolment, reg_pass, reg_re_pass;

        myDialog1.setContentView(R.layout.popup_issue_book);

        WindowManager.LayoutParams lp = myDialog1.getWindow().getAttributes();
        lp.dimAmount=25f;
        myDialog1.getWindow().setAttributes(lp);
        myDialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        EditText issueBookID, issueStudentEnrollment,issueBookDate,returnBookDate;
        TextView Issue;

        issueBookID = myDialog1.findViewById(R.id.issue_book_id);
        issueStudentEnrollment = myDialog1.findViewById(R.id.issue_book_student_enrollment);
        issueBookDate = myDialog1.findViewById(R.id.issue_date);
        returnBookDate = myDialog1.findViewById(R.id.return_date);
        Issue = myDialog1.findViewById(R.id.issue_book_button);
        Issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String issueID = issueBookID.getText().toString();
                String Enrollment = issueStudentEnrollment.getText().toString();
                String IssueDate = issueBookDate.getText().toString();
                String ReturnDate = returnBookDate.getText().toString();

                if(TextUtils.isEmpty(issueID) || TextUtils.isEmpty(Enrollment) ||
                        TextUtils.isEmpty(IssueDate) || TextUtils.isEmpty(ReturnDate)){
                    progressDialog.dismiss();
                    Toast.makeText(HomeAdministration.this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(HomeAdministration.this, "This book is already issued to this student", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            reference1.child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                                    int count = Integer.parseInt(snapshot2.getValue().toString());
                                                    if(count==0){
                                                        progressDialog.dismiss();
                                                        Toast.makeText(HomeAdministration.this, "Book is not available right now.", Toast.LENGTH_SHORT).show();
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
                                                                    startActivity(new Intent(HomeAdministration.this, HomeAdministration.class));
                                                                    Toast.makeText(HomeAdministration.this, "Book Issued successfully", Toast.LENGTH_SHORT).show();
                                                                    //f = false;

                                                                }
                                                                else{
                                                                    Toast.makeText(HomeAdministration.this, "Error in Book Issue", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(HomeAdministration.this, "Error2: "+error, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(HomeAdministration.this, "Error occurred", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(HomeAdministration.this, "Book doesn't exist.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(HomeAdministration.this, "Error1: "+error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        myDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog1.show();
    }

    //Return Book Popup Window

    public void ShowPopupForReturnBook() {

        myDialog2.setContentView(R.layout.popup_return_book);

        WindowManager.LayoutParams lp = myDialog2.getWindow().getAttributes();
        lp.dimAmount=25f;
        myDialog2.getWindow().setAttributes(lp);
        myDialog2.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        EditText BookId, EnrollmentNumber;
        TextView Return;

        BookId = myDialog2.findViewById(R.id.reg_bookId);
        EnrollmentNumber = myDialog2.findViewById(R.id.reg_enrolment);
        Return = myDialog2.findViewById(R.id.reg_return);

        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String bookId = BookId.getText().toString();
                String enrollment = EnrollmentNumber.getText().toString();

                if(TextUtils.isEmpty(bookId) || TextUtils.isEmpty(enrollment)){
                    progressDialog.dismiss();
                    Toast.makeText(HomeAdministration.this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query applesQuery = ref.child("IssueBook").child(enrollment).child(bookId);

                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()){
                                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("History").child(enrollment).child(bookId);

                                reference1.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if(task.isSuccessful()){
                                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Books").child(bookId);
                                            reference2.child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int count = Integer.parseInt(snapshot.getValue().toString());
                                                    reference2.child("quantity").setValue(""+(count+1));
                                                    if(count==0){
                                                        reference2.child("status").setValue("Available");
                                                    }
                                                    appleSnapshot.getRef().removeValue();
                                                    progressDialog.dismiss();
                                                    myDialog2.cancel();
                                                    Toast.makeText(HomeAdministration.this, "Book Returned successfully", Toast.LENGTH_SHORT).show();
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(HomeAdministration.this, "Getting error01", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                        else{
                                            Toast.makeText(HomeAdministration.this, "Error in Return Book", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                            Log.e("RemoveABook", "onCancelled", databaseError.toException());
                            Toast.makeText(HomeAdministration.this, "Error in Book Return", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.show();
    }



    //Add Book Popup Window

    public void ShowPopupForAddBook() {

        myDialog3.setContentView(R.layout.popup_add_book);

        WindowManager.LayoutParams lp = myDialog3.getWindow().getAttributes();
        lp.dimAmount=25f;
        myDialog3.getWindow().setAttributes(lp);
        myDialog3.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);


        EditText addBookID, addBookName, addBookCategory, addBookAuthor, addBookQuantity, bookUrl;
        TextView add;

        book_image = myDialog3.findViewById(R.id.book_image);
        addBookID = myDialog3.findViewById(R.id.add_book_id);
        addBookName = myDialog3.findViewById(R.id.add_book_name);
        addBookCategory = myDialog3.findViewById(R.id.add_book_category);
        addBookAuthor = myDialog3.findViewById(R.id.add_book_author);
        addBookQuantity = myDialog3.findViewById(R.id.add_book_quantity);
        bookUrl = myDialog3.findViewById(R.id.book_drive_link);
        add = myDialog3.findViewById(R.id.txt_add);

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
                String BookUrl;

                if(bookUrl.getText().toString().equals("")) BookUrl = "Null";
                else BookUrl = bookUrl.getText().toString();
                if(TextUtils.isEmpty(BookID) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Category) || TextUtils.isEmpty(Author) || TextUtils.isEmpty(Quantity)){
                    progressDialog.dismiss();
                    Toast.makeText(HomeAdministration.this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                }else if((Integer.parseInt(Quantity)<1)){
                    progressDialog.dismiss();
                    addBookQuantity.setError("Invalid Quantity");
                    Toast.makeText(HomeAdministration.this, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference reference0 = FirebaseDatabase.getInstance().getReference().child("Books").child(BookID);
                    reference0.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                reference0.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.getValue().equals(Name)){
                                            reference0.child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int count = Integer.parseInt(snapshot.getValue().toString());
                                                    if(count==0){
                                                        reference0.child("status").setValue("Available");
                                                    }
                                                    count += Integer.parseInt(Quantity);
                                                    reference0.child("quantity").setValue(""+count);
                                                    progressDialog.dismiss();
                                                    myDialog3.cancel();
                                                    //startActivity(new Intent(HomeAdministration.this, HomeAdministration.class));
                                                    Toast.makeText(HomeAdministration.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                        else{
                                            progressDialog.dismiss();
                                            Toast.makeText(HomeAdministration.this, "A book is already available with this book ID", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(HomeAdministration.this, "Getting error02", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                DatabaseReference databaseReference = firebaseDatabase.getReference().child("Books").child(BookID);
                                StorageReference storageReference = firebaseStorage.getReference().child("BookImages").child(Category).child(Name);
                                if(imageUri!=null){
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        image_uri = uri.toString();
                                                        Book book = new Book(BookID, Name, Author, Category, Quantity, "Available", image_uri, BookUrl);
                                                        databaseReference.setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                progressDialog.dismiss();
                                                                if(task.isSuccessful()){
                                                                    startActivity(new Intent(HomeAdministration.this, HomeAdministration.class));
                                                                    Toast.makeText(HomeAdministration.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    Toast.makeText(HomeAdministration.this, "Error in adding Book details", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                            else{
                                                progressDialog.dismiss();
                                                Toast.makeText(HomeAdministration.this, "Errror in uploading images", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                else{
                                    image_uri = "https://firebasestorage.googleapis.com/v0/b/library-automation-9abd2.appspot.com/o/book.jpg?alt=media&token=c153d25f-22a6-48b7-b269-0948dd07ec74";
                                    Book book = new Book(BookID, Name, Author, Category, Quantity, "Available", image_uri, BookUrl);
                                    databaseReference.setValue(book).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            if(task.isSuccessful()){
                                                startActivity(new Intent(HomeAdministration.this, HomeAdministration.class));
                                                Toast.makeText(HomeAdministration.this, "Book added successfull", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(HomeAdministration.this, "Error in adding book details", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(HomeAdministration.this, "Getting error01", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        myDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog3.show();

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


    // Update Book Popup Window
    public void ShowPopupForRemoveBook() {

        myDialog4.setContentView(R.layout.popup_remove_book);

        WindowManager.LayoutParams lp = myDialog4.getWindow().getAttributes();
        lp.dimAmount=25f;
        myDialog4.getWindow().setAttributes(lp);
        myDialog4.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);


        EditText BookId;
        TextView btn;

        BookId = myDialog4.findViewById(R.id.remove_book_id);
        btn = myDialog4.findViewById(R.id.txt_remove);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String BookID = BookId.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Books").child(BookID);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Query applesQuery = ref.child("Books").child(BookID);

                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                        progressDialog.dismiss();
                                        myDialog4.cancel();
                                        Toast.makeText(HomeAdministration.this, "Book is successfully Removed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                    Log.e("RemoveABook", "onCancelled", databaseError.toException());
                                    Toast.makeText(HomeAdministration.this, "Error in removing book", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(HomeAdministration.this, "Book Id doesn't exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeAdministration.this, "Getting Error001", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        myDialog4.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog4.show();
    }


    // Update Book Popup Window
    public void ShowPopupForUpdateBook() {

        myDialog5.setContentView(R.layout.popup_update_book);

        WindowManager.LayoutParams lp = myDialog5.getWindow().getAttributes();
        lp.dimAmount=25f;
        myDialog5.getWindow().setAttributes(lp);
        myDialog5.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);


        EditText updateBookID,updateBookName,updateBookCategory,updateBookAuthor,updateQuantity;
        TextView update;


        updateBookID = myDialog5.findViewById(R.id.update_book_id);
        updateBookName = myDialog5.findViewById(R.id.update_book_name);
        updateBookCategory = myDialog5.findViewById(R.id.update_book_category);
        updateBookAuthor = myDialog5.findViewById(R.id.update_book_author);
        updateQuantity = myDialog5.findViewById(R.id.update_book_quantity);
        update = myDialog5.findViewById(R.id.txt_update);

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
                    Toast.makeText(HomeAdministration.this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
                }else if((Integer.parseInt(Quantity)<1)){
                    progressDialog.dismiss();
                    updateQuantity.setError("Invalid Quantity");
                    Toast.makeText(HomeAdministration.this, "Invalid Quantity", Toast.LENGTH_SHORT).show();
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
                                            myDialog5.cancel();
                                            //startActivity(new Intent(UpdateBook.this, UpdateBook.class));
                                            Toast.makeText(HomeAdministration.this, "Book updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            progressDialog.dismiss();
                                            Toast.makeText(HomeAdministration.this, "Error in updating Book details", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(HomeAdministration.this, "Book Id doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(HomeAdministration.this, "Getting error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        myDialog5.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog5.show();
    }

}