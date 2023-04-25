package com.example.moviemate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.databinding.InfoFragmentBinding;
import com.example.moviemate.viewmodel.SharedViewModel;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {
    private InfoFragmentBinding binding;
    public InfoFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Inflate the View for this fragment using the binding
        binding = InfoFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        SharedViewModel model = new
                ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        model.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //binding.textMessage.setText(s);

            }
        });

        //Add genre spinner
        createGenreSpinner();
        createTheaterSpinner();

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newName=binding.editPersonName.getText().toString();
                String newDob=binding.editDob.getText().toString();
                String genrePreference = binding.spinnerGenre.getSelectedItem().toString();
                String theaterPreference = binding.spinnerTheater.getSelectedItem().toString();

            } });
        return view;
    }

    private void createGenreSpinner()
    {
        Spinner genreSpinner =binding.spinnerGenre;
        List<String> list = new ArrayList<String>();
        list.add("Comedy");
        list.add("Horror");
        list.add("Action");
        list.add("Thriller");
        list.add("Adventure");
        list.add("Science fiction");
        final ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, list);
        genreSpinner.setAdapter(genreAdapter);
        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void createTheaterSpinner()
    {
        Spinner genreSpinner =binding.spinnerTheater;
        List<String> list = new ArrayList<String>();
        list.add("Henkel Street Cinema");
        list.add("Mystery Radio Theatre");
        list.add("HOYTS Melbourne Central");
        list.add("Palace Cinema Como");

        final ArrayAdapter<String> theaterAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, list);
        genreSpinner.setAdapter(theaterAdapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}