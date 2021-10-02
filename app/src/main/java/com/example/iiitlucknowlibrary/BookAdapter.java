package com.example.iiitlucknowlibrary;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    Context context;
    ArrayList<Book> list;
    HashMap<String, ArrayList<Book>> book_map;
    public BookAdapter(Context context, ArrayList<Book> list, HashMap<String, ArrayList<Book>> book_map) {
        this.context = context;
        this.list = list;
        this.book_map = book_map;
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
        String s = book.getCategory();
        ArrayList<Book> temp = book_map.get(s);
        if(temp.size() == 0) {
            SpannableStringBuilder builder = new SpannableStringBuilder();

            String red = " No book available";
            SpannableString redSpannable= new SpannableString(red);
            redSpannable.setSpan(new ForegroundColorSpan(Color.RED), 0, red.length(), 0);
            builder.append(redSpannable);
            holder.book_quantity.setText("Status: " + red);
        }
        else if(temp.size() == 1) {
            holder.book_quantity.setText("Status: "+temp.size()  + " book available");
        }
        else {
            holder.book_quantity.setText("Status: "+temp.size() + " books available");
        }
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
