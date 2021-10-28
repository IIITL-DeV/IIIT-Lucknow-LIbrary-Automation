package com.example.iiitlucknowlibrary;

import android.content.Intent;
import android.os.Bundle;

import com.example.iiitlucknowlibrary.Authentication.Registration;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.iiitlucknowlibrary.databinding.ActivityUserHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class UserHome extends AppCompatActivity {

    private ActivityUserHomeBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            startActivity(new Intent(UserHome.this, Registration.class));
        }
        else if(mAuth.getCurrentUser().getEmail().equals("admin@gmail.com")){
            startActivity(new Intent(UserHome.this, HomeAdministration.class));
        }

        binding = ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_user_home);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}