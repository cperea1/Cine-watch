package com.example.cinewatch20;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.cinewatch20.models.MovieModel;
import com.example.cinewatch20.request.Service;
import com.example.cinewatch20.response.MovieSearchResponse;
import com.example.cinewatch20.utils.Credentials;
import com.example.cinewatch20.utils.MovieApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetRetrofitResponseAccordingToID();
            }
        });


    }

    private void GetRetrofitResponse() {
        MovieApi movieApi = Service.getMovieApi();

        Call<MovieSearchResponse> responseCall = movieApi
                .searchMovie(
                        Credentials.API_KEY,
                        "Lilo & Stitch",
                        1
                );

        responseCall.enqueue(new Callback<MovieSearchResponse>() {
            @Override
            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
                if (response.code() == 200) {
                    Log.v("Tag", "the response" + response.body().toString());

                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());

                    for (MovieModel movie: movies) {
                        Log.v("Tag", "The Release Date " + movie.getRelease_date());
                    } //end for
                } //end if
                else {
                    try {
                        Log.v("Tag", "Error" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } //end else
            }

            @Override
            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {

            }
        });

    } //end GRR

    private void GetRetrofitResponseAccordingToID() {
        MovieApi movieApi = Service.getMovieApi();
        Call<MovieModel> responseCall = movieApi
                .getMovie(550,
                        Credentials.API_KEY);

        responseCall.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {
                    MovieModel movie = response.body();
                    Log.v("Tag", "Name: " + movie.getTitle());
                } //end if
                else {
                    try {
                        Log.v("Tag", "Error  " + response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } {

                    }
                } //end else
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }
} //end class