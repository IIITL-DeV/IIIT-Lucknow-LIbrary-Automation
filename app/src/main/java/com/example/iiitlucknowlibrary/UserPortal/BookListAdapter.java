package com.example.iiitlucknowlibrary.UserPortal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiitlucknowlibrary.Book;
import com.example.iiitlucknowlibrary.R;
import com.example.iiitlucknowlibrary.administration.IssueBook;
import com.example.iiitlucknowlibrary.administration.IssueBookModel;
import com.example.iiitlucknowlibrary.ui.dashboard.MyBooksFragment;
import com.example.iiitlucknowlibrary.ui.home.HomeFragment;
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
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.MyViewHolder> {

    Context context;
    public ArrayList<Book> bookList;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    FirebaseAuth mAuth;
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
        holder.quantity.setText("Quantity: "+  book.getQuantity() );
        holder.author_name.setText("Author Name: "+ book.getAuthor());
        holder.book_status.setText("Status: "+  book.getStatus() );
        holder.book_id.setText("ID: "+  book.getBookID());
        Picasso.get().load(book.getImageUri()).into(holder.book_category_image);
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);
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
                                       Book book1 = new Book(book.getBookID(), book.getName(), book.getAuthor(), book.getCategory(), book.getQuantity(), "Available", book.getImageUri());
                                       database2.setValue(book1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){
                                                   progressDialog.dismiss();
                                                   Toast.makeText(context, "Added to WishList successfully", Toast.LENGTH_SHORT).show();
                                               }
                                               else{
                                                   Toast.makeText(context, "Error in adding to WishList", Toast.LENGTH_SHORT).show();
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
    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView quantity,book_id , book_status,author_name;
        CircleImageView book_category_image;
        public MyViewHolder(@NonNull View itemView ) {
            super(itemView);
            int position = this.getAdapterPosition();
            quantity = itemView.findViewById(R.id.quantity);
            author_name = itemView.findViewById(R.id.author_name);
            book_id = itemView.findViewById(R.id.book_id);
            book_status = itemView.findViewById(R.id.book_status);
            book_category_image = itemView.findViewById(R.id.book_category_image);

        }

    }


}
