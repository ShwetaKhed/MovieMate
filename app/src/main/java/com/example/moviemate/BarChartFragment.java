package com.example.moviemate;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.moviemate.databinding.FragmentBarReportBinding;
import com.example.moviemate.model.Movie;
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.service.RetrofitClient;
import com.example.moviemate.viewmodel.SharedViewModel;
import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarChartFragment extends Fragment {
    private FragmentBarReportBinding binding;
    ArrayList<Movie> finalMovieList = new ArrayList<Movie>();
    Context context;

    private boolean isStartDateChanged;
    private boolean isEndDateChanged;

    public BarChartFragment(){}

    BarChart barChart;

    EditText startDate ;
    EditText endDate;

    TextView movieName1;
    TextView movieName2;
    TextView movieName3;
    TextView movieName4;
    TextView movieName5;
    TextView popularity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext();

        binding = FragmentBarReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        // Fetching list of fields on the bar chart screen
        barChart = view.findViewById(R.id.barChart);
        startDate = view.findViewById(R.id.startDate);
        endDate = view.findViewById(R.id.endDate);
        movieName1 = view.findViewById(R.id.movieName1);
        movieName2 = view.findViewById(R.id.movieName2);
        movieName3 = view.findViewById(R.id.movieName3);
        movieName4 = view.findViewById(R.id.movieName4);
        movieName5 = view.findViewById(R.id.movieName5);
        popularity = view.findViewById(R.id.popularity);

        System.out.println("Bar chart activity " + this.getActivity());
        // Callind the api around 50 times to fetch large amount of data that can be used to display bar chart
        for (int i = 450; i < 500; i++) {
            // sending the api key and page number to retrofit client
            Call<MovieResult> call = RetrofitClient.getInstance().getApi().
                    getPopularMovies1("6f6d1b438fddb937dd48a7f88b87eae7", String.valueOf(i));
            call.enqueue(new Callback<MovieResult>() {
                // getting the response and adding the movie list
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
                    // handing on failure
                }

            });
        }
        // start date dat picker
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

        // end date dat picker
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

        // end date setter method
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

        // end date setter method
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
                    createBarChart(barChart);


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


    public void createBarChart(BarChart barChart) throws ParseException {
        String startDt = String.valueOf(binding.startDate.getText());
        String endDt = String.valueOf(binding.endDate.getText());
        // validating start and end date is not null
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
        // adding top 5 movies
        for (int i = 0; i < finalMovieList.size(); i++)
        {
            Date startDate = dateFormat.parse(startDt);
            Date endDate = dateFormat.parse(endDt);
            Date releaseDate = dateFormat.parse(finalMovieList.get(i).getReleaseDate());
            if (releaseDate.after(startDate) && releaseDate.before(endDate)) {
                topMovies.add(finalMovieList.get(i));
            }
        }
        // checking if movies are empty
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

        // displaying the bar chart

        BarModel bm1 = new BarModel(topMovies.get(0).getOriginalTitle(),new BigDecimal(topMovies.get(0).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(), Color.parseColor("#FFA726"));
        BarModel bm2 = new BarModel(topMovies.get(1).getOriginalTitle(),new BigDecimal(topMovies.get(1).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(), Color.parseColor("#66BB6A"));
        BarModel bm3 = new BarModel(topMovies.get(2).getOriginalTitle(),new BigDecimal(topMovies.get(2).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(), Color.parseColor("#EF5350"));
        BarModel bm4 = new BarModel(topMovies.get(3).getOriginalTitle(),new BigDecimal(topMovies.get(3).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(), Color.parseColor("#29B6F6"));
        BarModel bm5 = new BarModel(topMovies.get(4).getOriginalTitle(),new BigDecimal(topMovies.get(4).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(), Color.parseColor("#d885ed"));

        barChart.addBar(bm1);
        barChart.addBar(bm2);
        barChart.addBar(bm3);
        barChart.addBar(bm4);
        barChart.addBar(bm5);

        bm1.setShowLabel(false);
        bm2.setShowLabel(false);
        bm3.setShowLabel(false);
        bm4.setShowLabel(false);
        bm5.setShowLabel(false);

        movieName1.setText(topMovies.get(0).getOriginalTitle());
        movieName2.setText(topMovies.get(1).getOriginalTitle());
        movieName3.setText(topMovies.get(2).getOriginalTitle());
        movieName4.setText(topMovies.get(3).getOriginalTitle());
        movieName5.setText(topMovies.get(4).getOriginalTitle());
        popularity.setText("Popularity Index (Out of 100)");

        barChart.startAnimation();

    }

}
