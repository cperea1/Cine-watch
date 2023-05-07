package com.example.cinewatch20.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.cinewatch20.Activities.Swipe;
import com.example.cinewatch20.R;
import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.utils.Credentials;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<MovieItem> {

    private Context context;
    private List<MovieItem> mMovies;

    private final OnMovieListener onMovieListener;

    public MovieAdapter(@NonNull Context context, int resource, List<MovieItem> mMovies, OnMovieListener onMovieListener) {
        super(context, resource, mMovies);
        this.mMovies = mMovies;
        this.onMovieListener = onMovieListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieViewHolder holder;


        // Inflate the view if necessary
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
            holder = new MovieViewHolder(convertView, onMovieListener);
            convertView.setTag(holder);
        } //end if
        else {
            holder = (MovieViewHolder) convertView.getTag();
        } //end else

        // Get the name at the current position
        MovieItem movieItem = getItem(position);
        holder.title.setText(mMovies.get(position).getTitle());

        Glide.with(holder.itemView.getContext())
                .load(Credentials.TMDB_POSTER_PATH + mMovies.get(position).getPoster_path())
                .into((holder).movie_img);

        Log.v("movie poster:", Credentials.TMDB_POSTER_PATH + mMovies.get(position).getPoster_path());



        return convertView;
    }

    public void setmMovies(List<MovieItem> mMovies) {
        this.mMovies = mMovies;
        //notifyDataSetChanged();
    }
}

