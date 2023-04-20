package com.example.cinewatch20.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.cinewatch20.R;
import com.example.cinewatch20.models.MovieModel;
import com.example.cinewatch20.utils.Credentials;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<MovieModel> {

    private Context context;
    private List<MovieModel> mMovies;

    private OnMovieListener onMovieListener;

    public MovieAdapter(@NonNull Context context, int resource, List<MovieModel> mMovies, OnMovieListener onMovieListener) {
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
        MovieModel movieModel = getItem(position);

        // Set the name in the TextView
        //holder.rating_bar.setRating(mMovies.get(position).getVote_average());
        //holder.duration.setText(mMovies.get(position).getRuntime() + "");
        //holder.overview.setText(mMovies.get(position).getOverview());
        holder.title.setText(mMovies.get(position).getTitle());

        Glide.with(holder.itemView.getContext())
                .load(Credentials.TMDB_POSTER_PATH + mMovies.get(position).getImageUrl())
                .into((holder).movie_img);




        return convertView;
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }
}

