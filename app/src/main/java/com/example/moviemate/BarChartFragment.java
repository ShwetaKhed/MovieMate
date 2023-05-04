package com.example.moviemate;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.moviemate.R;
import com.example.moviemate.databinding.ActivityMainBinding;
import com.example.moviemate.databinding.FragmentBarReportBinding;
import com.github.mikephil.charting.components.Description;

import com.example.moviemate.model.Movie;
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.service.RetrofitClient;
import com.example.moviemate.viewmodel.SharedViewModel;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarChartFragment extends Fragment {
    private FragmentBarReportBinding binding;
    ArrayList<Movie> finalMovieList = new ArrayList<Movie>();
    Context context;
    public BarChartFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBarReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        return view;
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);


        List<BarEntry> movieEntries = new ArrayList<>();
        movieEntries.add(new BarEntry(0, 100));
        movieEntries.add(new BarEntry(1, 48));
        movieEntries.add(new BarEntry(2, 23));
        movieEntries.add(new BarEntry(3, 84));
        movieEntries.add(new BarEntry(4, 11));


        BarDataSet barDataSet = new BarDataSet(movieEntries, "Popularity");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        Legend legend = binding.barChart.getLegend();
        legend.setTextColor(Color.WHITE);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("Mv1", "Mv2", "Mv3", "Mv4", "Mv5"));

        binding.barChart.getX();
        XAxis xAxis = binding.barChart.getXAxis();
        binding.barChart.getXAxis().setValueFormatter(new
                com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);

        BarData barData = new BarData(barDataSet);
        binding.barChart.setData(barData);
        barData.setBarWidth(1.0f);
        binding.barChart.setVisibility(View.VISIBLE);
        binding.barChart.animateY(4000);


        //description will be displayed as "Description Label" if not provided
        Description description = new Description();
        description.setText("Popularity for Each Movie");
        binding.barChart.setDescription(description);
        //refresh the chart
        binding.barChart.invalidate();


        Call<MovieResult> call = RetrofitClient.getInstance().getMyApi().
                getPopularMovies();
        call.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                MovieResult movieList = response.body();
                for (int i = 0; i < movieList.getResults().size(); i++)
                {   Movie movie = new Movie();
                    Log.d("title", movieList.getResults().get(i).getOriginalTitle());
                    movie.setOriginalTitle(movieList.getResults().get(i).getOriginalTitle());
                    movie.setReleaseDate(movieList.getResults().get(i).getReleaseDate());
                    movie.setPopularity(movieList.getResults().get(i).getPopularity());
                    movie.setPopularity(movieList.getResults().get(i).getOriginal_language());
                    finalMovieList.add(movie);
                }
            }
            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }

        });
    }
}
