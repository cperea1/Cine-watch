package com.example.cinewatch20.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.cinewatch20.data.Config;
import com.example.cinewatch20.data.FileUtil;
import com.example.cinewatch20.data.MovieItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MovieJsonUpdater {

    private static final String JSON_FILE_PATH = "sorted_movie_vocab.json";
    private static List<JSONMovie> movies = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void addMovieToJSON(List<MovieItem> movieItems, Context context, Config config) {
        try {
            if (movies.size() == 0) {
                // Read the existing JSON file
                String jsonContent = readFileContent(context.getAssets());

                // Parse the JSON data into a list of movies
                parseMoviesFromJson(jsonContent);
            }

            // Add the new movie to the list
            movies.addAll(MovieItemToJSONMovie(movieItems));

            // Convert the list of movies back into JSON format
            String updatedJsonContent = convertMoviesToJson(movies);

            // Write the updated JSON data back to the file
            writeToFile(updatedJsonContent, context);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private static String readFileContent(AssetManager assetManager) throws IOException {
        InputStream inputStream = assetManager.open("sorted_movie_vocab.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }

    private static String convertMoviesToJson(List<JSONMovie> movies) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (JSONMovie movie : movies) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", movie.getTitle());
            jsonObject.put("id", movie.getId());
            jsonObject.put("genres", new JSONArray(movie.getGenres()));
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    private static void parseMoviesFromJson(String jsonContent) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonContent);
        movies.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String title = jsonObject.optString("title");
            int id = jsonObject.optInt("id");
            JSONArray genresArray = jsonObject.optJSONArray("genres");
            String[] genres = new String[0];

            if (genresArray != null) {
                genres = new String[genresArray.length()];
                for (int j = 0; j < genresArray.length(); j++) {
                    genres[j] = genresArray.optString(j);
                }
            }
            movies.add(new JSONMovie(title, id, genres));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void writeToFile(String content, Context context) throws IOException {
        FileOutputStream outputStream = context.openFileOutput("movie.json", Context.MODE_PRIVATE);
        outputStream.write(content.getBytes());
        outputStream.close();
    }


    private static List<JSONMovie> MovieItemToJSONMovie(List<MovieItem> movieItems) {
        List<JSONMovie> jsonMovies = new ArrayList<>();
        for (MovieItem movieItem : movieItems) {
            jsonMovies.add(new JSONMovie(movieItem.getTitle(), movieItem.getId(), movieItem.getGenres().toArray(new String[0])));
        }
        return jsonMovies;
    }

    public static class JSONMovie {
        private String title;
        private int id;
        private String[] genres;

        public JSONMovie(String title, int id, String[] genres) {
            this.title = title;
            this.id = id;
            this.genres = genres;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String[] getGenres() {
            return genres;
        }

        public void setGenres(String[] genres) {
            this.genres = genres;
        }
    }
}
