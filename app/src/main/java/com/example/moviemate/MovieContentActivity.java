package com.example.moviemate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MovieContentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");
        String email = getIntent().getStringExtra("userEmail");
        Log.d("emailemailemail", email);

        TextView title = (TextView) findViewById(R.id.movieTitle);
        TextView overview = (TextView) findViewById(R.id.movieOverview);
        TextView releaseDate = (TextView) findViewById(R.id.movieDate);

        ImageView imageView = (ImageView) findViewById(R.id.ivMovie);
        title.setText(movie.getOriginalTitle());
        overview.setText(movie.getOverview());
        releaseDate.setText(movie.getReleaseDate());
        Glide.with(this).load(movie.getPosterPath()).into(imageView);

        // initializing viewModels
        userMoviesViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(UserMoviesViewModel.class);

        // adding movie to room database on wishlist button
        binding.wishlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserMovies userMovie = new UserMovies(email, movie.getOriginalTitle(), movie.getPosterPath(),
                        movie.getOverview(), movie.getId(), movie.getReleaseDate());
                userMoviesViewModel.insert(userMovie);
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieContentActivity.this);
                builder.setTitle("Movie added to wishlist");
                builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                    finish();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
