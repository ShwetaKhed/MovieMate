package com.example.moviemate;

import android.content.Context;
import android.os.Bundle;
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

import com.example.moviemate.databinding.LaunchScreenBinding;
import com.example.moviemate.databinding.WishlistFragmentBinding;
import com.example.moviemate.entity.UserMovies;
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

        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.getLoginEmail().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                System.out.println("email" + s);
                readMoviesDataFromFirebase(s);
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState)
    {

    }

    private void getMoviesWishlist()
    {
        //List<UserMovies> movies = new List<UserMovies>();
    }
    private  void readMoviesDataFromFirebase(String email)
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
                        //TODO: Do Your stuff here
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
