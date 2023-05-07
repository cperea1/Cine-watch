package com.example.cinewatch20.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cinewatch20.Adapters.MovieRecyclerView;
import com.example.cinewatch20.Adapters.OnMovieListener;
import com.example.cinewatch20.CineWatchApplication;
import com.example.cinewatch20.R;
import com.example.cinewatch20.data.Config;
import com.example.cinewatch20.data.FileUtil;
import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.database.DatabaseInstance;
import com.example.cinewatch20.models.User;
import com.example.cinewatch20.utils.Credentials;
import com.example.cinewatch20.utils.MovieJsonUpdater;
import com.example.cinewatch20.viewModels.MovieListViewModel;

import java.io.IOException;
import java.util.List;

public class Search extends AppCompatActivity implements OnMovieListener {

    private static final String TAG = "CineWatch - Search";
    //Recycler View
    private RecyclerView recyclerView; //good
    private MovieRecyclerView movieRecyclerAdapter ;
    //private MovieViewHolder movieViewHolder;
    private static final String CONFIG_PATH = "config.json";  // Default config path in assets.
    //view model
    private MovieListViewModel movieListViewModel;

    User activeUser;

    Button home;
    private Handler handler;
    private List<MovieItem> likedMovies;
    private Config config;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view);
        String userId = this.getIntent().getStringExtra(Credentials.ACTIVE_USER_KEY);
        activeUser = ((CineWatchApplication)getApplication()).getActiveSessionUser();
        recyclerView = findViewById(R.id.recyclerView); //good

        home = findViewById(R.id.home_button);
        try {
            config = FileUtil.loadConfig(this.getAssets(), CONFIG_PATH);
        } catch (IOException ex) {
            Log.e(TAG, String.format("Error occurs when loading config %s: %s.", CONFIG_PATH, ex));
        }


        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform your action here

                Intent intent = new Intent(Search.this, Swipe.class);
                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                startActivity(intent);

            } //end onclick
        });


        SetupSearchView();
        ConfigureRecyclerView();
        ObserveAnyChange();

        getPopular();
        //movieRecyclerAdapter.notifyDataSetChanged();





    } //end onCreate

//    @Override
//    protected void onPause() {
//
//        super.onPause();
//
//        movieListViewModel.setmMovies(new ArrayList<>());
//
//
//
//    }

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
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieItem>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<MovieItem> movieItems) {
                if (movieItems != null) {
//                    for (MovieItem movieItem: movieItems) {
//                        Log.v("Tag", "onChanged: " + movieItem.getTitle());
//                    } //end for

                    movieRecyclerAdapter.setmMovies(movieItems);
                    movieRecyclerAdapter.notifyDataSetChanged();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        MovieJsonUpdater.addMovieToJSON(movieItems, Search.this, config);
                    }

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
        Intent intent = new Intent(Search.this, InfoPage.class);
        intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
        intent.putExtra(Credentials.ACTIVE_MOVIE_KEY, movieRecyclerAdapter.getmMovies().get(position).getId());
        intent.putExtra("where", 2);
        startActivity(intent);

        //Toast.makeText(Search.this,"Info Page",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClick(String category) {

    }

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
                } else {
                    Log.e(TAG, "Unable to fetch active user");
                }
            });
        });
    } //end method


//
} //end class