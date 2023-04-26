package com.example.cinewatch20.utils;

import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    //Search for movies
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );


    //Making search ith ID
    @GET("/3/movie/{movie_id}?")
    Call<MovieItem> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String key
            );

    @GET("/3/movie/popular?")
    Call<MovieSearchResponse> getPopularMovie(
            @Query("api_key") String key
    );
}
