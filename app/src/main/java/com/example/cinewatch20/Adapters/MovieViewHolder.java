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
    TextView title, overview, duration;
    ImageView movie_img;
    //RatingBar rating_bar;


    //Click Listener
    OnMovieListener onMovieListener;

    public MovieViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;

        title = itemView.findViewById(R.id.movie_title);
        //overview = itemView.findViewById(R.id.overview);

        movie_img = itemView.findViewById(R.id.movie_img);


        itemView.setOnClickListener(this);
    } //end constructor

    @Override
    public void onClick(View view) {

        onMovieListener.onMovieClick(getAdapterPosition());
    }
}
