package com.example.moviemate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.example.moviemate.databinding.BookingFragmentBinding;


import com.example.moviemate.viewmodel.SharedViewModel;

public class BookingFragment extends Fragment {
    private SharedViewModel model;
    private BookingFragmentBinding bookingBinding;
    public BookingFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        bookingBinding = BookingFragmentBinding.inflate(inflater, container, false);
        View view = bookingBinding.getRoot();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bookingBinding = null;
    }
}
