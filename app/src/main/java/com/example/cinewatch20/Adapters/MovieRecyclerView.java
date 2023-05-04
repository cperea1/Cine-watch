package com.example.cinewatch20.Adapters;

//import android.util.Log;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinewatch20.R;
import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.utils.Credentials;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieItem> mMovies;
    private final OnMovieListener onMovieListener;

    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Log.v("Here", "we made it here");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,
                parent, false);

        // MovieViewHolder holder = new MovieViewHolder(view, onMovieListener );

        return new MovieViewHolder(view, onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MovieViewHolder)holder).title.setText(mMovies.get(position).getTitle());
        //((MovieViewHolder)holder).overview.setText(mMovies.get(position).getOverview());
        //((MovieViewHolder)holder).duration.setText(mMovies.get(position).getRuntime() + "");

        //rating bar
        //((MovieViewHolder)holder).rating_bar.setRating(mMovies.get(position).getVote_average() / 2);


        //Image View
        Glide.with(holder.itemView.getContext())
                .load(Credentials.TMDB_POSTER_PATH + mMovies.get(position).getPoster_path())
                .into(((MovieViewHolder) holder).movie_img);

        Log.v("movie poster:", Credentials.TMDB_POSTER_PATH + mMovies.get(position).getPoster_path());


    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        } //end if

        return 0;
    }


    public void setmMovies(List<MovieItem> mMovies) {
        this.mMovies = mMovies;

    }

    public List<MovieItem> getmMovies() {
        return mMovies;
    }
} //end class
