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


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


import com.example.moviemate.databinding.LaunchScreenBinding;

import com.example.moviemate.entity.UserMovies;
import com.example.moviemate.viewmodel.UserMoviesViewModel;
import com.example.moviemate.viewmodel.UserViewModel;

import com.example.moviemate.worker.PeriodicWorker;
import com.google.android.material.navigation.NavigationView;

import com.google.gson.Gson;
import com.mapbox.geojson.Point;



import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;

import com.mapbox.maps.Style;

import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LaunchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private LaunchScreenBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    NavigationView nav_view;
    private UserViewModel model;
    private UserMoviesViewModel userMoviesViewModel;
    MapView mapView;

    String userEmail;

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

        saveUserMovieWishlistOnFirebase();
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

    private void saveUserMovieWishlistOnFirebase() {
        model.getLoginEmail().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                System.out.println("email id updated " + s);
                userEmail = s;
            }
        });

        // initializing viewModels
        userMoviesViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserMoviesViewModel.class);
        userMoviesViewModel.getAllUserMovies().observe(this, new Observer<List<UserMovies>>() {
            @Override
            public void onChanged(@Nullable List<UserMovies> movies) {
                System.out.println("On movies adding:" + movies);

                sendOneTimeRequestToWorkManager( userEmail, movies);
            }
        });
    }

    private void sendOneTimeRequestToWorkManager(String userEmail,  List<UserMovies> userMovies)
    {
        Gson gson = new Gson();
        String userMovie_json =  gson.toJson(userMovies);
        System.out.println("Data to send " + userMovie_json);

        //Add work manager here and add the call in queue.
        //Set constraints
        Constraints constraints = new Constraints.Builder()
                //.setRequiredNetworkType(NetworkType.UNMETERED)  //Connect with Unmetered network only
                .build();

        OneTimeWorkRequest oneTimeWorkRequest =
                new OneTimeWorkRequest.Builder(PeriodicWorker.class)
                        .setInputData(  //eg
                                new Data.Builder()
                                        .putString(PeriodicWorker.MOVIE_WISHLIST_KEY, userMovie_json)
                                        .putString(PeriodicWorker.USER_EMAIL_KEY, userEmail)
                                        .build()
                        )
                        .build();

        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null) {
                            if(workInfo.getState().isFinished())
                            {
                                Data outputData = workInfo.getOutputData();
                                String output = outputData.getString("OUTPUT");
                                System.out.println("Output from worker : "+ output + "\n");
                            }
                            String status = workInfo.getState().name();
                            System.out.println(status + "\n");
                        }
                    }
                });
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
    }

    private void sendPeriodicRequestToWorkManager(UserMovies userMovie)
    {
        Gson gson = new Gson();
        String userMovie_json =  gson.toJson(userMovie);
        System.out.println("Data to send " + userMovie_json);
        //Add work manager here and add the call in queue.
        //Set constraints
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)  //Connect with Unmetered network only
                .setRequiresCharging(true)
                //.setTimeOfDay(TimeOfDay.fromHoursOfDay(9, 17)) // Set the time interval between 9 AM and 5 PM
                .build();

        //Call the  PeriodicWorkRequest
        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(PeriodicWorker.class, 1, TimeUnit.MINUTES)
                        .setConstraints(constraints)
                        .setInputData(  //eg
                                new Data.Builder()
                                        .putString(PeriodicWorker.MOVIE_WISHLIST_KEY, userMovie_json)
                                        .build()
                        )
                        .build();

        WorkManager.getInstance().getWorkInfoByIdLiveData(periodicWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null) {
                            if(workInfo.getState().isFinished())
                            {
                                Data outputData = workInfo.getOutputData();
                                String output = outputData.getString("OUTPUT");
                                System.out.println("Output from worker : "+ output + "\n");
                            }
                            String status = workInfo.getState().name();
                            System.out.println("Worker Status : " + status + "\n");
                        }
                    }
                });
        WorkManager.getInstance().enqueue(periodicWorkRequest);
    }
}
