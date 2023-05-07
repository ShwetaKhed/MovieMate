package com.example.moviemate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class WishlistFragment extends Fragment {

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.context = container.getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.booking_fragment, container, false);
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

    }
}
