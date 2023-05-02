package com.example.moviemate.fragment;
import com.example.moviemate.DatePicker;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.R;
import com.example.moviemate.User;
import com.example.moviemate.databinding.InfoFragmentBinding;
import com.example.moviemate.viewmodel.SharedViewModel;
import com.example.moviemate.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import java.text.DateFormat;
import java.util.Calendar;
import android.app.DatePickerDialog.OnDateSetListener;

public class InfoFragment extends Fragment {
    private DatabaseReference mDatabaseRef;
    private InfoFragmentBinding binding;

    public InfoFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Inflate the View for this fragment using the binding
        binding = InfoFragmentBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        System.out.println(requireActivity());
        UserViewModel model = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        model.getLoginEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               System.out.println("email" + s);
               readDataFromFirebase(s);
            }
        });

        model.getDateOfBirth().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                System.out.println("date of birth" + s);
                binding.editDob.setText(s);
            }
        });


        //Add genre spinner
        createGenreSpinner();
        createTheaterSpinner();
        //DatePickerDialog
        binding.editDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on edit date of birth");
                pickDateOfBirth();
            }
        });
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String newName=binding.editPersonName.getText().toString();
                String newDob=binding.editDob.getText().toString();
                String genrePreference = binding.spinnerGenre.getSelectedItem().toString();
                String theaterPreference = binding.spinnerTheater.getSelectedItem().toString();

                String email = model.getLoginEmail().getValue();
                if(email == null || email.isEmpty())
                {
                    Toast.makeText(getContext() , "User is not logged In", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Get the data before @ in the email address and consider it as the username of the user
                String username = email.split("@")[0];

                User user = new User(email, newName, newDob, genrePreference, theaterPreference );

                mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
                mDatabaseRef.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext() , "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            } });

        // Initialize the listener


        return view;
    }


    private void readDataFromFirebase(String email)
    {
        System.out.println("User login email id is : " + email);
        if(email == null)
            return;

        //Get the data before @ in the email address and consider it as the username of the user
        String username = email.split("@")[0];

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mDatabaseRef.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        //update the fields here
                        Toast.makeText(getContext(), "Successfully Read", Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String email = String.valueOf(dataSnapshot.child("email").getValue());
                        String name = String.valueOf(dataSnapshot.child("name").getValue());
                        System.out.println("email : " + email + " and name" + name);
                        binding.editPersonName.setText(name);
                    }
                   else {
                        //data for this user didnot exist
                        //show empty blanks here
                    }
                    //
                }
                else {
                   //error occurs while fetching this data
                    Toast.makeText(getContext(), "Failed to read the data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //get the username.
        //check for username is not empt

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

    private void pickDateOfBirth()
    {
        //DialogFragment
        DatePicker mDatePickerDialogFragment = new DatePicker();
        mDatePickerDialogFragment.show(getChildFragmentManager(), DatePicker.TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}