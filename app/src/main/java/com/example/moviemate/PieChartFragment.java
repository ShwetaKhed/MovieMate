package com.example.moviemate;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.databinding.FragmentPieChartBinding;
import com.example.moviemate.model.Movie;
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.service.RetrofitClient;
import com.example.moviemate.viewmodel.SharedViewModel;
import com.example.moviemate.viewmodel.UserViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PieChartFragment extends Fragment {
    private FragmentPieChartBinding binding;
    ArrayList<Movie> finalMovieList = new ArrayList<Movie>();
    Context context;
    boolean showReport = false;

    private boolean isStartDateChanged;
    private boolean isEndDateChanged;
    public PieChartFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentPieChartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        System.out.println("Pie chart acticity " + this.getActivity());
        for (int i = 450; i < 500; i++) {

            Call<MovieResult> call = RetrofitClient.getInstance().getMyApi().
                    getPopularMovies1("6f6d1b438fddb937dd48a7f88b87eae7", String.valueOf(i));
            call.enqueue(new Callback<MovieResult>() {

                @Override
                public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                    MovieResult movieList = response.body();
                    for (int j = 0; j < movieList.getResults().size(); j++) {
                        Movie movie = new Movie();
                        movie.setOriginalTitle(movieList.getResults().get(j).getOriginalTitle());
                        movie.setReleaseDate(movieList.getResults().get(j).getReleaseDate());
                        movie.setPopularity(movieList.getResults().get(j).getPopularity());
                        movie.setPopularity(movieList.getResults().get(j).getOriginal_language());
                        finalMovieList.add(movie);
                        if (finalMovieList.size() == 1000)
                        {
                            try {
                                createPieChart(view);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }


                }
                @Override
                public void onFailure(Call<MovieResult> call, Throwable t) {

                }

            });
        }

        binding.startDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    System.out.println("Clicked on start date");
                    DatePicker mDatePickerDialogFragment = new DatePicker();
                    mDatePickerDialogFragment.show(getChildFragmentManager(), DatePicker.TAG);
                    isStartDateChanged = true;
                    return true;
                }
                return false;
            }
        });

        binding.endDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    System.out.println("Clicked on end date");
                    DatePicker mDatePickerDialogFragment = new DatePicker();
                    mDatePickerDialogFragment.show(getChildFragmentManager(), DatePicker.TAG);
                    isEndDateChanged = true;
                    return true;
                }
                return false;
            }
        });

        SharedViewModel sharedViewmodel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewmodel.getStartDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                System.out.println("Start date" + s);
                if(isStartDateChanged)
                {
                    isStartDateChanged = false;
                    binding.startDate.setText(s);
                }
            }
        });
        sharedViewmodel.getStartDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                System.out.println("End date set on " + s);
                if(isEndDateChanged)
                {
                    isEndDateChanged = false;
                    binding.endDate.setText(s);
                }
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void createPieChart(View view) throws ParseException {
        ArrayList<Movie> topMovies = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < finalMovieList.size(); i++)
        {
            int count = 0;
            Date startDate = dateFormat.parse("2013-05-16");
            Date endDate = dateFormat.parse("2019-03-01");
            Date releaseDate = dateFormat.parse(finalMovieList.get(i).getReleaseDate());

            if (releaseDate.after(startDate) && releaseDate.before(endDate)) {
                System.out.println("Date is between start and end dates.");

                    topMovies.add(finalMovieList.get(i));

            }
        }
        Log.d("Count", String.valueOf(topMovies.size()));
        PieChart pieChart = view.findViewById(R.id.pieChart1);
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(20f, topMovies.get(0).getOriginalTitle()));
        entries.add(new PieEntry(20f, topMovies.get(1).getOriginalTitle()));
        entries.add(new PieEntry(20f, topMovies.get(2).getOriginalTitle()));
        entries.add(new PieEntry(20f, topMovies.get(3).getOriginalTitle()));
        entries.add(new PieEntry(20f, topMovies.get(4).getOriginalTitle()));
        PieDataSet dataSet = new PieDataSet(entries, "Movie Pie Chart");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(22f);
        dataSet.setValueTextColor(Color.BLACK);

        pieChart.setUsePercentValues(true);
        pieChart.setHoleRadius(30f);
        //pieChart.setTransparentCircleRadius(65f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(22f);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();

    }
}
