package com.example.moviemate.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.moviemate.entity.UserMovies;

import java.util.List;

@Dao
public interface UserMoviesDao {

    @Query("SELECT * FROM UserMovies ORDER BY user_email ASC")
    LiveData<List<UserMovies>> getAll();

    @Query("SELECT * FROM UserMovies WHERE user_email = :email LIMIT 1")
    UserMovies findByUserEmail(String email);

    @Insert
    void insert(UserMovies userMovies );
    @Delete
    void delete(UserMovies userMovies);
    @Update
    void updateCustomer(UserMovies userMovies);
    @Query("DELETE FROM UserMovies")
    void deleteAll();
}
