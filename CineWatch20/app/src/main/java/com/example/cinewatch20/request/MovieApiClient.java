package com.example.cinewatch20.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cinewatch20.AppExecutors;
import com.example.cinewatch20.models.MovieModel;
import com.example.cinewatch20.response.MovieSearchResponse;
import com.example.cinewatch20.utils.Credentials;
import com.example.cinewatch20.utils.MovieApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    //Live Data
    private MutableLiveData<List<MovieModel>> mMovies ;

    private static MovieApiClient instance;

    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        } //end if
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
    } //end con

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }




    // This Method will be used ...
    public void searchMoviesApi(String query, int pageNumber) {

        if (retrieveMoviesRunnable != null) retrieveMoviesRunnable = null;

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelling
                myHandler.cancel(true);

                return;
            }
        },3000, TimeUnit.MILLISECONDS);

    } //end SMA

    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {


            try {
                Response response =  getMovies(query, pageNumber).execute();
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


            if (cancelRequest) {
                return;
            } //end if
        } //end run

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {
            return Service.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );

        } //end getMovies

        private void cancelRequest() {
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        } //end Cancel Request





    } //end class









} //end class
