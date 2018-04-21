package com.kennybright.moviesapp;

import android.content.Context;
import android.os.AsyncTask;

import java.net.URL;
import java.util.List;
import com.kennybright.utilities.MovieJsonUtils;

 class FetchMovieTrailerTask  extends AsyncTask<String,Void, List<String>> {


   private final Context _context;
   private final OnUpdateListener listener;
   public interface OnUpdateListener {
       void onUpdate(List<String> key);
   }


    public FetchMovieTrailerTask (Context context, OnUpdateListener listener)
    {
        _context = context;
        this.listener = listener;
    }
    @Override
    protected List<String> doInBackground(String... params) {
        if (params.length == 0)
        { return null; }

        String itemId = params[0];
        URL moviesDbRequestUrl = com.kennybright.utilities.NetworkUtils.buildItemUrl(itemId,"videos");

        try {
            String response = com.kennybright.utilities.NetworkUtils
                    .getResponseFromHttpUrl(moviesDbRequestUrl);


        //    String trailerKey = MovieJsonUtils.getMovieTrailerFromJson(_context, response);
            return MovieJsonUtils.getMovieTrailerFromJson(_context,response);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<String> keys) {
       // super.onPostExecute(s);
       //String   movieTrailerKey = key;
       if (listener != null )
       {
           listener.onUpdate(keys);
       }


    }


}
