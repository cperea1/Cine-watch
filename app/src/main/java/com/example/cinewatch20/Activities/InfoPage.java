package com.example.cinewatch20.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.cinewatch20.Adapters.MovieViewHolder;
import com.example.cinewatch20.CineWatchApplication;
import com.example.cinewatch20.R;
import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.models.User;
import com.example.cinewatch20.utils.Credentials;
import com.example.cinewatch20.viewModels.MovieListViewModel;

public class InfoPage extends AppCompatActivity {
    private final String TAG = "CineWatch - InfoPage";
    ImageView moviePosterImageView;
    RatingBar ratingBar;
    TextView actorsTextView;
    TextView movieTitleTextView;
    TextView bioTextView;
    TextView platformsTextView;
    Button addToBookmarksButton;
    public static MovieItem movieItem;
    MovieListViewModel movieListViewModel;
    int movieId;
    String userId;
    User activeUser;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_page);

        movieId = getIntent().getIntExtra(Credentials.ACTIVE_MOVIE_KEY, 0);
        userId = getIntent().getStringExtra(Credentials.ACTIVE_USER_KEY);
        activeUser = ((CineWatchApplication)getApplication()).getActiveSessionUser();

        // Initialize views

        moviePosterImageView = findViewById(R.id.movie_poster);

        movieTitleTextView = findViewById(R.id.movieInfoTitle);

        ratingBar = findViewById(R.id.movie_rating);

        actorsTextView = findViewById(R.id.movie_actors);

        bioTextView = findViewById(R.id.movie_bio);

        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        platformsTextView = findViewById(R.id.movie_platforms);
        addToBookmarksButton = findViewById(R.id.add_to_bookmarks_button);


        searchByID(movieId);

        Log.v(TAG, "movieItem: " + movieItem);





        // Set placeholder values for movie information
        Glide.with(this)
                .load(Credentials.TMDB_POSTER_PATH + movieItem.getPoster_path())
                .into(moviePosterImageView);

        movieTitleTextView.setText(movieItem.getTitle());
        ratingBar.setRating(3.5f);


        // Set click listener for the "Add to bookmarks" button
        addToBookmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add movie to bookmarks list
                for (MovieItem item : activeUser.getBookmarkedMovies()) {
                    if (movieItem.getId() == movieId) {
                        activeUser.removeBookmarkedMovies(movieId);
                        Toast.makeText(InfoPage.this, "Movie removed from bookmarks!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } //end for
                activeUser.addBookmarkedMovies(movieItem);
                Toast.makeText(InfoPage.this, "Movie added to bookmarks", Toast.LENGTH_SHORT).show();

            }
        });
    }

//    private void addToBookmarks() {
//        // Implement code to add movie to bookmarks list
//        // Create an intent to add the movie to the SavedMoviesActivity
//        Intent intent = new Intent(this, LikedMoviesList.class);
//        intent.putExtra("movie_title", movieTitleTextView.getText().toString());
//        intent.putExtra("movie_poster_url", moviePosterUrl);
//        intent.putExtra("movie_rating", movieRatingTextView.getText().toString());
//        intent.putExtra("movie_actors", movieActorsTextView.getText().toString());
//        intent.putExtra("movie_bio", movieBioTextView.getText().toString());
//        intent.putExtra("movie_platforms", moviePlatformsTextView.getText().toString());
//
//        // Start the SavedMoviesActivity with the intent
//        startActivity(intent);
//
//        // Show a toast message to indicate that the movie was added to bookmarks
//        Toast.makeText(this, "Movie added to bookmarks", Toast.LENGTH_SHORT).show();
//
//
//
//    }

    private void searchByID(int id) {
        movieListViewModel.searchByID(id);
    } //end getPopular

} //end class