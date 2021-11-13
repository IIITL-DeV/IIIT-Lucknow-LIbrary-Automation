package com.example.iiitlucknowlibrary.UserPortal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiitlucknowlibrary.R;
import com.example.iiitlucknowlibrary.administration.IssueBookModel;

import java.util.ArrayList;

public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.MyViewHolder> {

    Context context;
    ArrayList<IssueBookModel> bookList;
    public MyBookAdapter(Context context, ArrayList<IssueBookModel> book_list) {
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
        holder.my_book_id.setText("Id: " + issued_book.getIssueId());
        holder.my_issue_date.setText("Issue Date: " + issued_book.getIssueDate());
        holder.my_return_date.setText("Return Date: " +issued_book.getReturnDate());

    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView my_book_id ,my_issue_date,my_return_date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            my_book_id = itemView.findViewById(R.id.my_book_id);
            my_issue_date = itemView.findViewById(R.id.my_issue_date);
            my_return_date = itemView.findViewById(R.id.my_return_date);
        }
    }

}
