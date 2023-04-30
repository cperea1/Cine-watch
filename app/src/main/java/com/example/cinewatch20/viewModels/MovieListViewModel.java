package com.example.cinewatch20.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {


    private MovieRepository movieRepository;



    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    } //end con

    public LiveData<List<MovieItem>> getMovies() {
        return movieRepository.getMovies();
    } //end getter


    public void searchMovieApi(String query, int pageNumber) {
        movieRepository.searchMovieApi(query, pageNumber);
    }

    public void getPopular() {
        movieRepository.getPopular();
    }

    public void setmMovies(List<MovieItem> mMovies) {
        movieRepository.setmMovies(mMovies);
    }

    public void searchByID(int id) {
        movieRepository.searchByID(id);
    }
} //end class
