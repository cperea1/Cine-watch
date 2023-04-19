package com.example.cinewatch20.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cinewatch20.AppExecutors;
import com.example.cinewatch20.models.MovieModel;
import com.example.cinewatch20.response.MovieSearchResponse;
import com.example.cinewatch20.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    //Live Data
    private MutableLiveData<List<MovieModel>> mMovies;

    private static MovieApiClient instance;

    private MovieQueryRunnable movieQueryRunnable;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        } //end if
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
    } //end constructor

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }

    public void setmMovies(List<MovieModel> m) {
        this.mMovies.postValue(m);
    }

    // This Method will be used ...
    public void searchMoviesApi(String query, int pageNumber) {

        if (movieQueryRunnable != null) movieQueryRunnable = null;

        movieQueryRunnable = new MovieQueryRunnable(query, "search by name", pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(movieQueryRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelling
                myHandler.cancel(true);

                return;
            }
        },3000, TimeUnit.MILLISECONDS);

    } //end SMA

    public void getPopular() {

        if (movieQueryRunnable != null) movieQueryRunnable = null;

        movieQueryRunnable = new MovieQueryRunnable( "popular");

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(movieQueryRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelling
                myHandler.cancel(true);

                return;
            }
        },3000, TimeUnit.MILLISECONDS);

    } //end getPopular




    private class MovieQueryRunnable implements Runnable {

        private String query;
        private String queryType;
        private int pageNumber;
        boolean cancelRequest;

        private int movie_id;

        public MovieQueryRunnable(String query, String queryType, int pageNumber) {
            this.query = query;
            this.queryType = queryType;
            this.pageNumber = pageNumber;
            this.cancelRequest = false;
        }

        public MovieQueryRunnable(String queryType, int movie_id) {
            this.queryType = queryType;
            this.movie_id = movie_id;
            this.cancelRequest = false;
        }

        public MovieQueryRunnable(String queryType) {
            this.queryType = queryType;
            this.cancelRequest = false;
        }

        //use switch statement to allow for different types of queries while only using one runnable object


        @Override
        public void run() {
            switch (queryType) {
                case "search by id":

                    try {
                        Response response =  getMovies(movie_id).execute();
                        if (cancelRequest) {
                            return;
                        } //end if

                        if (response.code() == 200) {
                            List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber == 1) {
                        mMovies.postValue(list);
                    } //end if
                    else {
                        List<MovieModel> currentMovies  = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    } //end else
                        } //end if
                        else {
                            String error = response.errorBody().string();
                            Log.v("Tag", "Error " + error);
                            mMovies.postValue(null);
                        } //end else

                    } catch (IOException e) {
                        e.printStackTrace();
                        mMovies.postValue(null);
                    }
                    break;

                case "search by name":
                    try {
                        Response response =  searchMovies(query, pageNumber).execute();
                        if (cancelRequest) {
                            return;
                        } //end if

                        if (response.code() == 200) {
                            List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());

                            if (pageNumber == 1) {
                                mMovies.postValue(list);
                            } //end if
                            else {
                                List<MovieModel> currentMovies  = mMovies.getValue();
                                currentMovies.addAll(list);
                                mMovies.postValue(currentMovies);
                            } //end else
                        } //end if
                        else {
                            String error = response.errorBody().string();
                            Log.v("Tag", "Error " + error);
                            mMovies.postValue(null);
                        } //end else

                    } catch (IOException e) {
                        e.printStackTrace();
                        mMovies.postValue(null);
                    }


                    break;
                case "popular":

                    try {
                        Response response =  getPopularMovies().execute();
                        if (cancelRequest) {
                            return;
                        } //end if

                        if (response.code() == 200) {
                            List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber == 1) {
                        mMovies.postValue(list);
                    } //end if
                    else {
                        List<MovieModel> currentMovies  = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    } //end else
                        } //end if
                        else {
                            String error = response.errorBody().string();
                            Log.v("Tag", "Error " + error);
                            mMovies.postValue(null);
                        } //end else

                    } catch (IOException e) {
                        e.printStackTrace();
                        mMovies.postValue(null);
                    }


                    break;
            }






            if (cancelRequest) {
                return;
            } //end if
        } //end run

        private Call<MovieSearchResponse> searchMovies(String query, int pageNumber) {
            return Service.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );

        } //end getMovies

        private Call<MovieModel> getMovies(int movie_id) {
            return Service.getMovieApi().getMovie(
                    movie_id,
                    Credentials.API_KEY
            );

        } //end getMovies

        private Call<MovieSearchResponse> getPopularMovies() {
            return Service.getMovieApi().getPopularMovie(
                    Credentials.API_KEY
            );

        } //end getMovies





        private void cancelRequest() {
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        } //end Cancel Request






    } //end class









} //end class
