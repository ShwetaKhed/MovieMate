package com.example.moviemate.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.moviemate.R;
import com.example.moviemate.databinding.ActivityMainBinding;
import com.example.moviemate.databinding.FragmentReportBinding;
import com.example.moviemate.viewmodel.SharedViewModel;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.github.mikephil.charting.components.Description;


public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;
    final String CHART_URL = "";
    public ReportFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        SharedViewModel model = new
                ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //binding.textMessage.setText(s);

            }
        });

        List<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0, 6766));
        barEntries.add(new BarEntry(1, 4444));
        barEntries.add(new BarEntry(2, 2222));
        barEntries.add(new BarEntry(3, 5555));
        barEntries.add(new BarEntry(4, 1111));
        barEntries.add(new BarEntry(5, 3656));
        barEntries.add(new BarEntry(6, 3435));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Steps");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        List<String> xAxisValues = new ArrayList<>(Arrays.asList("Sun", "Mon", "Tues","Wed", "Thurs", "Fri","Sat"));
//        binding.barChart.getXAxis().setValueFormatter(new
//                com.github.mikephil.charting.formatter.IndexAxisValueFormatter(xAxisValues));
//        BarData barData = new BarData(barDataSet);
//        binding.barChart.setData(barData);
//        barData.setBarWidth(1.0f);
//        binding.barChart.setVisibility(View.VISIBLE);
//        binding.barChart.animateY(4000);
//        //description will be displayed as "Description Label" if not provided
//        Description description = new Description();
//        description.setText("Daily Steps");
//        binding.barChart.setDescription(description);
//        //refresh the chart
//        binding.barChart.invalidate();
        return view;
    }
}
