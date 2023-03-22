package com.example.cinewatch;

import android.content.Context;
import android.widget.Toast;

import com.example.cinewatch.Models.APIResponse;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    Context context;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/search/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getMovie(OnFetchDataListener listener, String movie) {
        CallMDB callMDB = retrofit.create(CallMDB.class);
        Call <APIResponse> call = callMDB.callMovie(movie);

        try {
            call.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(context, "Error!!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    listener.onFetchData(response.body(), response.message());
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    listener.onError("Request Failed!!");

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Ann error occurred!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public interface CallMDB {
        @GET("movie?api_key=753907c72fe168057c05612c733142a3")
        Call<APIResponse> callMovie(@Query("movie") String movie);


    }
}
