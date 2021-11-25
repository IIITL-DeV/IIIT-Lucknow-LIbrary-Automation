package com.example.iiitlucknowlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.iiitlucknowlibrary.Authentication.Login;
import com.example.iiitlucknowlibrary.Authentication.Registration;
import com.example.iiitlucknowlibrary.ui.home.HomeFragment;
import com.example.iiitlucknowlibrary.ui.notifications.WishlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.iiitlucknowlibrary.databinding.ActivityUserHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityUserHomeBinding binding;
    FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(UserHome.this, Login.class));
        }
        else if(mAuth.getCurrentUser().getEmail().equals("admin@gmail.com")){
            startActivity(new Intent(UserHome.this, HomeAdministration.class));
        }

        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_user_home);
        NavigationUI.setupWithNavController(binding.navView, navController);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.profile:
                startActivity(new Intent(UserHome.this , UserProfile.class));
                break;
            case R.id.home_menu:
                startActivity(new Intent(UserHome.this , HomeFragment.class));
                break;
            case R.id.login:
            case R.id.logout:
                startActivity(new Intent(UserHome.this  , Login.class));
                break;
            case R.id.wish_list:
                startActivity(new Intent(UserHome.this , WishlistFragment.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}