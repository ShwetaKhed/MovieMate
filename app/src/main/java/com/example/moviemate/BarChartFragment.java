package com.example.moviemate;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.moviemate.R;
import com.example.moviemate.databinding.ActivityMainBinding;
import com.example.moviemate.databinding.FragmentBarReportBinding;
import com.github.mikephil.charting.components.Description;

import com.example.moviemate.model.Movie;
import com.example.moviemate.model.MovieResult;
import com.example.moviemate.service.RetrofitClient;
import com.example.moviemate.viewmodel.SharedViewModel;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

/*import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;*/


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

    boolean showReport = false;

    private boolean isStartDateChanged;
    private boolean isEndDateChanged;

    public BarChartFragment(){}

    BarChart barChart;

    EditText startDate ;
    EditText endDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.context = container.getContext();

        binding = FragmentBarReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        barChart = view.findViewById(R.id.barChart);
        startDate = view.findViewById(R.id.startDate);
        endDate = view.findViewById(R.id.endDate);

        System.out.println("Bar chart activity " + this.getActivity());
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
/*                        if (finalMovieList.size() == 1000)
                        {
                            try {
                                createBarChart(view);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }*/
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

        bm1.setShowLabel(true);
        bm2.setShowLabel(true);
        bm3.setShowLabel(true);
        bm4.setShowLabel(true);
        bm5.setShowLabel(true);
        bm1.setLegendLabel(topMovies.get(0).getOriginalTitle());
        bm2.setLegendLabel(topMovies.get(1).getOriginalTitle());
        bm3.setLegendLabel(topMovies.get(2).getOriginalTitle());
        bm4.setLegendLabel(topMovies.get(3).getOriginalTitle());
        bm5.setLegendLabel(topMovies.get(4).getOriginalTitle());

        /*barChart.addBar(new BarModel(topMovies.get(0).getOriginalTitle(),new BigDecimal(topMovies.get(0).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(), 0xFF123456));
        barChart.addBar(new BarModel(topMovies.get(1).getOriginalTitle(),new BigDecimal(topMovies.get(1).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(),  0xFF343456));
        barChart.addBar(new BarModel(topMovies.get(2).getOriginalTitle(),new BigDecimal(topMovies.get(2).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(), 0xFF563456));
        barChart.addBar(new BarModel(topMovies.get(3).getOriginalTitle(),new BigDecimal(topMovies.get(3).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(), 0xFF873F56));
        barChart.addBar(new BarModel(topMovies.get(4).getOriginalTitle(),new BigDecimal(topMovies.get(4).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue(), 0xFF56B7F1));
*/

        barChart.startAnimation();



        /*List<BarEntry> movieEntries = new ArrayList<>();
        float pop1 = new BigDecimal(topMovies.get(0).getPopularity()).divide(totalPopularity, 2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue();
        movieEntries.add(new BarEntry(0, pop1));
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
        binding.barChart.invalidate();*/
    }

}
