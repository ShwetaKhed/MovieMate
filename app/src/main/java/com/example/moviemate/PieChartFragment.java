package com.example.moviemate;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.databinding.FragmentPieChartBinding;
import com.example.moviemate.model.Movie;
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.service.RetrofitClient;
import com.example.moviemate.viewmodel.SharedViewModel;



import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    PieChart pieChart;
    EditText startDate ;
    EditText endDate;



    public PieChartFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext();

        binding = FragmentPieChartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        pieChart = view.findViewById(R.id.piechart);
        startDate = view.findViewById(R.id.startDate);
        endDate = view.findViewById(R.id.endDate);
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
                        finalMovieList.add(movie);

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

                    SimpleDateFormat originalDateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
                    Date originalDate = null;
                    try {
                        originalDate = originalDateFormat.parse(s);
                        SimpleDateFormat desiredDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        String desiredDateString = desiredDateFormat.format(originalDate);

                        System.out.println(desiredDateString);
                        binding.startDate.setText(desiredDateString);
                        startDate.setError(null);

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
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
                    SimpleDateFormat originalDateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
                    Date originalDate = null;
                    try {
                        originalDate = originalDateFormat.parse(s);
                        SimpleDateFormat desiredDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        String desiredDateString = desiredDateFormat.format(originalDate);

                        System.out.println(desiredDateString);
                        binding.endDate.setText(desiredDateString);
                        endDate.setError(null);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        Button button = (Button) view.findViewById(R.id.viewChartButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    createPieChart(pieChart);


                } catch (ParseException e) {
                    throw new RuntimeException(e);
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

    public void createPieChart(PieChart pieChart) throws ParseException {
        String startDt = String.valueOf(binding.startDate.getText());
        String endDt = String.valueOf(binding.endDate.getText());
        if (startDt.isEmpty())
        {
            startDate.setError("Required");
            return;
        }
        if (endDt.isEmpty())
        {
            endDate.setError("Required");
            return;
        }
        ArrayList<Movie> topMovies = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < finalMovieList.size(); i++)
        {
            int count = 0;
            Date startDate = dateFormat.parse(startDt);
            Date endDate = dateFormat.parse(endDt);
            Date releaseDate = dateFormat.parse(finalMovieList.get(i).getReleaseDate());
            if (releaseDate.after(startDate) && releaseDate.before(endDate)) {
                    topMovies.add(finalMovieList.get(i));
            }
        }
        if (topMovies.isEmpty()) {
            binding.invalidText.setText("Select different dates and retry");
           return;
        }

        BigDecimal totalPopularity = new BigDecimal(0.00);
        for (int index = 0; index < 5; index ++)
        {
          totalPopularity = totalPopularity.add(new BigDecimal(topMovies.get(index).getPopularity()));
        }
        Log.d("totalPopularity", String.valueOf(totalPopularity));


        pieChart.clearChart();
        pieChart.addPieSlice(
                new PieModel(
                        topMovies.get(0).getOriginalTitle(),
                        new BigDecimal(topMovies.get(0).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(),
                        Color.parseColor("#FFA726")));

        pieChart.addPieSlice(
                new PieModel(
                        topMovies.get(1).getOriginalTitle(),
                        new BigDecimal(topMovies.get(1).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(),
                        Color.parseColor("#66BB6A")));
        //label2.setText(topMovies.get(1).getOriginalTitle());
        pieChart.addPieSlice(
                new PieModel(
                        topMovies.get(2).getOriginalTitle(),
                        new BigDecimal(topMovies.get(2).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(),
                        Color.parseColor("#EF5350")));
        //label3.setText(topMovies.get(2).getOriginalTitle());
        pieChart.addPieSlice(
                new PieModel(
                        topMovies.get(3).getOriginalTitle(),
                        new BigDecimal(topMovies.get(3).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(),
                        Color.parseColor("#29B6F6")));
        //label4.setText(topMovies.get(3).getOriginalTitle());
        pieChart.addPieSlice(
                new PieModel(
                        topMovies.get(4).getOriginalTitle(),
                        new BigDecimal(topMovies.get(4).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(),
                        Color.parseColor("#d885ed")));
        //label5.setText(topMovies.get(4).getOriginalTitle());
        pieChart.startAnimation();



    }
}
