package com.example.iiitlucknowlibrary.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiitlucknowlibrary.UserPortal.MyBookAdapter;
import com.example.iiitlucknowlibrary.administration.IssueBookModel;
import com.example.iiitlucknowlibrary.databinding.FragmentDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MyBooksFragment extends Fragment {

    private MyBooksViewModel myBooksViewModel;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myBooksViewModel =
                new ViewModelProvider(this).get(MyBooksViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textDashboard;
        myBooksViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });


        /*
                start
         */

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        RecyclerView recyclerView;
        FirebaseStorage firebaseStorage;
        TextView no_book;

        firebaseStorage = FirebaseStorage.getInstance();
        String uId = mAuth.getCurrentUser().getUid().toString();
        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("user").child(uId).child("enrolment");

        recyclerView = binding.myBooksListRecyclerView;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<IssueBookModel> book_list = new ArrayList<IssueBookModel>();
        MyBookAdapter myAdapter = new MyBookAdapter(getContext(),book_list);
        recyclerView.setAdapter(myAdapter);
        no_book = binding.noBooks;

        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String s = snapshot.getValue().toString();
                    if(s.isEmpty()){
                        no_book.setText("NO BOOK TO SHOW :(");
                    }else {
                        DatabaseReference database2 = FirebaseDatabase.getInstance().getReference("IssueBook");
                        database2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    IssueBookModel issued_book = dataSnapshot.getValue(IssueBookModel.class);
                                    book_list.add(issued_book);
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
                End
         */


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}