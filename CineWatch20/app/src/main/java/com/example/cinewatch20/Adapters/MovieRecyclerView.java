package com.example.cinewatch20.Adapters;

//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinewatch20.R;
import com.example.cinewatch20.models.MovieModel;
import com.example.cinewatch20.utils.Credentials;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;

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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ((MovieViewHolder)holder).title.setText(mMovies.get(i).getTitle());
        //((MovieViewHolder)holder).overview.setText(mMovies.get(i).getOverview());
        //((MovieViewHolder)holder).duration.setText(mMovies.get(i).getRuntime() + "");

        //rating bar
        //((MovieViewHolder)holder).rating_bar.setRating(mMovies.get(i).getVote_average() / 2);


        //Image View
        Glide.with(holder.itemView.getContext())
                .load(Credentials.TMDB_POSTER_PATH + mMovies.get(i).getImageUrl())
                .into(((MovieViewHolder) holder).movie_img);

    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        } //end if

        return 0;
    }


    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;

    }

} //end class
