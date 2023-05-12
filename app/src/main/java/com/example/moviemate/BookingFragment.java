package com.example.moviemate;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemate.databinding.BookingFragmentBinding;
import com.example.moviemate.databinding.InfoFragmentBinding;
import com.example.moviemate.databinding.LaunchScreenBinding;
import com.example.moviemate.model.Movie;
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.model.User;
import com.example.moviemate.service.RetrofitClient;
import com.example.moviemate.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingFragment extends Fragment {


    ArrayList<Movie> finalMovieList = new ArrayList<Movie>();

    ArrayList<Movie> indMovieList = new ArrayList<Movie>();
    Context context;

    String userEmail;

    private BookingFragmentBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = BookingFragmentBinding.inflate(inflater, container, false);
        this.context = container.getContext();
        View view = binding.getRoot();
        Button myButton = (Button) view.findViewById(R.id.btnSearch);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = String.valueOf(binding.searchText.getText());
                indMovieList.clear();
                if (keyword.isEmpty())
                {
                    for (int i = 0; i < finalMovieList.size(); i++) {
                        indMovieList.add(finalMovieList.get(i));
                    }

                }
                else {
                    for (int i = 0; i < finalMovieList.size(); i++) {
                        String movieName = finalMovieList.get(i).getOriginalTitle().toLowerCase();
                        if (movieName.contains(keyword.toLowerCase())) {
                            indMovieList.add(finalMovieList.get(i));
                            break;
                        }
                    }
                }

                Adapter itemAdapter = new Adapter(getContext(),indMovieList, userEmail);
                RecyclerView recyclerView
                        = view.findViewById(R.id.recycleView);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(getContext()));

                // adapter instance is set to the
                // recyclerview to inflate the items.
                recyclerView.setAdapter(itemAdapter);
            }
        });


        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.booking_fragment, container, false);
        return view;
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
        Call<MovieResult> call = RetrofitClient.getInstance().getMyApi().getUpcoming();
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                MovieResult movieList = response.body();

                for (int i = 0; i < movieList.getResults().size(); i++)
                {   Movie movie = new Movie();
                    movie.setOriginalTitle(movieList.getResults().get(i).getOriginalTitle());
                    movie.setPosterPath("http://image.tmdb.org/t/p/w500" + movieList.getResults().get(i).getPosterPath());
                    movie.setReleaseDate(movieList.getResults().get(i).getReleaseDate());
                    movie.setAdult(movieList.getResults().get(i).getAdult());
                    movie.setOriginal_language(movieList.getResults().get(i).getOriginal_language());
                    movie.setOverview(movieList.getResults().get(i).getOverview());
                    finalMovieList.add(movie);
                }
                UserViewModel model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
                model.getLoginEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        System.out.println("email" + s);
                        BookingFragment.this.userEmail = s;

                    }
                });
                Adapter itemAdapter = new Adapter(getContext(),finalMovieList, userEmail);
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
