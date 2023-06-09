package com.example.moviemate.worker;
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.moviemate.dao.UserMoviesDao;
import com.example.moviemate.database.UserMoviesDatabase;
import com.example.moviemate.entity.UserMovies;
import com.example.moviemate.viewmodel.UserMoviesViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;
import com.google.common.reflect.TypeToken;
public class PeriodicWorker extends Worker {

    private static final String TAG = PeriodicWorker.class.getSimpleName();
    public static final String MOVIE_WISHLIST_KEY = "MOVIE_WISHLIST";
    public static final String USER_EMAIL_KEY = "USER_EMAIL";
    public static final String PERIODIC_WORKER_TAG = "PERIODIC_WORKER_TAG";

    public static boolean isDataSaveOnFireBase;
    public PeriodicWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        try {
            Data data =  getInputData();
            String userMovieInfo = data.getString(MOVIE_WISHLIST_KEY);

            Gson gson = new Gson();
            Type type = new TypeToken<List<UserMovies>>(){}.getType();
            List<UserMovies> userMovies =  gson.fromJson(userMovieInfo, type);
            String email =  data.getString(USER_EMAIL_KEY);
            //Send the movies list
            saveMoviesWishlistOnFirebase(email, userMovies);

            isDataSaveOnFireBase = true;
            // Indicate whether the work finished successfully with the Result
            return Result.success();

        }catch (Throwable throwable)
        {
            Log.e(TAG, "Error: ", throwable);
            return Result.failure();
        }
    }

    UserMoviesDatabase moviesDatabase;
    private void saveMoviesWishlistOnFirebase(String userEmail, List<UserMovies> userMovies)
    {
        //Get the data before @ in the email address and consider it as the username of the user
        String username =userEmail.split("@")[0];

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Movies");
        mDatabaseRef.child(username).setValue(userMovies).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Data data1 = new Data.Builder()
                    .putString("OUTPUT", "Task Finish Successfully")
                    .build();
            }
        });
    }
}

