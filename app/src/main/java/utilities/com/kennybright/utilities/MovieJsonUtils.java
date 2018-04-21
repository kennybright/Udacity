
package com.kennybright.utilities;


import android.content.Context;

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
@SuppressWarnings("unused")
public final class MovieJsonUtils {


    public static List<Movie> getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {



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


    public static List<String> getMovieTrailerFromJson(Context context, String movieTrailerJsonStr)
            throws JSONException {


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

        List<String> trailerList;
        JSONArray trailerResult = movieJson.getJSONArray("results");
        //JSONObject jsonObj = trailerResult.getJSONObject(0);
        //String trailerKey = jsonObj.getString("key");

        // parsedMovieData = new String[moviesArray.length()];
        // parsedMovieData = toStringArray(moviesArray);
       trailerList = JsonTrailerArrayToList(trailerResult);
        return trailerList;

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

    private static List<String> JsonTrailerArrayToList(JSONArray trailerJsonArray) {
        List<String> trailerList = new ArrayList<>();
        String key;
        if(trailerJsonArray==null)
            return null;


        for (int i=0; i< trailerJsonArray.length(); i++ )
        {
            try {
                JSONObject obj=   (JSONObject)trailerJsonArray.get(i);

              key =   obj.getString("key");

                trailerList.add(key);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return trailerList;
    }
}