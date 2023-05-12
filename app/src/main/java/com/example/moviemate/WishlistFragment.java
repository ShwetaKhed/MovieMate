package com.example.moviemate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviemate.databinding.LaunchScreenBinding;
import com.example.moviemate.databinding.WishlistFragmentBinding;
import com.example.moviemate.entity.UserMovies;
import com.example.moviemate.model.Movie;
import com.example.moviemate.viewmodel.UserMoviesViewModel;
import com.example.moviemate.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {
    private DatabaseReference mDatabaseRef;
    private WishlistFragmentBinding binding;
    Context context;
    private List<UserMovies> userSelectedMovies = new ArrayList<UserMovies>();

    private List<String> movieNames = new ArrayList<>();
    private UserMoviesViewModel userMoviesViewModel;

    private String userEmail;

    View fragmentView;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        userMoviesViewModel = new ViewModelProvider(requireActivity()).get(UserMoviesViewModel.class);
        userMoviesViewModel.getAllUserMovies().observe(this, userMovies -> {
            for (UserMovies movie1: userMovies
            ) {
                if (!this.movieNames.contains(movie1.originalTitle)) {
                    Log.d(movie1.originalTitle, movie1.overview);
                    Log.d("User email", movie1.getUserEmail());
                    if (movie1.getUserEmail().equals(userEmail)) {
                        UserMovies movie = new UserMovies();
                        movie.setOriginalTitle(movie1.originalTitle);
                        movie.setPosterPath(movie1.posterPath);
                        this.movieNames.add(movie1.originalTitle);
                        this.userSelectedMovies.add(movie);
                    }
                    WishlistAdapter itemAdapter = new WishlistAdapter(getContext(),WishlistFragment.this.userSelectedMovies);
                    RecyclerView recyclerView
                            = fragmentView.findViewById(R.id.recycleView);
                    recyclerView.setLayoutManager(
                            new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(itemAdapter);
                }

            }
        } );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = WishlistFragmentBinding.inflate(inflater);
        View view = binding.getRoot();

        this.context = container.getContext();
        // Inflate the layout for this fragment



        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getLoginEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                System.out.println("email" + s);
                WishlistFragment.this.userEmail = s;
                readMoviesDataFromFirebase(view, s);
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState)
    {
        Log.d("Tag", String.valueOf(this.userSelectedMovies.size()));
        //TODO: Do Your stuff here
        fragmentView = view;


    }


    private  void readMoviesDataFromFirebase(View view, String email)
    {
        System.out.println("User login email id is : " + email);
        if(email == null)
            return ;

        List<UserMovies> movieList = new ArrayList<>();
        //Get the data before @ in the email address and consider it as the username of the user
        String username = email.split("@")[0];

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Movies");
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
                        for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                            UserMovies movie = movieSnapshot.getValue(UserMovies.class);
                            movieList.add(movie);
                        }
                        for (int i = 0; i < movieList.size(); i++) {
                            UserMovies movies = movieList.get(i);
                            System.out.println("Title of Movie : " + movies.originalTitle);
                        }


                      /*  WishlistAdapter itemAdapter = new WishlistAdapter(getContext(),movieList);
                        RecyclerView recyclerView
                                = view.findViewById(R.id.recycleView);
                        recyclerView.setLayoutManager(
                                new LinearLayoutManager(getContext()));

                        // adapter instance is set to the
                        // recyclerview to inflate the items.
                        recyclerView.setAdapter(itemAdapter);*/
                    }
                    else {
                        //data for this user didn't exist
                        //show empty blanks here
                        //TODO: NO Movie added in wishlist . Can show this message
                    }
                }
                else {
                    //error occurs while fetching this data
                    Toast.makeText(getContext(), "Failed to read the data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
