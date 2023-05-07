package com.example.moviemate.fragment;
import com.example.moviemate.DatePicker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviemate.model.User;
import com.example.moviemate.databinding.InfoFragmentBinding;
import com.example.moviemate.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

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
        binding.editDob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    System.out.println("Clicked on edit date of birth");
                    pickDateOfBirth();
                    // Do what you want
                    return true;
                }
                return false;
            }
        });

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String firstName=binding.editPersonName.getText().toString();
                String lastName=binding.editLastName.getText().toString();
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

                if(firstName == null ||firstName.isEmpty())
                {
                    Toast.makeText(getContext() , "Please enter your first name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(lastName == null ||lastName.isEmpty())
                {
                    Toast.makeText(getContext() , "Please enter your last name", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(newDob == null ||newDob.isEmpty())
                {
                    Toast.makeText(getContext() , "Please enter your date of birth", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(genrePreference == null ||genrePreference.isEmpty() || genrePreference.equals("Select Genre Preference"))
                {
                    Toast.makeText(getContext() , "Please enter your genre Preference", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(theaterPreference == null ||theaterPreference.isEmpty() || theaterPreference.equals("Select Theater Preference"))
                {
                    Toast.makeText(getContext() , "Please enter your genre Preference", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(email, firstName, lastName, newDob, genrePreference, theaterPreference );

                mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
                mDatabaseRef.child(username).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext() , "Successfully Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            } });

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
                        String firstname = String.valueOf(dataSnapshot.child("firstName").getValue());
                        String lastname = String.valueOf(dataSnapshot.child("lastName").getValue());
                        String dob = String.valueOf(dataSnapshot.child("dob").getValue());
                        String genrePreference = String.valueOf(dataSnapshot.child("genrePreference").getValue());
                        String theaterPreference = String.valueOf(dataSnapshot.child("theaterPreference").getValue());
                        System.out.println("email : " + email + " and name" + firstname);
                        binding.editPersonName.setText(firstname);
                        binding.editLastName.setText(lastname);
                        binding.editDob.setText(dob);
                        setSpinnerValue(binding.spinnerGenre, genrePreference);
                        setSpinnerValue(binding.spinnerTheater, theaterPreference);
                    }
                   else {
                        //data for this user didn't exist
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
        list.add("Select Genre Preference");
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
        list.add("Select Theater Preference");
        list.add("Henkel Street Cinema");
        list.add("Mystery Radio Theatre");
        list.add("HOYTS Melbourne Central");
        list.add("Palace Cinema Como");

        final ArrayAdapter<String> theaterAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, list);
        genreSpinner.setAdapter(theaterAdapter);
    }

    private void setSpinnerValue(Spinner spinner, String value)
    {
        ArrayAdapter myAdapter = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdapter.getPosition(value);
        //set the default according to the value
        spinner.setSelection(spinnerPosition);
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