package com.example.iiitlucknowlibrary.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiitlucknowlibrary.Authentication.Login;
import com.example.iiitlucknowlibrary.Book;
import com.example.iiitlucknowlibrary.HomeAdministration;
import com.example.iiitlucknowlibrary.R;
import com.example.iiitlucknowlibrary.UserHome;
import com.example.iiitlucknowlibrary.UserPortal.BookAdapter;
import com.example.iiitlucknowlibrary.UserProfile;
import com.example.iiitlucknowlibrary.administration.IssueBook;
import com.example.iiitlucknowlibrary.databinding.FragmentHomeBinding;
import com.example.iiitlucknowlibrary.ui.notifications.WishlistFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CircleImageView image_view;
    FirebaseAuth auth;
    FirebaseDatabase database;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        image_view = binding.imageView;
        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                progressDialog.show();

                startActivity(new Intent(getActivity(),Login.class));
                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });
        drawerLayout = binding.drawerLayout;
        toolbar = binding.toolbar;
        navigationView = binding.navigationView;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.profile:
                        startActivity(new Intent(getActivity() , UserProfile.class));
                        break;
                    case R.id.home_menu:
                        startActivity(new Intent(getActivity() , HomeFragment.class));
                        break;
                    case R.id.login:
                    case R.id.logout:
                        startActivity(new Intent(getActivity() , Login.class));
                        break;
                    case R.id.wish_list:
                        startActivity(new Intent(getActivity() , WishlistFragment.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final RecyclerView recyclerView = binding.booksRecyclerView;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        HashMap<String, ArrayList<Book>> book_map = new HashMap<String, ArrayList<Book>>();
        ArrayList<Book> list = new ArrayList<Book>();
        BookAdapter adapter = new BookAdapter(getContext(),list,book_map);
        recyclerView.setAdapter(adapter);
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book book = dataSnapshot.getValue(Book.class);
                    String s = book.getCategory();
                    ArrayList<Book> temp = new ArrayList<Book>();
                    if(book_map.containsKey(s)){
                        temp = book_map.get(s);
                        temp.add(book);
                        book_map.put(s,temp);
                    }else {
                        temp.add(book);
                        list.add(book);
                        book_map.put(s, temp);
                        temp=null;
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}