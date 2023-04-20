package com.example.cinewatch20.Activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cinewatch20.Adapters.*;
import com.example.cinewatch20.CineWatchApplication;
import com.example.cinewatch20.R;
import com.example.cinewatch20.client.RecommendationClient;
import com.example.cinewatch20.data.Config;
import com.example.cinewatch20.data.FileUtil;
import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.data.Result;
import com.example.cinewatch20.database.DatabaseInstance;
import com.example.cinewatch20.models.MovieModel;
import com.example.cinewatch20.models.User;
import com.example.cinewatch20.service.MovieDetailsCallback;
import com.example.cinewatch20.service.MovieDetailsService;
import com.example.cinewatch20.utils.Credentials;
import com.example.cinewatch20.utils.MovieDetailsServiceUtil;
import com.example.cinewatch20.viewModels.MovieListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;


public class Swipe extends AppCompatActivity implements OnMovieListener, MovieDetailsCallback {
    private MovieAdapter arrayAdapter;
    List<MovieModel> mMovies;
    SwipeFlingAdapterView flingAdapterView;
    Button search_view;
    private MovieListViewModel movieListViewModel;
    OnMovieListener onMovieListener;
    public FirebaseAuth mAuth;
    private static final String CONFIG_PATH = "config.json";  // Default config path in assets.
    private static final String TAG = "CineWatch-MovieRecommendation";
    private String userId;
    private Handler handler;
    private TreeMap<String, List<MovieItem>> movieGenreMap;
    private List<Result> recommendations;
    private Config config;
    List<String> genres;
    private RecommendationClient client;
    private User activeUser;
    private List<MovieItem> movies;
    private boolean isServiceConnected;
    private boolean recommendationsHaveBeenFetched;
    private MovieDetailsService movieDetailsService;
    private ServiceConnection serviceConnection;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe);

        userId = this.getIntent().getStringExtra(Credentials.ACTIVE_USER_KEY);
        handler = new Handler();
        movieGenreMap = new TreeMap<>();


        recommendations = new ArrayList<>();

        try {
            config = FileUtil.loadConfig(this.getAssets(), CONFIG_PATH);
        } catch (IOException ex) {
            Log.e(TAG, String.format("Error occurs when loading config %s: %s.", CONFIG_PATH, ex));
        }

        //loadGenres();



        client = new RecommendationClient(this, config);
        handler.post(() -> client.load());

        recommendationsHaveBeenFetched = false;
        loadActiveUserFromDb(userId);



        mMovies = new ArrayList<>();
        mMovies.add(new MovieModel("Movie 1", "/oT8MbC0FuAcwZhuucO1YRRHcYSS.jpg", "2/2/2022", 1, "Yes", 8.7f, 3));

        flingAdapterView= findViewById(R.id.card);
        search_view = findViewById(R.id.scroll_view);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform your action here

                Intent intent = new Intent(Swipe.this, MovieListActivity.class);
                startActivity(intent);

            }
        });

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

                activeUser.addDislikedMovieItem(mMovies.get(0));

            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(Swipe.this,"liked",Toast.LENGTH_SHORT).show();

                activeUser.addLikedMovieItem(mMovies.get(0));

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }

            @Override
            public void onScroll(float v) {

            }
        });
        flingAdapterView.setOnItemClickListener((i, o) -> Toast.makeText(Swipe.this,"data is"+ mMovies.get(i),Toast.LENGTH_SHORT).show());

        ObserveAnyChange();

    } //end onCreate



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
                    arrayAdapter.notifyDataSetChanged();

                } //end if
            } //end onChanged
        });
    } //end OAC

    @Override
    public void onMovieClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {

    }

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
    } //end loadGenres

    private void loadActiveUserFromDb(String userId) {
        handler.post(() -> {
            Log.i(TAG, "Fetching user details from database");
            if(userId == null)
                return;
            DatabaseInstance.DATABASE.getReference().child("Users").child(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    activeUser = task.getResult().getValue(User.class);
                    ((CineWatchApplication)getApplication()).setActiveSessionUser(activeUser);
                    Log.d(TAG, "User " + userId + " fetched from database: " + activeUser);
                    movies = MovieDetailsServiceUtil.ModelToItem(activeUser.getLikedMovies());
                    executeRecommendationEngine();
                } else {
                    Log.e(TAG, "Unable to fetch active user");
                }
            });
        });
    } //end method

    private void executeRecommendationEngine() {
        Log.i(TAG, "Executing recommendation engine for selected movies");
        // Run inference with TF Lite.
        Log.d(TAG, "Run inference with TFLite model.");
        recommendations = client.recommend(movies);
        Log.d(TAG, "Recommendations loaded");
        if (isServiceConnected && !recommendationsHaveBeenFetched) {
            Log.d(TAG, "Entered from db");
            fetchMovieDetailsForRecommendations();
        }
    }

    private void fetchMovieDetailsForRecommendations() {
        recommendationsHaveBeenFetched = true;
        List<Integer> selectedMovies = movies.stream().map(MovieItem::getId).collect(Collectors.toList());
        recommendations = recommendations.stream().filter(result -> !selectedMovies.contains(result.item.getId())).collect(Collectors.toList());
        Log.i(TAG, "Fetching movie details for all recommendations");
        movieDetailsService.getMoviesDetails(
                recommendations.stream().map(r -> r.item.getId()).collect(Collectors.toList()), (MovieDetailsCallback) this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart.activity.MovieRecommendation");
        handler.post(
                () -> {
                    client.load();
                });
        bindMovieDetailsService();
    }

    private void bindMovieDetailsService(){
        Intent serviceIntent = new Intent(this, MovieDetailsService.class);
        this.startService(serviceIntent);

        if(serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.i(TAG, "On service connected with component");
                    isServiceConnected = true;
                    MovieDetailsService.MovieDetailsBinder binder = (MovieDetailsService.MovieDetailsBinder) service;
                    movieDetailsService = binder.getService();
                    if (recommendations.size() > 0 && !recommendationsHaveBeenFetched) {
                        fetchMovieDetailsForRecommendations();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Log.i(TAG, "On service disconnected");
                    isServiceConnected = false;
                }

            };
            Log.i(TAG, "Service bound");
            this.bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void dbMovieDetails(List<MovieItem> movieItems) {
        showResult(movieItems);
    }
    private void showResult(final List<MovieItem> recommendations) {
        ////////this is what we need to change

        //loadMap(recommendations);
        //movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        mMovies = MovieDetailsServiceUtil.ItemToModel(recommendations);
        movieListViewModel.setmMovies(mMovies);


           } //end showResult

    private void loadMap(List<MovieItem> recommendations) {
        for (MovieItem movieItem : recommendations) {
            for (String genre : movieItem.getGenres()) {
                if (!movieGenreMap.containsKey(genre)) {
                    List<MovieItem> list = new ArrayList<>();
                    movieGenreMap.put(genre, list);
                }
                movieGenreMap.get(genre).add(movieItem);
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!isServiceConnected){
            return;
        }
        Log.i(TAG, "unbinding service from recommendation page");
        this.unbindService(serviceConnection);
        serviceConnection = null;
        isServiceConnected = false;
    }

} //end class