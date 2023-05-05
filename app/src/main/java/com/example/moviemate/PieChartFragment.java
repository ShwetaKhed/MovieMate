package com.example.moviemate;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.moviemate.databinding.FragmentPieChartBinding;
import com.example.moviemate.model.Movie;
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.service.RetrofitClient;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PieChartFragment extends Fragment {
    private FragmentPieChartBinding binding;
    ArrayList<Movie> finalMovieList = new ArrayList<Movie>();
    Context context;
    public PieChartFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPieChartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        return view;
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Call<MovieResult> call = RetrofitClient.getInstance().getMyApi().
                getPopularMovies1("6f6d1b438fddb937dd48a7f88b87eae7", "2");
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
        PieChart pieChart = view.findViewById(R.id.pieChart1);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(30f, "Slice 1"));
        entries.add(new PieEntry(20f, "Slice 2"));
        entries.add(new PieEntry(50f, "Slice 3"));
        PieDataSet dataSet = new PieDataSet(entries, "Movie Pie Chart");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        pieChart.setUsePercentValues(true);
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(65f);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }
}
