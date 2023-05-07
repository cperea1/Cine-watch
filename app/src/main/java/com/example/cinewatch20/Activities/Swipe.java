package com.example.cinewatch20.Activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.cinewatch20.models.User;
import com.example.cinewatch20.service.MovieDetailsCallback;
import com.example.cinewatch20.service.MovieDetailsService;
import com.example.cinewatch20.utils.Credentials;
import com.example.cinewatch20.viewModels.MovieListViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Swipe extends AppCompatActivity implements OnMovieListener, MovieDetailsCallback {
    ProgressBar progressBar;
    MovieAdapter arrayAdapter;
    List<MovieItem> mMovies;
    SwipeFlingAdapterView flingAdapterView;
    Button search_view, infoButton, saveButton, accountButton, neverSeenButton, trailerButton;
    private MovieListViewModel movieListViewModel;
    OnMovieListener onMovieListener;
    public FirebaseAuth mAuth;
    private static final String CONFIG_PATH = "config.json";  // Default config path in assets.
    private static final String TAG = "CineWatch-Swipe";
    private String userId;
    private Handler handler;
    private List<Result> recommendations;
    private Config config;
    List<String> genres;
    private RecommendationClient client;
    private User activeUser;
    private List<MovieItem> likedMovies;
    private boolean isServiceConnected;
    private boolean recommendationsHaveBeenFetched;
    private MovieDetailsService movieDetailsService;
    private ServiceConnection serviceConnection;
    private MovieItem firstMovie;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe);

        userId = this.getIntent().getStringExtra(Credentials.ACTIVE_USER_KEY);
        handler = new Handler();
        recommendations = new ArrayList<>();
        // Initialize the progress bar or spinner widget
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);



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
        //not sure why but we need to add something to get anything to show up
        mMovies.add(new MovieItem(-1, "helper", null, null, "/oT8MbC0FuAcwZhuucO1YRRHcYSS.jpg", "Yes", "", "", 0, 0.0f, "", "", 0, 0, false));
        arrayAdapter = new MovieAdapter(getApplicationContext(), R.layout.movie_list_item, mMovies, onMovieListener);
        flingAdapterView= findViewById(R.id.card);
        search_view = findViewById(R.id.scroll_view);
        accountButton = findViewById(R.id.account);
        infoButton = findViewById(R.id.info);
        saveButton = findViewById(R.id.save);
        trailerButton = findViewById(R.id.trailer);
        //neverSeenButton = findViewById(R.id.never_seen);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform your action here

                Intent intent = new Intent(Swipe.this, Search.class);
                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                startActivity(intent);

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!activeUser.getBookmarkedMovies().contains(mMovies.get(0))) {
                    activeUser.addBookmarkedMovies(mMovies.get(0));
                    ((CineWatchApplication) getApplication()).setActiveSessionUser(activeUser);
                    Toast.makeText(Swipe.this, mMovies.get(0).getTitle() + " Bookmarked",Toast.LENGTH_SHORT).show();


                }
                else
                    Toast.makeText(Swipe.this, "Movie already bookmarked!", Toast.LENGTH_SHORT).show();


            }
        });
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform your action here

                Intent intent = new Intent(Swipe.this, Account.class);
                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                startActivity(intent);



            }
        });


        //flingAdapterView.setOnItemClickListener((i, o) -> Toast.makeText(Swipe.this,"data is"+ mMovies.get(i),Toast.LENGTH_SHORT).show());

        ObserveAnyChange();
    } //end onCreate
    @Override
    public void onStart() {
        Log.v(TAG, "onStart()");
        super.onStart();
        Log.v(TAG, "onStart.activity.Swipe");
        handler.post(
                () -> {
                    client.load();
                });
        bindMovieDetailsService();

    } //end onStart

    @Override
    protected void onRestart() {
        super.onRestart();

        loadActiveUserFromDb(userId);
        arrayAdapter = new MovieAdapter(Swipe.this, R.layout.movie_list_item, mMovies, onMovieListener);
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "onPause()");
        super.onPause();
        Log.i(TAG, "Updating user changes in application");
        ((CineWatchApplication)getApplication()).setActiveSessionUser(activeUser);
        handler.post(() -> {
            DatabaseInstance.DATABASE.getReference().child("Users").child(activeUser.getId()).setValue(activeUser).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    Log.i(TAG, "Updated user in db: " + activeUser);
                } else {
                    Log.i(TAG, "Error in updating db for user: " + task.getException());
                }
            });
        });

//        mMovies.clear();
    } //end onPause

    private void ObserveAnyChange() {
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieItem>>() {
            @Override
            public void onChanged(List<MovieItem> movieItems) {
                if (movieItems != null) {
//                    for (MovieItem movieItem: movieItems) {
//                        Log.v("Tag", "onChanged: " + movieItem.getTitle());
//                    } //end for


                    arrayAdapter.setmMovies(movieItems);
                    mMovies = movieItems;
                    arrayAdapter.notifyDataSetChanged();

                    trailerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String videoId = mMovies.get(0).getTrailerID(); // Replace with your video ID
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
                            intent.putExtra("VIDEO_ID", videoId);
                            startActivity(intent);
                        }
                    });

                    infoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Perform your action here

                            Intent intent = new Intent(Swipe.this, InfoPage.class);
                            intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                            intent.putExtra(Credentials.ACTIVE_MOVIE_KEY, mMovies.get(0).getId());
                            intent.putExtra("where", 1);
                            startActivity(intent);

                            Toast.makeText(Swipe.this,"Info Page",Toast.LENGTH_SHORT).show();

                        }
                    });

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
                    likedMovies = activeUser.getLikedMovies();
                    executeRecommendationEngine();
                } else {
                    Log.e(TAG, "Unable to fetch active user");
                }
            });
        });
    } //end method

    private void executeRecommendationEngine() {
        likedMovies = activeUser.getLikedMovies();
        Log.i(TAG, "Executing recommendation engine for selected movies");
        // Run inference with TF Lite.
        Log.d(TAG, "Run inference with TFLite model.");
        recommendations = client.recommend(likedMovies);
        Log.d(TAG, "Recommendations loaded");
        if (isServiceConnected && !recommendationsHaveBeenFetched) {
            Log.d(TAG, "Entered from db");
            fetchMovieDetailsForRecommendations();
        }
    }

    private void fetchMovieDetailsForRecommendations() {
        recommendationsHaveBeenFetched = true;
        List<Integer> selectedMovies = likedMovies.stream().map(MovieItem::getId).collect(Collectors.toList());
        recommendations = recommendations.stream().filter(result -> !selectedMovies.contains(result.item.getId())).collect(Collectors.toList());
        Log.i(TAG, "Fetching movie details for all recommendations");
        movieDetailsService.getMoviesDetails(
                recommendations.stream().map(r -> r.item.getId()).collect(Collectors.toList()), (MovieDetailsCallback) this, activeUser.getSubscriptions());
    }



    private void bindMovieDetailsService(){
        Log.v(TAG, "bindMovieDetailsService()");
        Intent serviceIntent = new Intent(this, MovieDetailsService.class);
        this.startService(serviceIntent);

        if(serviceConnection == null) {
            Log.v(TAG, "serviceConnection = Null, creating a new serviceConnection");
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
    private void showResult(List<MovieItem> recommendations) {
        movieListViewModel.setmMovies(recommendations);
        progressBar.setVisibility(View.GONE);

        arrayAdapter = new MovieAdapter(getApplicationContext(), R.layout.movie_list_item, mMovies, onMovieListener);

        flingAdapterView.setAdapter(arrayAdapter);
        flingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                //work is done in swipe left and right
            }

            @Override
            public void onLeftCardExit(Object o) {
                Toast.makeText(Swipe.this,"dislike",Toast.LENGTH_SHORT).show();
                Log.v("Fling", "Movie Disliked: " + mMovies.get(0));

                activeUser.addDislikedMovieItem(mMovies.get(0));
                activeUser.removeLikedMovieItem(mMovies.get(0).getId());
                mMovies.remove(0);
                arrayAdapter.notifyDataSetChanged();


                Log.v("Movie Providers", mMovies.get(0).getProviders().toString());
                Log.v("User Subscriptions", activeUser.getSubscriptions().toString());


            }
            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(Swipe.this,"liked",Toast.LENGTH_SHORT).show();
                Log.v("Fling", "Movie Liked: " + mMovies.get(0));
                activeUser.addLikedMovieItem(mMovies.get(0));
                activeUser.removeDislikedMovieItem(mMovies.get(0).getId());

                mMovies.remove(0);
                arrayAdapter.notifyDataSetChanged();

                Log.v("Movie Providers", mMovies.get(0).getProviders().toString());
                Log.v("User Subscriptions", activeUser.getSubscriptions().toString());

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                bindMovieDetailsService();
                executeRecommendationEngine();
                fetchMovieDetailsForRecommendations();
            }

            @Override
            public void onScroll(float v) {

            }
        });

           } //end showResult

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy()");
        super.onDestroy();
        if(!isServiceConnected){
            return;
        }
        Log.i(TAG, "unbinding service from recommendation page");
        this.unbindService(serviceConnection);
        serviceConnection = null;
        isServiceConnected = false;
    } //end onDestroy
} //end class