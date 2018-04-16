
package com.kennybright.utilities;


import android.content.ContentValues;
import android.content.Context;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.kennybright.moviesapp.model.Movie;
/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class MovieJsonUtils {


    public static List<Movie> getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {

        /* Weather information. Each day's forecast info is an element of the "list" array */
        final String OWM_LIST = "list";

        /* All temperatures are children of the "temp" object */
        final String OWM_TEMPERATURE = "temp";

        /* Max temperature for the day */
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";

        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";

        final String OWM_MESSAGE_CODE = "cod";

        /* String array to hold each day's weather String */
        //String[] parsedMovieData = null;
        List<Movie> movieList = new ArrayList<>();
        JSONObject movieJson = new JSONObject(movieJsonStr);

        /* Is there an error? */
        if (movieJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray moviesArray = movieJson.getJSONArray("results");


        // parsedMovieData = new String[moviesArray.length()];
        // parsedMovieData = toStringArray(moviesArray);
        movieList = JsonMovieArrayToList(moviesArray);
        return movieList;

    }


    public static String getMovieTrailerFromJson(Context context, String movieTrailerJsonStr)
            throws JSONException {

        /* Weather information. Each day's forecast info is an element of the "list" array */
        final String OWM_LIST = "list";

        /* All temperatures are children of the "temp" object */
        final String OWM_TEMPERATURE = "temp";

        /* Max temperature for the day */
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";

        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";

        final String OWM_MESSAGE_CODE = "cod";

        /* String array to hold each day's weather String */
        //String[] parsedMovieData = null;

        JSONObject movieJson = new JSONObject(movieTrailerJsonStr);

        /* Is there an error? */
        if (movieJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = movieJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray trailerResult = movieJson.getJSONArray("results");
        JSONObject jsonObj = trailerResult.getJSONObject(0);
        String trailerKey = jsonObj.getString("key");

        // parsedMovieData = new String[moviesArray.length()];
        // parsedMovieData = toStringArray(moviesArray);

        return trailerKey;

    }

    private static List<Movie> JsonMovieArrayToList(JSONArray moviesJsonArray) {
        List<Movie> movieList = new ArrayList<>();

        if(moviesJsonArray==null)
            return null;


        for (int i=0; i< moviesJsonArray.length(); i++ )
        {
            try {
                JSONObject obj=   (JSONObject)moviesJsonArray.get(i);
                Movie movie = new Movie();
                movie.setVote_average( Double.parseDouble(obj.getString("vote_average")));
                movie.setTitle( obj.getString("title"));
                movie.setPoster_path( obj.getString("poster_path"));
                movie.setOverview(obj.getString("overview"));
                movie.setRelease_date(Date.valueOf( obj.getString("release_date")));
                movie.setPopuplarity(Double.parseDouble(obj.getString("popularity")));
                movie.setMovie_id(Integer.parseInt(obj.getString("id")));
                movieList.add(movie);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movieList;
    }
}