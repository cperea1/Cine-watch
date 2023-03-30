package com.example.cinewatch20.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinewatch20.R;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //Widgets
    TextView title, releaseDate, duration;
    ImageView movie_img;
    RatingBar rating_bar;


    //Click Listener
    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);

        title = itemView.findViewById(R.id.movie_title);
        releaseDate = itemView.findViewById(R.id.movie_release_date);
        duration = itemView.findViewById(R.id.movie_duration);
        movie_img = itemView.findViewById(R.id.movie_img);
        rating_bar = itemView.findViewById(R.id.rating_bar);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
