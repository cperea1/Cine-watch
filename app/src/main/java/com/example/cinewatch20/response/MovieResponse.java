package com.example.cinewatch20.response;

import com.example.cinewatch20.data.MovieItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//This class is for single movie requests
public class MovieResponse {
    //1 Finding the movie object
    @SerializedName("results")
    @Expose
    private MovieItem movie;

    public MovieItem getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
