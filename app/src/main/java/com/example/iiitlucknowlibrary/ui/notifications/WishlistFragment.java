package com.example.iiitlucknowlibrary.ui.notifications;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iiitlucknowlibrary.Authentication.Login;
import com.example.iiitlucknowlibrary.Book;
import com.example.iiitlucknowlibrary.R;
import com.example.iiitlucknowlibrary.UserProfile;
import com.example.iiitlucknowlibrary.WishListAdapter;
import com.example.iiitlucknowlibrary.databinding.FragmentWishlistBinding;
import com.example.iiitlucknowlibrary.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class WishlistFragment extends Fragment  implements NavigationView.OnNavigationItemSelectedListener{

    private WishlistViewModel wishlistViewModel;
    private FragmentWishlistBinding binding;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wishlistViewModel =
                new ViewModelProvider(this).get(WishlistViewModel.class);

        binding = FragmentWishlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        wishlistViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        RecyclerView wishListRecyclerView = binding.wishListRecyclerView;
        wishListRecyclerView.setHasFixedSize(true);
        wishListRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ArrayList<Book> book_list = new ArrayList<Book>();
        WishListAdapter myAdapter = new WishListAdapter(getContext(),book_list);
        wishListRecyclerView.setAdapter(myAdapter);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("user").child(userId).child("enrolment");
        drawerLayout = binding.drawerLayout;
        toolbar = binding.toolbar;
        navigationView = binding.navigationView;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar, R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String roll_no = snapshot.getValue().toString();
                DatabaseReference database2 = FirebaseDatabase.getInstance().getReference("WishList").child(roll_no);
                database2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            for(DataSnapshot dataSnapshot : snapshot1.getChildren()){
                                Book book = dataSnapshot.getValue(Book.class);
                                book_list.add(book);
                            }
                            myAdapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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