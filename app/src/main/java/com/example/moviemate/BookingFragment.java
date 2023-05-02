package com.example.moviemate;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemate.databinding.LaunchScreenBinding;
import com.example.moviemate.model.Movie;
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.service.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingFragment extends Fragment {

    ArrayList courseName = new ArrayList<>();

    ArrayList img = new ArrayList<>();

    ArrayList<Movie> finalMovieList = new ArrayList<Movie>();
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.context = container.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.booking_fragment, container, false);
    }

    public static BookingFragment newInstance()
    {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);
        Call<MovieResult> call = RetrofitClient.getInstance().getMyApi().getLatestMovies();
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                MovieResult movieList = response.body();

                for (int i = 0; i < movieList.getResults().size(); i++)
                {   Movie movie = new Movie();
                    movie.setOriginalTitle(movieList.getResults().get(i).getOriginalTitle());
                    movie.setPosterPath("http://image.tmdb.org/t/p/w500" + movieList.getResults().get(i).getPosterPath());
                    movie.setReleaseDate(movieList.getResults().get(i).getReleaseDate());
                    movie.setOverview(movieList.getResults().get(i).getOverview());
                    movie.setOverview(movieList.getResults().get(i).getOverview());
                    finalMovieList.add(movie);
                }
                Adapter itemAdapter = new Adapter(getContext(),finalMovieList);
                RecyclerView recyclerView
                        = view.findViewById(R.id.recycleView);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(getContext()));

                // adapter instance is set to the
                // recyclerview to inflate the items.
                recyclerView.setAdapter(itemAdapter);


            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }

        });
    }


}
