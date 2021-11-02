package com.example.iiitlucknowlibrary;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class WishListAdapter  extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {
    Context context;
    public ArrayList<Book> bookList;
    public WishListAdapter(Context context, ArrayList<Book> book_list) {
        this.context = context;
        this.bookList = book_list;
    }
    @NonNull
    @Override
    public WishListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_booklist_row,parent,false);
        return  new WishListAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.MyViewHolder holder, int position) {
        Book book = bookList.get(position);
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
        public MyViewHolder(@NonNull View itemView ) {
            super(itemView);
            quantity = itemView.findViewById(R.id.quantity);
            author_name = itemView.findViewById(R.id.author_name);
            book_id = itemView.findViewById(R.id.book_id);
            book_status = itemView.findViewById(R.id.book_status);
            book_category_image = itemView.findViewById(R.id.book_category_image);

        }

    }

}
