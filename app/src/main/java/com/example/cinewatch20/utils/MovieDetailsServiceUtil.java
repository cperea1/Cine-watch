package com.example.cinewatch20.utils;

import android.util.Log;


import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.service.model.MovieDetails;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.utils.URIBuilder;

import java.util.ArrayList;
import java.util.List;



public class MovieDetailsServiceUtil {
    private static final String TAG = "CinemaFreak-MovieDetailsServiceUtil";

    public static String buildMovieDetailsUrl(int tmdbId){
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost(Credentials.TMDB_HOST_URL);
        builder.setPath(Credentials.MOVIE_PATH+"/"+tmdbId);
        builder.addParameter(Credentials.API_KEY_PARAM, Credentials.API_KEY);
        builder.addParameter(Credentials.APPEND_TO_RESPONSE, Credentials.VIDEOS);


        String url = "";
        try {
            url = builder.build().toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        url = url + "," + Credentials.WATCH_PROVIDERS;
        Log.d(TAG, "Url for movie details formed: "+url);
        return url;
    }


    public static List<MovieItem> DetailsToItem(List<MovieDetails> movieDetails){
        List<MovieItem> movieItems = new ArrayList<>();
        for(MovieDetails md : movieDetails){
            movieItems.add(new MovieItem(md.getId(), md.getTitle(), md.getGenres(), md.getProviders(), md.getPoster(), md.getOverview(), md.getTrailer(), md.getBackDrop(), md.getLikes(), md.getDislikes()));
        }

        return movieItems;
    }



}
