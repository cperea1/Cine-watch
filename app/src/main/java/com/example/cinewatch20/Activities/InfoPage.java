package com.example.cinewatch20.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.cinewatch20.CineWatchApplication;
import com.example.cinewatch20.R;
import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.database.DatabaseInstance;
import com.example.cinewatch20.models.User;
import com.example.cinewatch20.service.MovieItemCallback;
import com.example.cinewatch20.service.model.MovieDetails;
import com.example.cinewatch20.utils.Credentials;
import com.example.cinewatch20.utils.MovieDetailsServiceUtil;
import com.example.cinewatch20.viewModels.MovieListViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InfoPage extends AppCompatActivity implements MovieItemCallback {
    private final String TAG = "CineWatch - InfoPage";
    ImageView moviePosterImageView;
    RatingBar ratingBar;
    TextView runtimeTextView;
    TextView releaseDateTextView;
    TextView movieTitleTextView;
    TextView bioTextView;
    TextView platformsTextView;
    Button bookmarksButton, backButton;
    MovieItem movieItem;
    MovieListViewModel movieListViewModel;
    int movieId;
    String userId;
    User activeUser;
    int where;
    private Handler handler;


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_page);

        handler = new Handler();
        movieId = getIntent().getIntExtra(Credentials.ACTIVE_MOVIE_KEY, 0);
        userId = getIntent().getStringExtra(Credentials.ACTIVE_USER_KEY);
        loadActiveUserFromDb(userId);
        activeUser = ((CineWatchApplication)getApplication()).getActiveSessionUser();

        where = getIntent().getIntExtra("where", 1);

        // Initialize views

        moviePosterImageView = findViewById(R.id.movie_poster);

        movieTitleTextView = findViewById(R.id.movieInfoTitle);

        ratingBar = findViewById(R.id.movie_rating);

        runtimeTextView = findViewById(R.id.runtimeText);
        releaseDateTextView = findViewById(R.id.releaseDateText);

        bioTextView = findViewById(R.id.movie_bio);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        platformsTextView = findViewById(R.id.movie_platforms);
        bookmarksButton = findViewById(R.id.add_to_bookmarks_button);

        backButton = findViewById(R.id.backbutton);


        if (activeUser.getBookmarkedMovies().stream().anyMatch(movieItem -> movieItem.getId() == movieId))
            bookmarksButton.setText("Remove from bookmarks");




        getInfo((MovieItemCallback) this);




        // Set click listener for the "Add to bookmarks" button
        bookmarksButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Add movie to bookmarks list
                for (MovieItem item : activeUser.getBookmarkedMovies()) {
                    if (item.getId() == movieId) {
                        activeUser.removeBookmarkedMovies(movieId);
                        ((CineWatchApplication) getApplication()).setActiveSessionUser(activeUser);
                        Toast.makeText(InfoPage.this, "Movie removed from bookmarks!", Toast.LENGTH_SHORT).show();
                        bookmarksButton.setText("Add to bookmarks");
                        return;
                    }
                } //end for
                activeUser.addBookmarkedMovies(movieItem);
                ((CineWatchApplication) getApplication()).setActiveSessionUser(activeUser);
                bookmarksButton.setText("remove from bookmarks");
                Toast.makeText(InfoPage.this, "Movie added to bookmarks", Toast.LENGTH_SHORT).show();

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch(where) {
                    case 1:
                        intent = new Intent(InfoPage.this, Swipe.class);
                        break;
                    case 2:
                        intent = new Intent(InfoPage.this, Search.class);
                        break;
                    case 3:
                        intent = new Intent(InfoPage.this, Bookmarks.class);
                        break;
                    case 4:
                        intent = new Intent(InfoPage.this, LikedMovies.class);
                        break;
                } //end switch


                if (intent != null) {
                    intent.putExtra(Credentials.ACTIVE_USER_KEY, activeUser.getId());
                }
                startActivity(intent);

            }
        });
    } //end onCreate


    private void getInfo(MovieItemCallback callback) {
        DatabaseReference childRef = DatabaseInstance.DATABASE.getReference("movies");
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MovieDetails md = MovieDetailsServiceUtil.mapDataToMovieDetails(movieId, snapshot.child(movieId+ ""));
                List<MovieDetails> mdl = new ArrayList<>();
                mdl.add(md);
                movieItem = MovieDetailsServiceUtil.DetailsToItem(mdl).get(0);


                // Call the callback with the populated movieItem
                callback.onMovieItemLoaded(movieItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "error reading from firebase: "+error.getMessage());
            }
        });

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onMovieItemLoaded(MovieItem movieItem) {
        Log.v(TAG, "movieItem: " + movieItem);





        // Set placeholder values for movie information
        Glide.with(this)
                .load(Credentials.TMDB_POSTER_PATH + movieItem.getPoster_path())
                .into(moviePosterImageView);

        movieTitleTextView.setText(movieItem.getTitle());
        ratingBar.setRating(movieItem.getVote_average());
        bioTextView.setText(movieItem.getOverview());
        releaseDateTextView.setText(movieItem.getRelease_date());
        runtimeTextView.setText(movieItem.getRuntime() == 0? "" : "Runtime: " + movieItem.getRuntime());

        StringBuilder sb = new StringBuilder();
        movieItem.getProviders().forEach(m -> sb.append(m.getProviderName().trim()).append(", "));
        if(sb.length()>0)
            platformsTextView.setText(sb);
        else
            platformsTextView.setText("");

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
                } else {
                    Log.e(TAG, "Unable to fetch active user");
                }
            });
        });
    } //end method
} //end class