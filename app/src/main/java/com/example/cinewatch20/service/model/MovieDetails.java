package com.example.cinewatch20.service.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MovieDetails {

    private int id;
    private String title;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private List<Genre> genres;
    private Videos videos;
    private String trailer;
    private String release_date;
    private int runtime;
    private float vote_average;
    private String tagline;
    @SerializedName("watch/providers")
    private WatchProviders watchProviders;
    private List<WatchProviders.Provider> providers;

    //Constructor
    public MovieDetails(){
        genres = new ArrayList<>();
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public List<WatchProviders.Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<WatchProviders.Provider> providers) {
        this.providers = providers;
    }

    public WatchProviders getWatchProviders() {
        return watchProviders;
    }

    public void setWatchProviders(WatchProviders watchProviders) {
        this.watchProviders = watchProviders;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public Videos getVideos() {
        return videos;
    }

    public void setVideos(Videos videos) {
        this.videos = videos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String original_title) {
        this.title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public List<String> getGenres() {
        return genres.stream().map(Genre::getName).collect(Collectors.toList());
    }
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }


    @Override
    public String toString() {
        return "MovieDetails{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", poster='" + poster_path + '\'' +
                ", backDrop='" + backdrop_path + '\'' +
                ", genres=" + genres +
                ", videos=" + videos +
                ", trailer='" + trailer + '\'' +
                ", releaseDate='" + release_date + '\'' +
                ", runtime=" + runtime +
                ", voteAverage=" + vote_average +
                ", tagline='" + tagline + '\'' +
                ", watchProviders=" + watchProviders +
                ", providers=" + providers +
                '}';
    }
}
