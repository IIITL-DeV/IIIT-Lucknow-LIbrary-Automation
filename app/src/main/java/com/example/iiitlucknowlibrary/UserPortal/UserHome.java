package com.example.iiitlucknowlibrary.UserPortal;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.iiitlucknowlibrary.Authentication.Login;
import com.example.iiitlucknowlibrary.Book;
import com.example.iiitlucknowlibrary.R;
import com.example.iiitlucknowlibrary.administration.HomeAdministration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHome extends AppCompatActivity {

    FirebaseAuth mAuth;
    BottomNavigationView bottomNavigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    DatabaseReference database;
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

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
        /*
            BottomNavigationView implementation begins

         */


        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.nav_categories);
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
                        startActivity(new Intent(getApplicationContext(),WishList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_categories:
                        return true;
                }
                return true;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(UserHome.this, Login.class));
        }
        else if(Objects.equals(mAuth.getCurrentUser().getEmail(), "admin@gmail.com")){
            startActivity(new Intent(UserHome.this, HomeAdministration.class));
        }
        final RecyclerView recyclerView = findViewById(R.id.categoriesRecyclerView);
        database = FirebaseDatabase.getInstance().getReference("Books");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        HashMap<String, ArrayList<Book>> book_map = new HashMap<String, ArrayList<Book>>();
        ArrayList<Book> list = new ArrayList<>();
        BookAdapter adapter = new BookAdapter(getApplicationContext(),list,book_map);
        recyclerView.setAdapter(adapter);
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Book book = dataSnapshot.getValue(Book.class);
                        assert book != null;
                        String s = book.getCategory();
                        ArrayList<Book> temp = new ArrayList<>();
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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

}