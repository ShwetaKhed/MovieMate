package com.example.moviemate.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.moviemate.dao.UserMoviesDao;
import com.example.moviemate.database.UserMoviesDatabase;
import com.example.moviemate.entity.UserMovies;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class UserMoviesRepository {
    private UserMoviesDao userMoviesDao;
    private LiveData<List<UserMovies>> allUserMovies;
    public UserMoviesRepository(Application application){
        UserMoviesDatabase db = UserMoviesDatabase.getInstance(application);
        userMoviesDao = db.userMoviesDao();
        allUserMovies = userMoviesDao.getAll();
    }

    public LiveData<List<UserMovies>> getAllCustomers() {
        return allUserMovies;
    }
    public void insert(final UserMovies userMovies){ UserMoviesDatabase.databaseWriteExecutor.execute(new Runnable() {
        @Override
        public void run() { userMoviesDao.insert(userMovies);
        } });
    }
    public void deleteAll(){ UserMoviesDatabase.databaseWriteExecutor.execute(new Runnable() {
        @Override
        public void run() { userMoviesDao.deleteAll();
        } });
    }
    public void delete(final UserMovies userMovies){
        UserMoviesDatabase.databaseWriteExecutor.execute(new Runnable() { @Override
        public void run() { userMoviesDao.delete(userMovies);
        } });
    }

    public void updateCustomer(final UserMovies userMovies){ UserMoviesDatabase.databaseWriteExecutor.execute(new Runnable() {
        @Override
        public void run() { userMoviesDao.updateCustomer(userMovies);
        } });
    }
    public CompletableFuture<UserMovies> findByEmailFuture(final String email) {
        return CompletableFuture.supplyAsync(new Supplier<UserMovies>() { @Override
        public UserMovies get() {
            return userMoviesDao.findByUserEmail(email);
        }
        }, UserMoviesDatabase.databaseWriteExecutor);
    }
}
