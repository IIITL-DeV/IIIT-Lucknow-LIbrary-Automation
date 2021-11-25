package com.example.iiitlucknowlibrary.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.iiitlucknowlibrary.R;
import com.example.iiitlucknowlibrary.UserPortal.MyBookAdapter;
import com.example.iiitlucknowlibrary.UserPortal.UserProfile;
import com.example.iiitlucknowlibrary.administration.IssueBookModel;
import com.example.iiitlucknowlibrary.databinding.FragmentMyBooksBinding;
import com.example.iiitlucknowlibrary.ui.home.HomeFragment;
import com.example.iiitlucknowlibrary.ui.notifications.WishlistFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyBooksFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MyBookFragment";
    private MyBooksViewModel myBooksViewModel;
    private FragmentMyBooksBinding binding;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CircleImageView image_view;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myBooksViewModel =
                new ViewModelProvider(this).get(MyBooksViewModel.class);

        binding = FragmentMyBooksBinding.inflate(inflater, container, false);
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

        image_view = binding.imageView;
        image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserProfile.class));
            }
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String ss = mAuth.getCurrentUser().getUid();
        DatabaseReference reference11 = FirebaseDatabase.getInstance().getReference("user").child(ss).child("enrolment");

        RecyclerView recyclerView = binding.myBooksListRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<IssueBookModel> book_list = new ArrayList<IssueBookModel>();
        MyBookAdapter myAdapter = new MyBookAdapter(getContext(),book_list);
        recyclerView.setAdapter(myAdapter);
        drawerLayout = binding.drawerLayout;
        toolbar = binding.toolbar;
        navigationView = binding.navigationView;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        reference11.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                //if(snapshot.hasChildren()){
                    String us = snapshot.getValue().toString();
                    DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("IssueBook").child(us);
                    database1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            //Log.d(TAG, "onCreateView: "+snapshot1.getValue());
                            if(snapshot1.hasChildren()){
                                for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                                    IssueBookModel issued_book = dataSnapshot.getValue(IssueBookModel.class);
                                    book_list.add(issued_book);
                                }
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                //}
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.profile:
                startActivity(new Intent(getActivity(), UserProfile.class));
                break;
            case R.id.home_menu:
                startActivity(new Intent(getActivity() , HomeFragment.class));
                break;
            case R.id.login:
            case R.id.logout:
                startActivity(new Intent(getActivity()  , Login.class));
                break;
            case R.id.wish_list:
                startActivity(new Intent(getActivity(), WishlistFragment.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}