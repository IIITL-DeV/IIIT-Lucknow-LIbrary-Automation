package com.example.iiitlucknowlibrary.UserPortal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiitlucknowlibrary.Authentication.Users;
import com.example.iiitlucknowlibrary.Book;
import com.example.iiitlucknowlibrary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.MyViewHolder> {

    Context context;
    public ArrayList<Book> bookList;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth mAuth;
    String UserEmail, UserUid;
    public BookListAdapter(Context context, ArrayList<Book> book_list) {
        this.context = context;
        this.bookList = book_list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_booklist_row,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = bookList.get(position);

        holder.bookName.setText("Name: "+ book.getName());
        holder.authorName.setText("Author: "+  book.getAuthor());
        holder.bookId.setText("BookId: " + book.getBookID());
        holder.quantity.setText("Status: "+  book.getQuantity() + " Books Available");

        Picasso.get().load(book.getImageUri()).into(holder.book_category_image);
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);
        UserEmail = FirebaseAuth.getInstance().getUid();

        DatabaseReference reference33 = FirebaseDatabase.getInstance().getReference().child("user");
//        reference33.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            void onDataChange(DataSnapshot snapshot) {
//                if (snapshot.hasChild("name")) {
//                    // run some code
//                }
//            }
//        });


        reference33.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getUid())){
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            new AlertDialog.Builder(context)
                                    .setTitle("Add to WishList")
                                    .setMessage("Are you sure to add?")
                                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressDialog.show();
                                            firebaseDatabase = FirebaseDatabase.getInstance();
                                            firebaseStorage = FirebaseStorage.getInstance();
                                            mAuth = FirebaseAuth.getInstance();
                                            String us = mAuth.getCurrentUser().getUid();
                                            DatabaseReference database1 = firebaseDatabase.getReference("user").child(us).child("enrolment");
                                            database1.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String roll_no = snapshot.getValue().toString();
                                                    DatabaseReference database2 = firebaseDatabase.getReference().child("WishList").child(roll_no).child(book.getBookID());
                                                    Book book1 = new Book(book.getBookID(), book.getName(), book.getAuthor(), book.getCategory(), book.getQuantity(), "Available", book.getImageUri(), book.getBookUrl());
                                                    database2.setValue(book1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            boolean found =false;
                                                            String id = book.getBookID();
                                                            for(int ii=0;ii < bookList.size();ii++){
                                                                Book this_book = bookList.get(ii);
                                                                if(id.equals(this_book.getBookID())){
                                                                    found = true;
                                                                    break;
                                                                }
                                                            }
                                                            if(!found) {
                                                                if (task.isSuccessful()) {
                                                                    progressDialog.dismiss();
                                                                    Toast.makeText(context, "Added to WishList successfully", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(context, "Error in adding to WishList", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }else{
                                                                progressDialog.dismiss();
                                                                Toast.makeText(context, "This book is already in WishList!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });

                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                            return false;
                        }
                    });

                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Open E-Book in Browser")
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("open", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(book.getBookUrl().equals("Null")){
                                                Toast.makeText(context, "Soft copy of this book is not available right now.", Toast.LENGTH_SHORT).show();

                                            }
                                            else{
                                                Uri uri=Uri.parse(book.getBookUrl());
                                                //https://drive.google.com/file/d/14xX01pnestk4zxG5X16FpMo4E00yTzjp/view?usp=sharing
                                                Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                                                context.startActivity(intent);
                                            }
                                        }
                                    })
                                    .setNegativeButton("cancel",null)
                                    .show();
                        }
                    });
                }
                else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView quantity,bookId , bookStatus,authorName, bookName;
        CircleImageView book_category_image;
        public MyViewHolder(@NonNull View itemView ) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookName);
            authorName = itemView.findViewById(R.id.authorName);
            bookId = itemView.findViewById(R.id.bookId);
            quantity = itemView.findViewById(R.id.quantity);
            book_category_image = itemView.findViewById(R.id.book_category_image);

        }

    }


}
