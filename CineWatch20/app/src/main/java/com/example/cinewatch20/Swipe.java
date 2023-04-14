package com.example.cinewatch20;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cinewatch20.Adapters.MovieAdapter;
import com.example.cinewatch20.Adapters.OnMovieListener;
import com.example.cinewatch20.client.RecommendationClient;
import com.example.cinewatch20.data.Config;
import com.example.cinewatch20.data.FileUtil;
import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.data.Result;
import com.example.cinewatch20.models.MovieModel;
import com.example.cinewatch20.viewModels.MovieListViewModel;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
//import java.util.List;

public class Swipe extends AppCompatActivity implements OnMovieListener {

    private static final String CONFIG_PATH = "config.json";  // Default config path in assets.
    private static final String TAG = "CinemaFreak-HomeScreen";
    private MovieAdapter arrayAdapter;
    List<MovieModel> mMovies;
    SwipeFlingAdapterView flingAdapterView;
    Button search_view;

    //view model
    private MovieListViewModel movieListViewModel;

    OnMovieListener onMovieListener;

    List<String> genres;
    TreeMap<String, List<MovieItem>> movieGenreMap;
    private Handler handler;
    private RecommendationClient client;
    private List<MovieItem> movies;
    private List<Result> recommendations;
    private Config config;



    @SuppressLint({"MissingInflatedId", "WrongThread"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe);
        handler = new Handler();
        recommendations = new ArrayList<>();

        // Load config file.
        try {
            config = FileUtil.loadConfig(this.getAssets(), CONFIG_PATH);
        } catch (IOException ex) {
            Log.e(TAG, String.format("Error occurs when loading config %s: %s.", CONFIG_PATH, ex));
        }

        loadGenres();

        client = new RecommendationClient(this, config);
        handler.post(() -> client.load());

        recommendations = client.recommend(movies);



        mMovies = new ArrayList<>();
        mMovies.add(new MovieModel("Movie 1", "/oT8MbC0FuAcwZhuucO1YRRHcYSS.jpg", "2/2/2022", 1, "Yes", 8.7f, 3));

        flingAdapterView= findViewById(R.id.card);
        search_view = findViewById(R.id.scroll_view);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        //searchMovieApi("king", 1);
        //getPopular();
        ObserveAnyChange();



        arrayAdapter = new MovieAdapter(Swipe.this, R.layout.movie_list_item, mMovies, onMovieListener);

        flingAdapterView.setAdapter(arrayAdapter);



        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.v("Tag", "Movie: " + mMovies.get(0));
                mMovies.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(Swipe.this,"dislike",Toast.LENGTH_SHORT).show();
                //Log.v("Tag", "Movie: " + mMovies.get(0));

            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(Swipe.this,"liked",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });



        flingAdapterView.setOnItemClickListener((i, o) -> Toast.makeText(Swipe.this,"data is"+ mMovies.get(i),Toast.LENGTH_SHORT).show());

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform your action here

                Intent intent = new Intent(Swipe.this, MovieListActivity.class);
                startActivity(intent);

            }
        });




    } //end onCreate

    private void loadGenres() {
        try {
            InputStream inputStream = this.getAssets().open("movie_genre_vocab.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            String genre = new String(buffer);
            genres = Arrays.asList(genre.split("\\r?\\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPopular() {
        movieListViewModel.getPopular();
    } //end getPopular

    private void searchMovieApi(String query, int pageNumber) {
        movieListViewModel.searchMovieApi(query, pageNumber);
    } //end SMA ---- good


    private void ObserveAnyChange() {
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                if (movieModels != null) {
                    for (MovieModel movieModel: movieModels) {
                        Log.v("Tag", "onChanged: " + movieModel.getTitle());
                    } //end for


                    arrayAdapter.setmMovies(movieModels);
                    mMovies = movieModels;
                    //arrayAdapter.notifyDataSetChanged();

                } //end if
            } //end onChanged
        });
    } //end OAC -----good

    @Override
    public void onMovieClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }
} //end class