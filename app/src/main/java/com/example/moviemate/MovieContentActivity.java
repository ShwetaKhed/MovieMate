package com.example.moviemate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviemate.databinding.LaunchScreenBinding;
import com.example.moviemate.databinding.MovieContentBinding;
import com.example.moviemate.entity.UserMovies;
import com.example.moviemate.model.Movie;
import com.example.moviemate.viewmodel.UserMoviesViewModel;
import com.example.moviemate.viewmodel.UserViewModel;
import com.google.gson.Gson;

public class MovieContentActivity extends AppCompatActivity {
    private MovieContentBinding binding;
    private UserMoviesViewModel userMoviesViewModel;
    private UserViewModel userViewModel;
    private String userEmail;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MovieContentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        TextView title = (TextView) findViewById(R.id.movieTitle);
        TextView overview = (TextView) findViewById(R.id.movieOverview);
        Log.d("TAG", movie.getPosterPath());
       /* TextView releaseDate = (TextView) findViewById(R.id.releaseDate);
        TextView adult = (TextView) findViewById(R.id.adult);
        TextView overview = (TextView) findViewById(R.id.movieOverview);*/
        ImageView imageView = (ImageView) findViewById(R.id.ivMovie);
        title.setText(movie.getOriginalTitle());
        overview.setText(movie.getOverview());
        Glide.with(this).load(movie.getPosterPath()).into(imageView);
        /*releaseDate.setText("Release Date" + movie.getReleaseDate());
        adult.setText(movie.getAdult());
       */
        //Glide.with(imageView.getContext()).load(movieImage.get(position)).into(holder.imageView);

        // initializing viewModels
        userMoviesViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserMoviesViewModel.class);
        userViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserViewModel.class);

        userViewModel.getLoginEmail().observe(this, loginEmail -> {
            userEmail = loginEmail;
            Log.d("Current logged in user", userEmail);
        });
        // adding movie to room database on wishlist button
        binding.wishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserMovies userMovie = new UserMovies(userEmail, movie.getOriginalTitle(), movie.getPosterPath(),
                        movie.getOverview(), movie.getId(), movie.getReleaseDate());
                userMoviesViewModel.insert(userMovie);
            }
        });

        userMoviesViewModel.getAllUserMovies().observe(this, userMovies -> {
            for (UserMovies movie1: userMovies
                 ) {
                    Log.d(movie1.originalTitle, movie1.overview);
                    Log.d("User email", movie1.getUserEmail());
            }
        } );
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
