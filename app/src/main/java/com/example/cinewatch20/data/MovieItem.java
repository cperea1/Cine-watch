/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cinewatch20.data;

import com.example.cinewatch20.service.model.WatchProviders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A movie item representing recommended content.
 */
public class MovieItem implements Serializable {

    public static final String JOINER = " | ";

    private final int id;
    private final String title;
    private final List<String> genres;
    private final List<WatchProviders.Provider> providers;
    private String poster_path;
    private String overview;
    private String trailerID;
    private String release_date;
    private int runtime;
    private float vote_average;
    private String tagline;
    private String backdrop_path;

    public MovieItem(int id, String title, List<String> genres, List<WatchProviders.Provider> providers, String poster_path, String overview, String trailerID, String release_date, int runtime, float vote_average, String tagline, String backdrop_path) {
        this.id = id;
        this.title = title;
        this.genres = genres;
        this.providers = providers;
        this.poster_path = poster_path;
        this.overview = overview;
        this.trailerID = trailerID;
        this.release_date = release_date;
        this.runtime = runtime;
        this.vote_average = vote_average;
        this.tagline = tagline;
        this.backdrop_path = backdrop_path;
    }

    public MovieItem() {
        this(0, "", new ArrayList<>(), new ArrayList<>(), "", "", "", "", 0, 0.0f, "", "");
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

    public String getTrailerID() {
        return trailerID;
    }

    public void setTrailerID(String trailerID) {
        this.trailerID = trailerID;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    @Override
    public String toString() {
        return "MovieItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genres=" + genres +
                ", providers=" + providers +
                ", poster_path='" + poster_path + '\'' +
                ", overview='" + overview + '\'' +
                ", trailerID='" + trailerID + '\'' +
                ", release_date='" + release_date + '\'' +
                ", runtime=" + runtime +
                ", vote_average=" + vote_average +
                ", tagline='" + tagline + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<WatchProviders.Provider> getProviders() {
        return providers;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }



} //end class
