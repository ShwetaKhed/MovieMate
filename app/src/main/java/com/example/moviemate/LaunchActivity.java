package com.example.moviemate;


import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;


import android.os.Bundle;


import android.os.Debug;
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
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


import com.example.moviemate.databinding.LaunchScreenBinding;

import com.example.moviemate.entity.UserMovies;

import com.example.moviemate.viewmodel.SharedViewModel;
import com.example.moviemate.viewmodel.UserMoviesViewModel;
import com.example.moviemate.viewmodel.UserViewModel;

import com.example.moviemate.worker.PeriodicWorker;
import com.google.android.material.navigation.NavigationView;


import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.Gson;

import com.google.type.TimeOfDay;
import com.mapbox.mapboxsdk.maps.MapboxMap;



import android.widget.DatePicker;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.widget.Button;


import androidx.annotation.NonNull;
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
import com.example.moviemate.model.Location;
import com.example.moviemate.service.RetrofitClient;
import com.example.moviemate.service.RetrofitClientMaps;
import com.example.moviemate.viewmodel.UserMoviesViewModel;
import com.example.moviemate.viewmodel.UserViewModel;

import com.example.moviemate.worker.PeriodicWorker;
import com.google.android.material.navigation.NavigationView;


import com.google.gson.Gson;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;


import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaunchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private LaunchScreenBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    NavigationView nav_view;
    private UserViewModel model;
    private UserMoviesViewModel userMoviesViewModel;
    private SharedViewModel sharedViewModel;

    List<UserMovies> allWishlistMovies = new ArrayList<UserMovies>();

    double lat = 0.0;
    double longitutde = 0.0;


    String userEmail;

    MapboxMap mapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LaunchScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        nav_view = findViewById(R.id.nav_view);
        LayoutInflater inflater = LayoutInflater.from(this);
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

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

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
        // Get the current time
        Calendar now = Calendar.getInstance();
        if(mCalendar.getTimeInMillis() > now.getTimeInMillis())
        {
            Toast.makeText(this, "Please select a valid date", Toast.LENGTH_SHORT).show();
            return;
        }
        //tvDate.setText(selectedDate);
        System.out.println("selected date " + selectedDate);
        //Compare here with the today's date user should not be able to select the future date.
        model.setDateOfBirth(selectedDate);
        sharedViewModel.setStartDate(selectedDate);
        sharedViewModel.setEndDate(selectedDate);

    }

    public void launchMaps(Context context) {

    }

    private void saveUserMovieWishlistOnFirebase() {
        model.getLoginEmail().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                System.out.println("email id updated " + s);
                userEmail = s;
            }
        });

        userMoviesViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserMoviesViewModel.class);
//        userMoviesViewModel.getAllUserMovies().observe(this, new Observer<List<UserMovies>>() {
//            @Override
//            public void onChanged(@Nullable List<UserMovies> movies) {
//                System.out.println("on Movie add to wishlist");
//                if(movies.size()>0)
//                sendOneTimeRequestToWorkManager( userEmail, movies); //Keep it for the demo only.
//
//            }
//        });

        userMoviesViewModel.getAllUserMovies().observe(this, userMovies -> {
            allWishlistMovies.clear();
            for (UserMovies movie1: userMovies
            ) {
                Log.d(movie1.originalTitle, movie1.overview);
                Log.d("User email", movie1.getUserEmail());
                allWishlistMovies.add(movie1);
            }
            if(allWishlistMovies.size()>0)
                getCurrentPeriodicWorkers();
        } );
    }


    //Check if any periodic worker exists if yes than utilize that only
    private void getCurrentPeriodicWorkers()
    {
        ListenableFuture<List<WorkInfo>> future =  WorkManager.getInstance(this ).getWorkInfosByTag(PeriodicWorker.PERIODIC_WORKER_TAG);//WorkManager.getInstance().getWorkInfosByTag(PeriodicWorker.PERIODIC_WORKER_TAG);
        try {
            List<WorkInfo> workersInfo = future.get();
            for (WorkInfo workInfo : workersInfo) {
                //if (workInfo.getState() == WorkInfo.State.RUNNING)
                {
                    String workerClassName = workInfo.getClass().getName();
                    UUID workerId = workInfo.getId();
                    System.out.println("current running worker " +  workerClassName + ", uuid " + workerId);
                }
            }
            if(workersInfo == null || workersInfo.size()==0)
            {
                System.out.println("No worker exist start he new worker");
                sendPeriodicRequestToWorkManager(userEmail, allWishlistMovies);
            }
            else {
                System.out.println("Worker already exist update it with id  " + workersInfo.get(0).getId());
                updatePeriodicRequestToWorkManager(userEmail, allWishlistMovies,workersInfo.get(0).getId());
            }
        } catch (ExecutionException | InterruptedException e) {
            // Handle the exception
            System.out.println("exception  running worker " + e);
        }
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

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null) {
                            String status = workInfo.getState().name();
                            System.out.println("Worker Status: "+ status + "\n");
                            //if successfully saved on the server it's going to delete the current room data
                            if(status =="SUCCEEDED")
                            {
                                //Delete room live data here
                                userMoviesViewModel.deleteAll();
                                PeriodicWorker.isDataSaveOnFireBase = false;
                            }
                        }
                    }
                });
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest);
    }

    private void sendPeriodicRequestToWorkManager(String userEmail,  List<UserMovies> userMovies)
    {
        Gson gson = new Gson();
        String userMovie_json =  gson.toJson(userMovies);
        System.out.println(" Data to send on firebase by Worker" + userMovie_json);

        //Add work manager here and add the call in queue.
        //Set constraints
        Constraints constraints = new Constraints.Builder()
                .build();

        //Get the initial delay time
        long initialDelay = getDelayUntilOfNight(23,0);

        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(PeriodicWorker.class, 1, TimeUnit.DAYS)
                        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                        .setConstraints(constraints)
                        .addTag(PeriodicWorker.PERIODIC_WORKER_TAG)
                        .setInputData(
                                new Data.Builder()
                                        .putString(PeriodicWorker.MOVIE_WISHLIST_KEY, userMovie_json)
                                        .putString(PeriodicWorker.USER_EMAIL_KEY, userEmail)
                                        .build()
                        )
                        .build();

        // var guid = periodicWorkRequest.getId();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null) {
                            String status = workInfo.getState().name();
                            System.out.println("Worker Status : " + status + "\n");
                            //if successfully saved on the server it's going to delete the current room data
                            if (PeriodicWorker.isDataSaveOnFireBase)
                            {
                                //Delete room live data here
                                userMoviesViewModel.deleteAll();
                                PeriodicWorker.isDataSaveOnFireBase = false;
                            }
                        }
                    }
                });

        WorkManager.getInstance(this).enqueue(
                periodicWorkRequest
        );
    }

    private void updatePeriodicRequestToWorkManager(String userEmail,  List<UserMovies> userMovies, UUID uuid)
    {
        Gson gson = new Gson();
        String userMovie_json =  gson.toJson(userMovies);
        System.out.println(" Data to send on firebase by Worker" + userMovie_json);

        //Add work manager here and add the call in queue.
        //Set constraints
        Constraints constraints = new Constraints.Builder()
                .build();

        //Get the initial delay time
        long initialDelay = getDelayUntilOfNight(10,15);
        // Create new WorkRequest from existing Worker, new constraints, and the id of the old WorkRequest.
        PeriodicWorkRequest updatedWorkRequest =
                new PeriodicWorkRequest.Builder(PeriodicWorker.class, 1, TimeUnit.DAYS)
                        .setId(uuid)
                        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                        .setConstraints(constraints)
                        .addTag(PeriodicWorker.PERIODIC_WORKER_TAG)
                        .setInputData(
                                new Data.Builder()
                                        .putString(PeriodicWorker.MOVIE_WISHLIST_KEY, userMovie_json)
                                        .putString(PeriodicWorker.USER_EMAIL_KEY, userEmail)
                                        .build()
                        )
                        .build();

        // var guid = periodicWorkRequest.getId();
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(updatedWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null) {
                            String status = workInfo.getState().name();
                            System.out.println("Updated Worker Status : " + status + "\n");
                            //if successfully saved on the server it's going to delete the current room data
                            if (PeriodicWorker.isDataSaveOnFireBase)
                            {
                                //Delete room live data here
                                userMoviesViewModel.deleteAll();
                                PeriodicWorker.isDataSaveOnFireBase = false;
                            }
                        }
                    }
                });


        System.out.println("Worker has been updated");
        // Pass the new WorkRequest to updateWork().
        WorkManager.getInstance(this).updateWork(updatedWorkRequest);

    }

    public static long getDelayUntilOfNight(int hour, int minute) {
        // Get the current time
        Calendar now = Calendar.getInstance();
        //Get the current time in millis
        long currentTimeMillis = now.getTimeInMillis();

        // Set the target time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        System.out.println("cal time " +  calendar.getTime());
        // Add one day in case of if the new time is before the current time
        if (calendar.before(now)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Calculate the delay until the target time
        long delayMillis = calendar.getTimeInMillis() - currentTimeMillis;
        //return the delay in millis
        return delayMillis;
    }
}
