package com.example.cinewatch.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinewatch.R;

public class PosterViewHolder extends RecyclerView.ViewHolder {
    public ImageView poster;
    public TextView movie_name;

    public PosterViewHolder(@NonNull View itemView) {
        super(itemView);
        poster = itemView.findViewById(R.id.poster);
        movie_name = itemView.findViewById(R.id.movie_name);
    }

}
