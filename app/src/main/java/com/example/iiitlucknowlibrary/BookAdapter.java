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
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    Context context;
    ArrayList<Book> list;
    public BookAdapter(Context context, ArrayList<Book> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_books_row,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Book book = list.get(position);
        holder.book_category_name.setText(book.getCategory());
        holder.book_quantity.setText("Status: "+  book.getQuantity() + " books available");
       Picasso.get().load(book.getImageUri()).into(holder.book_category_image);
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(context,BookList.class);
               intent.putExtra("categoryName",book.getCategory());
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
           }
       });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView book_category_name , book_quantity;
        CircleImageView book_category_image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            book_quantity = itemView.findViewById(R.id.book_quantity);
            book_category_image = itemView.findViewById(R.id.book_category_image);
            book_category_name = itemView.findViewById(R.id.book_category_name);
        }
    }

}
