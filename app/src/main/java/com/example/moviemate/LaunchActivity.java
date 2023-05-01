package com.example.moviemate;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemate.databinding.LaunchScreenBinding;
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.service.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaunchActivity extends AppCompatActivity {
    private LaunchScreenBinding binding;
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_fragment);
        binding = LaunchScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment,
                R.id.nav_booking_fragment,
                R.id.nav_info_fragment,
                R.id.nav_setting_fragment,
                R.id.nav_report_fragment)
                .setOpenableLayout(binding.drawerLayout1)
                .build();
        FragmentManager fragmentManager= getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.appBar.toolbar1,navController,
                mAppBarConfiguration);

    }




}
