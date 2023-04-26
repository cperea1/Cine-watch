package com.example.cinewatch20.service;

import com.example.cinewatch20.data.MovieItem;

import java.util.List;


public interface
MovieDetailsCallback {

    void dbMovieDetails(List<MovieItem> movieItems);
}
