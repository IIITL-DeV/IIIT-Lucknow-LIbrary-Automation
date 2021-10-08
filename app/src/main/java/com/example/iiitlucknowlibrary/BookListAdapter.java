package com.example.iiitlucknowlibrary;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiitlucknowlibrary.Authentication.Login;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Book> bookList;
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
        final Book book = bookList.get(position);
        holder.quantity.setText("Quantity: "+  book.getQuantity() );
        holder.author_name.setText("Author Name: "+ book.getAuthor());
        holder.book_status.setText("Status: "+  book.getStatus() );
        holder.book_id.setText("ID: "+  book.getBookID());
        Picasso.get().load(book.getImageUri()).into(holder.book_category_image);
    }
    @Override
    public int getItemCount() {
        return bookList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView quantity,book_id , book_status,author_name;
        CircleImageView book_category_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.quantity);
            author_name = itemView.findViewById(R.id.author_name);
            book_id = itemView.findViewById(R.id.book_id);
            book_status = itemView.findViewById(R.id.book_status);
            book_category_image = itemView.findViewById(R.id.book_category_image);

        }
    }

}
