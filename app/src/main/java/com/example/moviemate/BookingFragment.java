package com.example.moviemate;


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
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.service.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingFragment extends Fragment {

    ArrayList courseName = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.booking_fragment,
                container, false);
    }

    public static BookingFragment newInstance(String param1,
                                            String param2)
    {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        //args.putString("param1", param1);
        //args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState)
    {
        courseName = getLatestMovies();
        super.onViewCreated(view, savedInstanceState);

        Adapter itemAdapter = new Adapter(courseName);

        RecyclerView recyclerView
                = view.findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        // adapter instance is set to the
        // recyclerview to inflate the items.
        recyclerView.setAdapter(itemAdapter);

    }

    private ArrayList<String> getLatestMovies() {
        Call<MovieResult> call = RetrofitClient.getInstance().getMyApi().getLatestMovies();
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                MovieResult movieList = response.body();
                Log.d("tag", String.valueOf(movieList.getResults().size()));
                for (int i = 0; i < movieList.getResults().size(); i++)
                {
                    courseName.add(movieList.getResults().get(i).getOriginalTitle());
                }


            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }

        });
        courseName.add("13hbjhsdbf");
        return courseName;
    }
}
