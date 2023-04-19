package com.example.cinewatch20.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cinewatch20.Adapters.MovieRecyclerView;
import com.example.cinewatch20.Adapters.OnMovieListener;
import com.example.cinewatch20.R;
import com.example.cinewatch20.models.MovieModel;
import com.example.cinewatch20.viewModels.MovieListViewModel;

import java.util.List;

public class MovieListActivity extends AppCompatActivity implements OnMovieListener {

    //Recycler View
    private RecyclerView recyclerView; //good
    private MovieRecyclerView movieRecyclerAdapter ;
    //private MovieViewHolder movieViewHolder;

    //view model
    private MovieListViewModel movieListViewModel;

    Button home;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);

        recyclerView = findViewById(R.id.recyclerView); //good

        home = findViewById(R.id.home_button);


        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform your action here

                Intent intent = new Intent(MovieListActivity.this, Swipe.class);
                startActivity(intent);

            } //end onclick
        });


        SetupSearchView();
        ConfigureRecyclerView();
        ObserveAnyChange();






    } //end onCreate

    private void SetupSearchView() {
        final SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        //The search string from searchView
                        query,
                        1
                );
                 return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void ObserveAnyChange() {
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null) {
                    for (MovieModel movieModel: movieModels) {
                        Log.v("Tag", "onChanged: " + movieModel.getTitle());
                    } //end for

                    movieRecyclerAdapter.setmMovies(movieModels);
                    movieRecyclerAdapter.notifyDataSetChanged();

                } //end if
             } //end onChanged
        });
    } //end OAC -----good

    private void getPopular() {
        movieListViewModel.getPopular();
    } //end getPopular



    private void ConfigureRecyclerView() {
        //Live Data cannot be passed via the constructor
        movieRecyclerAdapter = new MovieRecyclerView(this);


        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    } //end CRV ----- good

    @Override
    public void onMovieClick(int position) {
        Toast.makeText(this, "The Position " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClick(String category) {

    }


//
} //end class