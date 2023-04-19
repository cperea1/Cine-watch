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
    private int runtime;

    //constructor


    public MovieModel(String title, String poster_path, String release_date, int movie_id, String overview, float vote_average, int runtime) {
        this.title = title;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.movie_id = movie_id;
        this.overview = overview;
        this.vote_average = vote_average;
        this.runtime = runtime;
    } //end constructor

    public MovieModel(String title, String poster_path, int movie_id, String overview) {
        this.title = title;
        this.poster_path = poster_path;
        this.movie_id = movie_id;
        this.overview = overview;
    }

    protected MovieModel(Parcel in) {
        title = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        movie_id = in.readInt();
        overview = in.readString();
        vote_average = in.readFloat();
        runtime = in.readInt();
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

    public String getImageUrl() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getId() {
        return movie_id;
    }

    public String getDescription() {
        return overview;
    }

    public float getVote_average() {
        return vote_average;
    }

    public int getRuntime() {
        return runtime;
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
        parcel.writeInt(runtime);
    }

    @Override
    public String toString() {
        return "MovieModel{" +
                "title='" + title + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", release_date='" + release_date + '\'' +
                ", movie_id=" + movie_id +
                ", overview='" + overview + '\'' +
                ", vote_average=" + vote_average +
                ", runtime=" + runtime +
                '}';
    }
} //end class
