package com.example.moviemate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviemate.model.Movie;
import com.google.gson.Gson;

public class MovieContentActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_content);
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
