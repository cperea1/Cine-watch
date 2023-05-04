package com.example.cinewatch20.repositories;

import androidx.lifecycle.LiveData;

import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.request.MovieApiClient;

import java.util.List;

public class MovieRepository {
    private static MovieRepository instance;


    private MovieApiClient movieApiClient;

    public static MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        } //end if

        return instance;
    } //end getInstance

    private MovieRepository() {
        movieApiClient = MovieApiClient.getInstance();
    } //end constructor

    public LiveData<List<MovieItem>> getMovies() {
        return movieApiClient.getMovies();
    } //end getter


    public void searchMovieApi(String query, int pageNumber) {
        movieApiClient.searchMoviesApi(query, pageNumber);
    }

    public void getPopular() {
        movieApiClient.getPopular();
    }

    public void setmMovies(List<MovieItem> mMovies) {
        movieApiClient.setmMovies(mMovies);
    }

    public void searchByID(int id) {
        movieApiClient.searchByID(id);
    }
} //end class
