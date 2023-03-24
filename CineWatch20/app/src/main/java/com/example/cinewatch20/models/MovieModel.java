package com.example.cinewatch20.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MovieModel implements Parcelable {
    //Model class for our movies

    private String title;
    private String poster_path;
    private String release_date;
    private int movie_id;
    private String overview;
    private float vote_average;

    //constructor


    public MovieModel(String title, String poster_path, String release_date, int movie_id, String overview, float vote_average) {
        this.title = title;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.movie_id = movie_id;
        this.overview = overview;
        this.vote_average = vote_average;
    } //end constructor

    protected MovieModel(Parcel in) {
        title = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        movie_id = in.readInt();
        overview = in.readString();
        vote_average = in.readFloat();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public String getOverview() {
        return overview;
    }

    public float getVote_average() {
        return vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeString(release_date);
        parcel.writeInt(movie_id);
        parcel.writeString(overview);
        parcel.writeFloat(vote_average);
    }
} //end class
