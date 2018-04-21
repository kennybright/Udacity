package com.kennybright.utilities;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;


public class NetworkUtils {


    public static final int STATUS_CONNECTED = 0 ;

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String STATIC_MOVIEDB_URL =
            "http://api.themoviedb.org/3/movie/";

    private static final String MOVIEDB_BASE_URL = STATIC_MOVIEDB_URL;

    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API.If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this course.
     */


    private final static String key = "7c687905b3ae257936322d1808797e4f";

    private final static String KEY_PARAM = "api_key";

    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param query request type.
     * @return The URL to use to query the movie db server.
     */
    public static URL buildUrl(String query) {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()

                .appendPath(query)
                .appendQueryParameter(KEY_PARAM, key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    // String request = "http://api.themoviedb.org/3/movie/550/videos?api_key=###";
    public static URL buildItemUrl(String itemId,String path)
    {
        Uri builtUri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()

                .appendPath(itemId)
                .appendPath(path)
                .appendQueryParameter(KEY_PARAM, key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Item Uri " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return Objects.requireNonNull(connectivityManager).getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
