package com.example.cinewatch20.viewModels;

import android.graphics.Movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.cinewatch20.models.MovieModel;
import com.example.cinewatch20.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {


    private MovieRepository movieRepository;



    public MovieListViewModel() {
        movieRepository = MovieRepository.getInstance();
    } //end con

    public LiveData<List<MovieModel>> getMovies() {
        return movieRepository.getMovies();
    } //end getter


    public void searchMovieApi(String query, int pageNumber) {
        movieRepository.searchMovieApi(query, pageNumber);
    }

} //end clas
