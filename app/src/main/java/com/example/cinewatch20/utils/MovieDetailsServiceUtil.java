package com.example.cinewatch20.utils;

import android.util.Log;

import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.service.model.Genre;
import com.example.cinewatch20.service.model.MovieDetails;
import com.example.cinewatch20.service.model.WatchProviders;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.utils.URIBuilder;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MovieDetailsServiceUtil {
    private static final String TAG = "CineWatch - MovieDetailsServiceUtil";

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
            movieItems.add(new MovieItem(
                    md.getId(),
                    md.getTitle(),
                    md.getGenres(),
                    md.getProviders(),
                    md.getPoster_path(),
                    md.getOverview(),
                    md.getTrailer(),
                    md.getRelease_date(),
                    md.getRuntime(),
                    md.getVote_average(),
                    md.getTagline(),
                    md.getBackdrop_path()
            ));
        }

        return movieItems;
    }

    public static MovieDetails mapDataToMovieDetails(int tmdbId, DataSnapshot snapshot) {
        MovieDetails md = new MovieDetails();
        md.setId(tmdbId);
        md.setOverview(snapshot.child("overview").getValue(String.class));
        md.setTitle(snapshot.child("title").getValue(String.class));
        md.setPoster_path(snapshot.child("poster_path").getValue(String.class));
        md.setBackdrop_path(snapshot.child("backdrop_path").getValue(String.class));
        md.setTrailer(snapshot.child("trailer").getValue(String.class));
        md.setRelease_date(snapshot.child("release_date").getValue(String.class));
        md.setTagline(snapshot.child("tagline").getValue(String.class));
        md.setVote_average(snapshot.hasChild("vote_average")? snapshot.child("vote_average").getValue(Float.class) : 0);
        md.setRuntime(snapshot.hasChild("runtime")? snapshot.child("runtime").getValue(Integer.class) : 0);


        List<WatchProviders.Provider> providersList = new ArrayList<>();

        DataSnapshot providerSnapshot = snapshot.child("providers");
        for (DataSnapshot provider : providerSnapshot.getChildren()) {
            WatchProviders.Provider providerObj = provider.getValue(WatchProviders.Provider.class);
            providersList.add(providerObj);
        }

        md.setProviders(providersList);

        String genres = snapshot.child("genres").getValue(String.class);

        if(genres != null){
            List<Genre> genreList = new ArrayList<>();
            for(String genre: genres.split(",")){
                genreList.add(new Genre(genre));
            }
            md.setGenres(genreList);
        }
        //Log.d(TAG, "Movie mapped: "+md);
        return md;

    }

    public static List<MovieItem> filterBySubscription(List<MovieItem> mi, List<String> subs) {
        List<MovieItem> movieItems = new ArrayList<>();


        for (MovieItem movieItem : mi) {
            List<String> providers = new ArrayList<>();

            for (WatchProviders.Provider provider : movieItem.getProviders()) {
                providers.add(provider.getProviderName());
            }
            if(Collections.disjoint(providers, subs)){
                continue;
            }

            movieItems.add(movieItem);
        }
        return movieItems;

    } //end fbs



}
