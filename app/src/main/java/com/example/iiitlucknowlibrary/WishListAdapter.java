package com.example.iiitlucknowlibrary;

import android.annotation.SuppressLint;
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

public class WishListAdapter  extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {
    Context context;
    WishListAdapter myAdapter;
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
    public void onBindViewHolder(@NonNull WishListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Book book = bookList.get(position);
        holder.quantity.setText("Quantity: "+  book.getQuantity() );
        holder.author_name.setText("Author Name: "+ book.getAuthor());
        holder.book_status.setText("Status: "+  book.getStatus() );
        holder.book_id.setText("ID: "+  book.getBookID());
        Picasso.get().load(book.getImageUri()).into(holder.book_category_image);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Remove from WishList")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                bookList.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
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
                                Uri uri=Uri.parse("https://drive.google.com/file/d/14xX01pnestk4zxG5X16FpMo4E00yTzjp/view?usp=sharing");
                                Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                                context.startActivity(intent);
                            }
                        })
                        .setNegativeButton("cancel",null)
                        .show();
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
            quantity = itemView.findViewById(R.id.quantity);
            author_name = itemView.findViewById(R.id.author_name);
            book_id = itemView.findViewById(R.id.book_id);
            book_status = itemView.findViewById(R.id.book_status);
            book_category_image = itemView.findViewById(R.id.book_category_image);

        }

    }

}
