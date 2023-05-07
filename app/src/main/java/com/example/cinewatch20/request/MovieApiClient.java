package com.example.cinewatch20.request;

import android.os.Build;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cinewatch20.AppExecutors;
import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.database.DatabaseInstance;
import com.example.cinewatch20.response.MovieResponse;
import com.example.cinewatch20.response.MovieSearchResponse;
import com.example.cinewatch20.service.MovieDetailsService;
import com.example.cinewatch20.utils.Credentials;
import com.example.cinewatch20.utils.MovieJsonUpdater;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    MovieDetailsService service;
    //Live Data
    private MutableLiveData<List<MovieItem>> mMovies;

    private static MovieApiClient instance;

    private MovieQueryRunnable movieQueryRunnable;
    private final String TAG = "CineWatch - MoveApiClient";

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        } //end if
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
    } //end constructor

    public LiveData<List<MovieItem>> getMovies() {
        return mMovies;
    }

    public void setmMovies(List<MovieItem> m) {
        this.mMovies.postValue(m);
    }

    // This Method will be used ...
    public void searchMoviesApi(String query, int pageNumber) {

        if (movieQueryRunnable != null) movieQueryRunnable = null;

        movieQueryRunnable = new MovieQueryRunnable(query, "search by name", pageNumber);

        final Future<?> myHandler = AppExecutors.getInstance().networkIO().submit(movieQueryRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelling
                myHandler.cancel(true);

            }
        },3000, TimeUnit.MILLISECONDS);

    } //end SMA

    public void getPopular() {

        if (movieQueryRunnable != null) movieQueryRunnable = null;

        movieQueryRunnable = new MovieQueryRunnable( "popular");

        final Future<?> myHandler = AppExecutors.getInstance().networkIO().submit(movieQueryRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelling
                myHandler.cancel(true);

            }
        },3000, TimeUnit.MILLISECONDS);

    } //end getPopular

    public void searchByID(int id) {

        if (movieQueryRunnable != null) movieQueryRunnable = null;

        movieQueryRunnable = new MovieQueryRunnable( "search by id", id);

        final Future<?> myHandler = AppExecutors.getInstance().networkIO().submit(movieQueryRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelling
                myHandler.cancel(true);

            }
        },3000, TimeUnit.MILLISECONDS);

    } //end getPopular




    private class MovieQueryRunnable implements Runnable {

        private String query;
        private final String queryType;
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
            pageNumber = 1;
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
                        Response<MovieResponse> response =  getMovies(movie_id).execute();
                        if (cancelRequest) {
                            return;
                        } //end if

                        if (response.code() == 200) { //200 means it worked
                            if (response.body() != null) {
                                //InfoPage.movieItem = ((MovieResponse) response.body()).getMovie();
                            }
                        } //end if
                        else {
                            String error = null;
                            if (response.errorBody() != null) {
                                error = response.errorBody().string();
                            }
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
                        Response<MovieSearchResponse> response =  searchMovies(query, pageNumber).execute();
                        if (cancelRequest) {
                            return;
                        } //end if

                        Log.v("Response Code: ", response.code() + "");
                        if (response.code() == 200) { //200 means it worked
                            List<MovieItem> list = null;
                            if (response.body() != null) {
                                list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                            }

                            if (pageNumber == 1) {
                                mMovies.postValue(list);
                            } //end if
                            else {
                                List<MovieItem> currentMovies  = mMovies.getValue();
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
                        Response<MovieSearchResponse> response =  getPopularMovies().execute();
                        if (cancelRequest) {
                            return;
                        } //end if

                        if (response.code() == 200) {
                            Log.v("TAG", "Response code = 200");
                            List<MovieItem> list = null;
                            if (response.body() != null) {
                                list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                            }


                            List<Integer> tmdbids = new ArrayList<>();
                            if (list != null) {
                                for (MovieItem item : list) {
                                    insertMovieDetailsInDatabase(item);
                                } //end for
                            } //end if


                            mMovies.postValue(list);
                        } //end if
                        else {
                            String error = null;
                            if (response.errorBody() != null) {
                                error = response.errorBody().string();
                            }
                            Log.v("Tag", "Error " + error);
                            mMovies.postValue(null);
                        } //end else

                    } catch (IOException e) {
                        e.printStackTrace();
                        mMovies.postValue(null);
                    }


                    break;
            } //end switch

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

        private Call<MovieResponse> getMovies(int movie_id) {
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
    private void insertMovieDetailsInDatabase(MovieItem response){
        DatabaseReference childRef = DatabaseInstance.DATABASE.getReference("movies").child(response.getId()+"");
        childRef.child("overview").setValue(response.getOverview());
        childRef.child("title").setValue(response.getTitle());
        childRef.child("poster_path").setValue(response.getPoster_path());
        childRef.child("backdrop_path").setValue(response.getBackdrop_path());
        childRef.child("likes").setValue(0);
        childRef.child("dislikes").setValue(0);
        childRef.child("providers").setValue(response.getProviders());
        childRef.child("runtime").setValue(response.getRuntime());
        childRef.child("vote_average").setValue(response.getVote_average());
        childRef.child("release_date").setValue(response.getRelease_date());
        childRef.child("tagline").setValue(response.getTagline());
        childRef.child("trailer").setValue(response.getTrailerID());

        StringBuilder sb= new StringBuilder();
        response.getGenres().forEach(m -> sb.append(m).append(","));
        if(sb.length()>0)
            sb.delete(sb.length()-1, sb.length());
        childRef.child("genres").setValue(sb.toString());


        Log.i(TAG, "Completed updating DB for tmdb Id "+response.getId());
    }
} //end class
