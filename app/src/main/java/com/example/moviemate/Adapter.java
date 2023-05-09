package com.example.moviemate;

import static android.content.ContentValues.TAG;
import static androidx.core.content.ContextCompat.startActivity;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.moviemate.databinding.BookingFragmentBinding;
import com.example.moviemate.model.Movie;
import com.example.moviemate.model.MovieResult;

import java.util.ArrayList;



// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ArrayList movieTitle = new ArrayList<>();
    ArrayList movieImage = new ArrayList<>();
    ArrayList movieOverview = new ArrayList<>();
    ArrayList movieAdult = new ArrayList<>();
    ArrayList movieReleaseDate= new ArrayList<>();

    ArrayList<Movie> movieList = new ArrayList<>();
    public static Context context;
    public String userEmail;



    public Adapter(Context context, ArrayList<Movie> movielist, String userEmail) {
        this.context = context;
        this.movieList = movielist;
        this.userEmail = userEmail;
        for (int i = 0; i < movielist.size(); i ++) {
            this.movieTitle.add(movielist.get(i).getOriginalTitle());
            this.movieImage.add(movielist.get(i).getPosterPath());
            this.movieOverview.add(movielist.get(i).getOverview());
            this.movieAdult.add(movielist.get(i).getAdult());
            this.movieReleaseDate.add(movielist.get(i).getReleaseDate());
        }


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the Layout(Instantiates list_item.xml
        // layout file into View object)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        // Passing view to ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Binding data to the into specified position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TypeCast Object to int type
       // int res = (int) courseImg.get(position);
        Glide.with(holder.imageView.getContext()).load(movieImage.get(position)).into(holder.imageView);
        holder.text.setText((String) movieTitle.get(position));
    }

    @Override
    public int getItemCount() {

        return movieTitle.size();
    }

    // Initializing the Views
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.movieTitle);
            imageView = (ImageView) view.findViewById(R.id.ivMovie);

            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION){
                        Movie selectedMovie =  movieList.get(position);
                        Intent i = new Intent(context, MovieContentActivity.class);
                        i.putExtra("movie", selectedMovie);
                        i.putExtra("userEmail", userEmail);
                        context.startActivity(i);
                    }
                }
            });
        }
    }
}