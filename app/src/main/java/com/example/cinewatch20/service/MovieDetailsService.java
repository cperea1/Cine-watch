package com.example.cinewatch20.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.cinewatch20.database.DatabaseInstance;
import com.example.cinewatch20.service.model.MovieDetails;
import com.example.cinewatch20.service.model.VideoResults;
import com.example.cinewatch20.utils.MovieDetailsServiceUtil;
import com.example.cinewatch20.utils.TmdbIdMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;



public class MovieDetailsService extends Service {

    private Binder movieDetailsBinder;
    private RequestQueue volleyRequestQueue;
    private final String TAG = "CineWatch--MovieDetailsService";

    public MovieDetailsService() {
        this.movieDetailsBinder = new MovieDetailsBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Create service invoked");
        volleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        TmdbIdMapper.getInstance().loadCsv(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "On start service invoked");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return movieDetailsBinder;
    }

    public void getMoviesDetails(List<Integer> movieIds, MovieDetailsCallback callback){
        Log.d(TAG, "Fetch movie details invoked");
        List<Integer> tmdbIds = movieIds.stream()
                .map(movieId -> TmdbIdMapper.getInstance().getTmdbId(getApplicationContext(), movieId))
                .filter(movieId -> movieId != -1)
                .collect(Collectors.toList());

        List<MovieDetails> moviesInDatabase = new ArrayList<>();
        List<Integer> tmdbIdNotInDatabase = new ArrayList<>();

        DatabaseReference childRef = DatabaseInstance.DATABASE.getReference("movies");
        childRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(int tmdbId: tmdbIds){
                    if(snapshot.hasChild(tmdbId+"")){
                        //Log.d(TAG, "Movie exists in DB. Fetching details for "+tmdbId);
                        MovieDetails md = MovieDetailsServiceUtil.mapDataToMovieDetails(tmdbId, snapshot.child(tmdbId+""));

                        moviesInDatabase.add(md);

                    } else {
                        tmdbIdNotInDatabase.add(tmdbId);
                    }
                }

                if(tmdbIdNotInDatabase.size() > 0)
                    fetchDetailsFromTmdb(tmdbIdNotInDatabase);

                callback.dbMovieDetails(MovieDetailsServiceUtil.DetailsToItem(moviesInDatabase));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "error in reading db: "+error.getMessage());
            }
        });
    }

    public void fetchDetailsFromTmdb(List<Integer> tmdbIds) {
        Log.d(TAG, "fetchDetailsFromTmdb invoked");

        for(int tmdbId : tmdbIds){
            Thread thread = new Thread(() -> {
                String url = MovieDetailsServiceUtil.buildMovieDetailsUrl(tmdbId);
                RequestFuture<MovieDetails> future = RequestFuture.newFuture();
                volleyRequestQueue.add(new VolleyRequestAdapter<>(url, MovieDetails.class, null, future, future));
                try {
                    Log.d(TAG, "Fetching details for tmdb id "+tmdbId+" on thread "+Thread.currentThread().getName());
                    MovieDetails response = future.get(60, TimeUnit.SECONDS);

                    response.setProviders(response.getWatchProviders().getProviderInfoForRegion("US").getFlatrateProviders());
                    Log.i(TAG, "Got response : " + response);
                    insertMovieDetailsInDatabase(response);

                } catch (Exception e) {
                    Log.e(TAG, "unable to call tmdb: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            thread.start(); //just calls the thread from above
        }

        Log.i(TAG, "Updated database with missing tmdb ids");
    }

    private void insertMovieDetailsInDatabase(MovieDetails response){
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

        StringBuilder sb= new StringBuilder();
        response.getGenres().forEach(m -> sb.append(m).append(","));
        if(sb.length()>0)
            sb.delete(sb.length()-1, sb.length());
        childRef.child("genres").setValue(sb.toString());

        String trailerLink = "";
        List<VideoResults> results = response.getVideos().getResults();
        for (int i = 0; i < results.size(); i++){
            String type = results.get(i).getType();
            String site = results.get(i).getSite();
            if (type.equals("Trailer") && site.equals("YouTube")){
                trailerLink = results.get(i).getKey();
                break;
            }
        }
        if (trailerLink.length() > 0) {
            childRef.child("trailer").setValue(trailerLink);
        } else {
            childRef.child("trailer").setValue("");
        }
        Log.i(TAG, "Completed updating DB for tmdb Id "+response.getId());
    }

    public class MovieDetailsBinder extends Binder {

        public MovieDetailsService getService(){
            return MovieDetailsService.this;
        }
    }


}
