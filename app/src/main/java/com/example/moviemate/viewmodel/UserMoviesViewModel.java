package com.example.moviemate.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.moviemate.entity.UserMovies;
import com.example.moviemate.repository.UserMoviesRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserMoviesViewModel extends AndroidViewModel {

    private UserMoviesRepository userMoviesRepository;
    private LiveData<List<UserMovies>> allUserMovies;
    public UserMoviesViewModel (Application application) {
        super(application);
        userMoviesRepository = new UserMoviesRepository(application);
        allUserMovies = userMoviesRepository.getAllCustomers();
    }

    public CompletableFuture<UserMovies> findByIDFuture(final String email)
    {
        return userMoviesRepository.findByEmailFuture(email);
    }
    public LiveData<List<UserMovies>> getAllUserMovies() {
        return allUserMovies;
    }

    public void insert(UserMovies userMovies) {
        userMoviesRepository.insert(userMovies);
    }

    public void deleteAll() {
        userMoviesRepository.deleteAll();
    }

    public void update(UserMovies userMovies) {
        userMoviesRepository.updateCustomer(userMovies);
    }

}
