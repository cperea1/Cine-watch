package com.example.cinewatch20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Info extends AppCompatActivity {
    private Button addToBookmarksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_page);

        // Initialize views
        ImageView moviePosterImageView = findViewById(R.id.movie_poster);
        TextView movieTitleTextView = findViewById(R.id.movie_title);
        RatingBar ratingBar = findViewById(R.id.movie_rating);
        ListView actorsTextView = findViewById(R.id.movie_actors);
        TextView bioTextView = findViewById(R.id.movie_bio);
        TextView platformsTextView = findViewById(R.id.movie_platforms);
        addToBookmarksButton = findViewById(R.id.add_to_bookmarks_button);

        // Set placeholder values for movie information
        moviePosterImageView.setImageResource(R.drawable.ic_launcher_background);
        movieTitleTextView.setText("Movie Title");
        ratingBar.setRating(3.5f);


        // Set click listener for the "Add to bookmarks" button
        addToBookmarksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add movie to bookmarks list
                addToBookmarks();
            }
        });
    }

    private void addToBookmarks() {
        // Implement code to add movie to bookmarks list
        // Create an intent to add the movie to the SavedMoviesActivity
        Intent intent = new Intent(this, LikedMoviesList.class);
        intent.putExtra("movie_title", movieTitleTextView.getText().toString());
        intent.putExtra("movie_poster_url", moviePosterUrl);
        intent.putExtra("movie_rating", movieRatingTextView.getText().toString());
        intent.putExtra("movie_actors", movieActorsTextView.getText().toString());
        intent.putExtra("movie_bio", movieBioTextView.getText().toString());
        intent.putExtra("movie_platforms", moviePlatformsTextView.getText().toString());

        // Start the SavedMoviesActivity with the intent
        startActivity(intent);

        // Show a toast message to indicate that the movie was added to bookmarks
        Toast.makeText(this, "Movie added to bookmarks", Toast.LENGTH_SHORT).show();



    }

}

