package com.example.cinewatch20.client;

import android.content.Context;
import android.util.Log;

import androidx.annotation.WorkerThread;

import com.example.cinewatch20.data.Config;
import com.example.cinewatch20.data.FileUtil;
import com.example.cinewatch20.data.MovieItem;
import com.example.cinewatch20.data.Result;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Interface to load TfLite model and provide recommendations.
 */
public class RecommendationClient implements Serializable {
    private static final String TAG = "Client.RecommendationClient";
    final Map<Integer, MovieItem> candidates = new HashMap<>();
    final Map<String, Integer> genres = new HashMap<>();
    private final Context context;
    private final Config config;
    private Interpreter tflite;


    public RecommendationClient(Context context, Config config) {
        this.context = context;
        this.config = config;

        if (!config.validate()) {
            Log.e(TAG, "data.Config is not valid.");
        }
    }

    /**
     * Load the TF Lite model and dictionary.
     */
    @WorkerThread
    public void load() {
        loadModel();
        loadCandidateList();
        if (config.useGenres()) {
            loadGenreList();
        }
    }

    /**
     * Load TF Lite model.
     */
    @WorkerThread
    private synchronized void loadModel() {
        try {
            ByteBuffer buffer = FileUtil.loadModelFile(this.context.getAssets(), config.model);
            tflite = new Interpreter(buffer);
            Log.v(TAG, "TFLite model loaded.");
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * Load recommendation candidate list.
     */
    @WorkerThread
    private synchronized void loadCandidateList() {
        try {
            Collection<MovieItem> collection =
                    FileUtil.loadMovieList(this.context.getAssets(), config.movieList);
            candidates.clear();
            for (MovieItem item : collection) {
                //Log.d(TAG, String.format("Load candidate: %s", item));
                candidates.put(item.getId(), item);
            }
            Log.v(TAG, "Candidate list loaded.");
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * Load movie genre list.
     */
    @WorkerThread
    private synchronized void loadGenreList() {
        try {
            List<String> genreList = FileUtil.loadGenreList(this.context.getAssets(), config.genreList);
            genres.clear();
            for (String genre : genreList) {
                genres.put(genre, genres.size());
            }
            Log.v(TAG, "Candidate list loaded.");
        } catch (IOException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    /**
     * Free up resources as the client is no longer needed.
     */
    @WorkerThread
    public synchronized void unload() {
        tflite.close();
        candidates.clear();
    }

    int[] preprocessIds(List<MovieItem> selectedMovies, int length) {
        int[] inputIds = new int[length];
        Arrays.fill(inputIds, config.pad); // Fill inputIds with the default.
        int i = 0;
        for (MovieItem item : selectedMovies) {
            if (i >= inputIds.length) {
                break;
            }
            inputIds[i] = item.getId();

            i++;
        }
        return inputIds;
    }

    int[] preprocessGenres(List<MovieItem> selectedMovies, int length) {
        // Fill inputGenres.
        int[] inputGenres = new int[length];
        Arrays.fill(inputGenres, config.unknownGenre); // Fill inputGenres with the default.
        int i = 0;
        for (MovieItem item : selectedMovies) {
            if (i >= inputGenres.length) {
                break;
            }
            for (String genre : item.getGenres()) {
                if (i >= inputGenres.length) {
                    break;
                }
                inputGenres[i] = genres.containsKey(genre) ? genres.get(genre) : config.unknownGenre;
                ++i;
            }
        }
        return inputGenres;
    }

    /**
     * Given a list of selected items, preprocess to get tflite input.
     */
    @WorkerThread
    synchronized Object[] preprocess(List<MovieItem> selectedMovies) {
        List<Object> inputs = new ArrayList<>();

        // Sort features.
        List<Config.Feature> sortedFeatures = new ArrayList<>(config.inputs);
        Collections.sort(sortedFeatures, Comparator.comparingInt((Config.Feature a) -> a.index));

        for (Config.Feature feature : sortedFeatures) {
            if (Config.FEATURE_MOVIE.equals(feature.name)) {
                inputs.add(preprocessIds(selectedMovies, feature.inputLength));
            } else if (Config.FEATURE_GENRE.equals(feature.name)) {
                inputs.add(preprocessGenres(selectedMovies, feature.inputLength));
            } else {
                Log.e(TAG, String.format("Invalid feature: %s", feature.name));
            }
        }
        return inputs.toArray();
    }

    /**
     * Postprocess to get results from tflite inference.
     */
    @WorkerThread
    synchronized List<Result> postProcess(
            int[] outputIds, float[] confidences, List<MovieItem> selectedMovies) {
        final ArrayList<Result> results = new ArrayList<>();

        // Add recommendation results. Filter null or contained items.
        for (int i = 0; i < outputIds.length; i++) {
            if (results.size() >= config.topK) {
                Log.d(TAG, String.format("Selected top K: %d. Ignore the rest.", config.topK));
                break;
            }

            int id = outputIds[i];
            MovieItem item = candidates.get(id);
            if (item == null) {
                Log.d(TAG, String.format("Inference output[%d]. Id: %s is null", i, id));
                continue;
            }
            if (selectedMovies.contains(item)) {
                Log.d(TAG, String.format("Inference output[%d]. Id: %s is contained", i, id));
                continue;
            }
            Result result = new Result(id, item, confidences[i]);
            results.add(result);
            //Log.v(TAG, String.format("Inference output[%d]. Result: %s", i, result));
        }

        return results;
    }

    /**
     * Given a list of selected items, and returns the recommendation results.
     */
    @WorkerThread
    public synchronized List<Result> recommend(List<MovieItem> likedMovies) {
        Object[] inputs = preprocess(likedMovies);

        // Run inference.
        int[] outputIds = new int[config.outputLength];
        float[] confidences = new float[config.outputLength];
        Map<Integer, Object> outputs = new HashMap<>();
        outputs.put(config.outputIdsIndex, outputIds);
        outputs.put(config.outputScoresIndex, confidences);
        tflite.runForMultipleInputsOutputs(inputs, outputs);

        return postProcess(outputIds, confidences, likedMovies);
    }

    Interpreter getTflite() {
        return this.tflite;
    }
}
