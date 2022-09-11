package com.example.iiitlucknowlibrary.administration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiitlucknowlibrary.Book;
import com.example.iiitlucknowlibrary.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyBookAdapterForAdmin extends RecyclerView.Adapter<MyBookAdapterForAdmin.MyViewHolder> {

    Context context;
    ArrayList<IssueBookModel> bookList;
    String UserEnrolment;
    String tmp;
    public MyBookAdapterForAdmin(Context context, ArrayList<IssueBookModel> book_list) {
        this.context = context;
        this.bookList = book_list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_mybooks_row,parent,false);
        return  new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final IssueBookModel  issued_book = bookList.get(position);
        String bId = issued_book.getIssueId();
        String bookId = "";
        int i = 0;
        while(i<bId.length() && bId.charAt(i)!=',') {
            bookId += bId.charAt(i++);
        }
        String EnNumber = "";
        i++;
        while(i<bId.length()){
            EnNumber += bId.charAt(i++);
        }

        tmp = EnNumber;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Books").child(bookId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 Book book  = dataSnapshot.getValue(Book.class);

                holder.bookName.setText(tmp + "\nBook Name: " + book.getName());
                Picasso.get().load(book.getImageUri()).into(holder.bookImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        holder.bookId.setText("Book Id: " + bookId);
        holder.issueDate.setText("Issue Date: " + issued_book.getIssueDate());
        holder.returnDate.setText("Return Date: " +issued_book.getReturnDate());
//        Picasso.get().load(issued_book.getImageUri()).into(holder.bookImage);

    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView bookName, bookId, issueDate, returnDate;
        CircleImageView bookImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookName);
            bookId = itemView.findViewById(R.id.bookId);
            issueDate = itemView.findViewById(R.id.issueDate);
            returnDate = itemView.findViewById(R.id.returnDate);
            bookImage = itemView.findViewById(R.id.bookImage);
        }
    }

}
