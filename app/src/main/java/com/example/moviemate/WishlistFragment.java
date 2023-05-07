package com.example.moviemate;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.databinding.LaunchScreenBinding;
import com.example.moviemate.databinding.WishlistFragmentBinding;
import com.example.moviemate.entity.UserMovies;
import com.example.moviemate.viewmodel.UserMoviesViewModel;
import com.example.moviemate.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {

    private WishlistFragmentBinding binding;
    Context context;
    private List<UserMovies> userSelectedMovies = new ArrayList<UserMovies>();
    private UserMoviesViewModel userMoviesViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        userMoviesViewModel = new ViewModelProvider(requireActivity()).get(UserMoviesViewModel.class);

        userMoviesViewModel.getAllUserMovies().observe(this, userMovies -> {
            this.userSelectedMovies = userMovies;
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = WishlistFragmentBinding.inflate(inflater);
        View view = binding.getRoot();

        this.context = container.getContext();
        // Inflate the layout for this fragment

        return view;
    }


    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState)
    {

    }
}
