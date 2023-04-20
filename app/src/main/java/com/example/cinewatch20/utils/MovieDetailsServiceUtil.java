package com.example.cinewatch20.utils;

import android.util.Log;


import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.models.MovieModel;
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
        builder.addParameter(Credentials.VIDEOS_PARAM, Credentials.VIDEOS);

        String url = "";
        try {
            url = builder.build().toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Url for movie details formed: "+url);
        return url;
    }


    public static List<MovieItem> DetailsToItem(List<MovieDetails> movieDetails){
        List<MovieItem> movieItems = new ArrayList<>();
        for(MovieDetails md : movieDetails){
            movieItems.add(new MovieItem(md.getId(), md.getTitle(), md.getGenres(), md.getPoster(), md.getOverview(), md.getTrailer(), md.getBackDrop(), md.getLikes(), md.getDislikes()));
        }
        return movieItems;
    }

    public static List<MovieModel> ItemToModel(List<MovieItem> movieItems){
        List<MovieModel> movieModels = new ArrayList<>();
        for(MovieItem mi : movieItems){
            movieModels.add(new MovieModel(mi.getTitle(), mi.getImageUrl(), mi.getId(),  mi.getDescription()));
        }
        return movieModels;
    }

    public static List<MovieItem> ModelToItem(List<MovieModel> movieModels){
        List<MovieItem> movieItems = new ArrayList<>();
        for(MovieModel mm : movieModels){
            movieItems.add(new MovieItem(mm.getId(), mm.getTitle(), mm.getImageUrl(),  mm.getDescription()));
        }
        return movieItems;
    }

}
