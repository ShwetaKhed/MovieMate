package com.example.moviemate;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemate.databinding.HomeFragmentBinding;


public class HomeActivity extends AppCompatActivity {

    private HomeFragmentBinding binding;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeFragmentBinding.inflate(getLayoutInflater());
        setContentView(R.layout.home_fragment);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);


    }

}
