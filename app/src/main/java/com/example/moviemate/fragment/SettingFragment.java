package com.example.moviemate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.moviemate.databinding.HomeFragmentBinding;
import com.example.moviemate.databinding.SettingsFragmentBinding;
import com.example.moviemate.viewmodel.SharedViewModel;

public class SettingFragment extends Fragment {
    private SharedViewModel model;
    private SettingsFragmentBinding addBinding;
    public SettingFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = SettingsFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}
