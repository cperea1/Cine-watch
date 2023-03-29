package com.example.cinewatch20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cinewatch20.Adapters.MovieRecyclerView;
import com.example.cinewatch20.Adapters.OnMovieListener;
import com.example.cinewatch20.models.MovieModel;
import com.example.cinewatch20.request.Service;
import com.example.cinewatch20.response.MovieSearchResponse;
import com.example.cinewatch20.utils.Credentials;
import com.example.cinewatch20.utils.MovieApi;
import com.example.cinewatch20.viewModels.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    //Recycler View
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;

    //view model
    private MovieListViewModel movieListViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);


        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);


        ConfigureRecyclerView();
        observingAnyChange();
        searchMovieApi("fast", 1);




    } //end onCreate

    private void observingAnyChange() {
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null) {
                    for (MovieModel movieModel: movieModels) {
                        Log.v("Tag", "onChanged: " + movieModel.getTitle());

                        movieRecyclerViewAdapter.setmMovies(movieModels);
                    }
                } //end if
            } //end onChanged
        });
    } //end OAC

    private void searchMovieApi(String query, int pageNumber) {
        movieListViewModel.searchMovieApi(query, pageNumber);
    }


    private void ConfigureRecyclerView() {
        //Live Data cannot be passed via the constructor
        movieRecyclerViewAdapter = new MovieRecyclerView(this);

        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onMovieClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }


//
} //end class