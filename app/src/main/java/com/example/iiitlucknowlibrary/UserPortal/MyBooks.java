package com.example.iiitlucknowlibrary.UserPortal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.iiitlucknowlibrary.Authentication.Login;
import com.example.iiitlucknowlibrary.R;
import com.example.iiitlucknowlibrary.administration.HomeAdministration;
import com.example.iiitlucknowlibrary.administration.IssueBookModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyBooks extends AppCompatActivity {
     BottomNavigationView bottomNavigationView;
     FirebaseAuth mAuth;
    DatabaseReference reference11;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);


         mAuth = FirebaseAuth.getInstance();
        String ss = mAuth.getCurrentUser().getUid();
        reference11 = FirebaseDatabase.getInstance().getReference("user").child(ss).child("enrolment");

        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

        final RecyclerView recyclerView =findViewById(R.id.myBooksRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ArrayList<IssueBookModel> book_list = new ArrayList<IssueBookModel>();
        MyBookAdapter myAdapter = new MyBookAdapter(getApplicationContext(),book_list);
        recyclerView.setAdapter(myAdapter);

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
        circleImageView = findViewById(R.id.profile_imageView);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserProfile.class));
            }
        });
        // UserHome means Categories here
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this
                ,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Side navigation Drawer implementation begins

        navigationView.bringToFront();
        drawerLayout.requestLayout();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.categories_menu:
                        startActivity(new Intent(getApplicationContext() , UserHome.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile_menu:
                        startActivity(new Intent(getApplicationContext() , UserProfile.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.logout_menu:
                        FirebaseAuth.getInstance().signOut();
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken("297181861064-7ahb7gh8b3tacknplv05ak57avgte8oa.apps.googleusercontent.com")
                                .requestEmail()
                                .build();
                        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MyBooks.this, gso);
                        mGoogleSignInClient.signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));

                        overridePendingTransition(0,0);
                        return true;
                    case R.id.wishlist_menu:
                        startActivity(new Intent(getApplicationContext() , WishList.class));
                        overridePendingTransition(0,0);

                        return true;
                    case R.id.mybooks_menu:
                        startActivity(new Intent(getApplicationContext() , MyBooks.class));
                        overridePendingTransition(0,0);

                        return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // BottomNavigationView implementation beginss
        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_mybooks);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.nav_mybooks:
                        return true;
                    case R.id.nav_wishlist:
                        startActivity(new Intent(getApplicationContext(),WishList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_categories:
                        startActivity(new Intent(getApplicationContext(),UserHome.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return true;
            }
        });

    }
}