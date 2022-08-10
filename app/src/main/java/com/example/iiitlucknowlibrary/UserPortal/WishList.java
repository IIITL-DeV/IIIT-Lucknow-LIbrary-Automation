package com.example.iiitlucknowlibrary.UserPortal;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.iiitlucknowlibrary.Authentication.Login;
import com.example.iiitlucknowlibrary.Book;
import com.example.iiitlucknowlibrary.R;
import com.example.iiitlucknowlibrary.WishListAdapter;
import com.example.iiitlucknowlibrary.administration.HomeAdministration;
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

public class WishList extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private CircleImageView circleImageView;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        final RecyclerView wishListRecyclerView = findViewById(R.id.wishlistRecyclerView);
        wishListRecyclerView.setHasFixedSize(true);
        wishListRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        ArrayList<Book> book_list = new ArrayList<Book>();
        WishListAdapter myAdapter = new WishListAdapter(getApplicationContext(),book_list);
        wishListRecyclerView.setAdapter(myAdapter);
        mAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
        else if(Objects.equals(mAuth.getCurrentUser().getEmail(), "admin@gmail.com")){
            startActivity(new Intent(getApplicationContext(), HomeAdministration.class));
        }
        String userId = mAuth.getCurrentUser().getUid();
        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("user").child(userId).child("enrolment");

        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if(snapshot.hasChildren()){
                String roll_no = snapshot.getValue().toString();
                DatabaseReference database2 = FirebaseDatabase.getInstance().getReference("WishList").child(roll_no);
                database2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        if(snapshot1.hasChildren()){
                            for(DataSnapshot dataSnapshot : snapshot1.getChildren()){
                                Book book = dataSnapshot.getValue(Book.class);
                                book_list.add(book);
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
                        startActivity(new Intent(getApplicationContext(), UserHome.class));
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
                        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
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
        bottomNavigationView.setSelectedItemId(R.id.nav_wishlist);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.nav_mybooks:
                        startActivity(new Intent(getApplicationContext(),MyBooks.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_wishlist:
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