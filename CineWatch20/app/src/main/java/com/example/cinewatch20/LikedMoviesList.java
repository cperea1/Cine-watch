package com.example.cinewatch20;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LikedMoviesList extends AppCompatActivity {

    private ArrayList<String> bookmarkedMovies;
    private ListView bookmarkList;
    private List<String> bookmarksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_movies);

        // Initialize views
        bookmarksList = loadBookmarksList();

        // Retrieve the movie information from the intent extras
        String movieId = getIntent().getStringExtra("movie_id");
        String movieTitle = getIntent().getStringExtra("movie_title");
        ImageView moviePoster = getIntent().getStringExtra("movie_poster");
        RatingBar ratingBar = getIntent().getStringExtra("movie_title");
        ListView actorsTextView = getIntent().getStringExtra("movie_title");
        TextView bioTextView = getIntent().getStringExtra("movie_title");
        TextView platformsTextView= getIntent().getStringExtra("movie_title");






    } //end onCreate
}
