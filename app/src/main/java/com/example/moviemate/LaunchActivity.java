package com.example.moviemate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;

import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;



import com.example.moviemate.databinding.LaunchScreenBinding;

import com.example.moviemate.viewmodel.UserViewModel;

import com.google.android.material.navigation.NavigationView;

import com.mapbox.geojson.Point;



import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;

import com.mapbox.maps.Style;

import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class LaunchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private LaunchScreenBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    NavigationView nav_view;
    private UserViewModel model;

    MapView mapView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LaunchScreenBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
        nav_view = findViewById(R.id.nav_view);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.maps_fragment, null);
        MapView mapView = view1.findViewById(R.id.mapView);


        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_booking_fragment,
                R.id.nav_wishlist_fragment,
                R.id.nav_info_fragment,
                R.id.nav_map_fragment,
                R.id.nav_bar_fragment,
                R.id.nav_pie_fragment)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.appBar.toolbar1, navController,
                mAppBarConfiguration);

        model = new ViewModelProvider(this).get(UserViewModel.class);
        Intent intent=getIntent();
        String email = intent.getStringExtra("userEmail");
        model.setLoginEmail(email);

       /* TextView first_name = findViewById(R.id.first_name);
        // Change to name
        first_name.setText("Welcome" + email);*/

        Button sign_out = findViewById(R.id.sign_out);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        launchMaps(this);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        //tvDate.setText(selectedDate);
        System.out.println("selected date " + selectedDate);
        model.setDateOfBirth(selectedDate);
    }

    public void launchMaps(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.maps_fragment, null);
        mapView = view.findViewById(R.id.mapView);
        final Point point = Point.fromLngLat(145.045837, -37.876823 );
        mapView = findViewById(R.id.mapView);
        CameraOptions cameraPosition = new CameraOptions.Builder()
                .zoom(13.0)
                .center(point)
                .build();
    /*    mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
        mapView.getMapboxMap().setCamera(cameraPosition);*/


    }




}
