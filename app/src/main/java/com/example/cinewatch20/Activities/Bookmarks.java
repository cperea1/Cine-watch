package com.example.cinewatch20.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinewatch20.Adapters.MovieRecyclerView;
import com.example.cinewatch20.Adapters.OnMovieListener;
import com.example.cinewatch20.CineWatchApplication;
import com.example.cinewatch20.R;
import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.database.DatabaseInstance;
import com.example.cinewatch20.models.User;
import com.example.cinewatch20.utils.Credentials;
import com.example.cinewatch20.viewModels.MovieListViewModel;

import java.util.List;

public class Bookmarks  extends AppCompatActivity implements OnMovieListener {

    private static final String TAG = "CineWatch - Bookmarks";
    //Recycler View
    private RecyclerView recyclerView; //good
    MovieRecyclerView movieRecyclerAdapter ;
    //private MovieViewHolder movieViewHolder;

    //view model
    MovieListViewModel movieListViewModel;

    User activeUser;

    Button home;
    private Handler handler;
    private List<MovieItem> movies;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmarks);

        String userId = this.getIntent().getStringExtra(Credentials.ACTIVE_USER_KEY);
        activeUser = ((CineWatchApplication)getApplication()).getActiveSessionUser();
        recyclerView = findViewById(R.id.bookmark_list); //good

        home = findViewById(R.id.home_button2);
        handler = new Handler();

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform your action here

                Intent intent = new Intent(Bookmarks.this, Swipe.class);
                intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                startActivity(intent);

            } //end onclick
        });

        //SetupSearchView();
        ConfigureRecyclerView();
        //ObserveAnyChange();


    } //end onCreate

    private void ConfigureRecyclerView() {
        //Live Data cannot be passed via the constructor
        movieRecyclerAdapter = new MovieRecyclerView(this);


        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieRecyclerAdapter.setmMovies(activeUser.getBookmarkedMovies());
    } //end CRV ----- good

    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(Bookmarks.this, InfoPage.class);
        intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
        intent.putExtra(Credentials.ACTIVE_MOVIE_KEY, activeUser.getBookmarkedMovies().get(position).getId());
        intent.putExtra("where", 3);
        startActivity(intent);

        Toast.makeText(Bookmarks.this,"Info Page",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCategoryClick(String category) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        activeUser = ((CineWatchApplication) getApplication()).getActiveSessionUser();
    } //end onStart

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
} //end class
