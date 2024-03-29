
package com.example.cinewatch20.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * data.Config for recommendation app.
 */
public final class Config {
    public static final String FEATURE_MOVIE = "movieFeature";
    public static final String FEATURE_GENRE = "genreFeature";
    private static final String TAG = "CinemaFreak-data.Config";
    private static final String DEFAULT_MODEL_PATH = "recommendation_rnn_i10o100.tflite";
    private static final String DEFAULT_MOVIE_LIST_PATH = "sorted_movie_vocab.json";
    private static final String DEFAULT_MOVIE_SELECTION_LIST_PATH = "selected_movie_vocab.json";
    private static final int DEFAULT_OUTPUT_LENGTH = 100;
    private static final int DEFAULT_TOP_K = 100;
    private static final int PAD_ID = 0;
    private static final int UNKNOWN_GENRE = 0;
    private static final int DEFAULT_OUTPUT_IDS_INDEX = 0;
    private static final int DEFAULT_OUTPUT_SCORES_INDEX = 1;
    private static final int DEFAULT_FAVORITE_LIST_SIZE = 100;
    /**
     * TF Lite model path.
     */
    public String model = DEFAULT_MODEL_PATH;
    /**
     * List of input features
     */
    public List<Feature> inputs = new ArrayList<>();
    /**
     * Number of output length from the model.
     */
    public int outputLength = DEFAULT_OUTPUT_LENGTH;
    /**
     * Number of max results to show in the UI.
     */
    public int topK = DEFAULT_TOP_K;
    /**
     * Path to the movie list.
     */
    public String movieList = DEFAULT_MOVIE_LIST_PATH;
    /**
     * Path to the genre list. Use genre feature if it is not null.
     */
    public String genreList = null;
    /**
     * Id for padding.
     */
    public int pad = PAD_ID;
    /**
     * Movie genre for unknown.
     */
    public int unknownGenre = UNKNOWN_GENRE;
    /**
     * Output index for ID.
     */
    public int outputIdsIndex = DEFAULT_OUTPUT_IDS_INDEX;
    /**
     * Output index for score.
     */
    public int outputScoresIndex = DEFAULT_OUTPUT_SCORES_INDEX;
    /**
     * The number of favorite movies for users to choose from.
     */
    public int favoriteListSize = DEFAULT_FAVORITE_LIST_SIZE;
    /**
     * The movies to select from
     */
    public String movieSelectionList = DEFAULT_MOVIE_SELECTION_LIST_PATH;

    public Config() {
    }

    public boolean validate() {
        if (inputs.isEmpty()) {
            Log.e(TAG, "config inputs should not be empty");
            return false;
        }

        boolean hasGenreFeature = false;
        for (Feature feature : inputs) {
            if (FEATURE_GENRE.equals(feature.name)) {
                hasGenreFeature = true;
                break;
            }
        }
        if (useGenres() || hasGenreFeature) {
            if (!useGenres() || !hasGenreFeature) {
                String msg =
                        "If uses genre, must set both `genreFeature` in inputs and `genreList` as vocab.";
                if (!useGenres()) {
                    msg += "`genreList` is missing.";
                }
                if (!hasGenreFeature) {
                    msg += "`genreFeature` is missing.";
                }
                Log.e(TAG, msg);
                return false;
            }
        }

        return true;
    }

    public boolean useGenres() {
        return genreList != null;
    }

    /**
     * Feature of the model.
     */
    public static class Feature {
        /**
         * Input feature name.
         */
        public String name;
        /**
         * Input feature index.
         */
        public int index;
        /**
         * Input feature length.
         */
        public int inputLength;
    }
}
