package com.example.moviemate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviemate.entity.UserMovies;
import com.example.moviemate.model.Movie;

import java.util.ArrayList;
import java.util.List;


// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    ArrayList movieTitle = new ArrayList<>();
    ArrayList movieImage = new ArrayList<>();


    List<UserMovies> movieList;
    public static Context context;



    public WishlistAdapter(Context context, List<UserMovies> movielist) {
        this.context = context;
        this.movieList = movielist;
        for (int i = 0; i < movielist.size(); i ++) {
            this.movieTitle.add(movielist.get(i).getOriginalTitle());
            this.movieImage.add(movielist.get(i).getPosterPath());

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

           /* view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION){
                        UserMovie selectedMovie =  movieList.get(position);
                        Intent i = new Intent(context, MovieContentActivity.class);
                        i.putExtra("movie", selectedMovie);
                        context.startActivity(i);
                    }
                }
            });*/
        }
    }
}