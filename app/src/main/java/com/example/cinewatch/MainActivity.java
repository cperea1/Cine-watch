package com.example.cinewatch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinewatch.Models.APIResponse;

public class MainActivity extends AppCompatActivity {

    SearchView search_view;
    TextView movie_name, overview;
    ImageView poster;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search_view = findViewById(R.id.search_view);
        movie_name = findViewById(R.id.movie_name);
        overview = findViewById(R.id.Overview);
        poster = findViewById(R.id.poster);
        progressDialog = new ProgressDialog(this);

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.setTitle("Fetching response for " + query);
                progressDialog.show();
                RequestManager manager = new RequestManager(MainActivity.this);
                manager.getMovie(listener, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private final OnFetchDataListener listener = new OnFetchDataListener() {
        @Override
        public void onFetchData(APIResponse apiResponse, String message) {
            progressDialog.dismiss();
            if (apiResponse == null) {
                Toast.makeText(MainActivity.this, "No data found!!", Toast.LENGTH_SHORT).show();
                return;
            } //end if
            showData(apiResponse);
        } //end OnFetchData

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    };

    private void showData(APIResponse apiResponse) {
        movie_name.setText(apiResponse.getTitle());
        //also set poster content
        overview.setText(apiResponse.getOverview());
    }
}