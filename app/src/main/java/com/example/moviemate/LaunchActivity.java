package com.example.moviemate;

import android.os.Bundle;
import android.util.Log;
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

import com.example.moviemate.databinding.HomeFragmentBinding;
import com.example.moviemate.databinding.LaunchScreenBinding;
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.model.androidVersion;
import com.example.moviemate.service.RetrofitClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaunchActivity extends AppCompatActivity {
    private LaunchScreenBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<androidVersion> people;

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
                .setOpenableLayout(binding.drawerLayout)
                .build();
        FragmentManager fragmentManager= getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.appBar.toolbar,navController,
                mAppBarConfiguration);
        getLatestMovies(binding);
        displayMovies();
    }

    private void getLatestMovies(LaunchScreenBinding binding) {
        Call<MovieResult> call = RetrofitClient.getInstance().getMyApi().getLatestMovies();
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                MovieResult movieList = response.body();
                Log.d("tag", String.valueOf(movieList.getResults().size()));
                for (int i = 0; i < movieList.getResults().size(); i++)
                {
                    Log.d("tag", movieList.getResults().get(i).getOriginalTitle());
                    Log.d("tag", movieList.getResults().get(i).getOverview());
                }
                //HomeFragmentBinding.inflate(getLayoutInflater()).tvResult.setText(movieList.getResults().get(1).getOriginalTitle());


            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void displayMovies(){
        recyclerView = findViewById(R.id.ListR);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        people = new ArrayList < androidVersion > ();

        people.add(new androidVersion("Honeycomb", "2.3-3.7", "honeycomb"));
        people.add(new androidVersion("Ice Cream Sandwich", "3.0-3.2.6", "icecreamsandwich"));
        people.add(new androidVersion("JellyBean", "4.0-4.3.1", "jellybean"));
        people.add(new androidVersion("Kitkat", "4.4-4.4.4", "kitkat"));
        people.add(new androidVersion("Lollipop", "5.0-5.1.1", "lollipop"));
        people.add(new androidVersion("Marshmallow", "6.0-6.0.1", "marshmallow"));

        myAdapter = new androidVersionAdapter(this, people);

        recyclerView.setAdapter(myAdapter);
    }



}
