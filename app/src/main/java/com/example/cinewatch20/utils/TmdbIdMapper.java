package com.example.cinewatch20.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class TmdbIdMapper {

    private HashMap<Integer, Integer> tmdbMap = new HashMap<>();;
    private static final TmdbIdMapper instance = new TmdbIdMapper();
    private final String fileName = "tmdb-map.csv";
    private final String TAG = "CineWatch-TmdbIdMapper";

    private TmdbIdMapper() { }

    public static TmdbIdMapper getInstance() {
        return instance;
    }

    public int getTmdbId(Context context, int movieDbId) {

        if(tmdbMap.size() == 0){
            loadCsv(context);
        }

        return tmdbMap.getOrDefault(movieDbId, -1);
    }

    public void loadCsv(Context context){
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            String csvSplitBy = ",";
            br.readLine(); //gets past the first example line

            while ((line = br.readLine()) != null) {
                String[] row = line.split(csvSplitBy);
                if(row.length != 2){
                    continue;
                }
                tmdbMap.put(Integer.parseInt(row[0]), Integer.parseInt(row[1]));
            }

            Log.i(TAG, "Completed loading csv file");
        }
        catch (Exception e){
            Log.i(TAG, "Unable to read mapping file");
            e.printStackTrace();
        }
    }
}
