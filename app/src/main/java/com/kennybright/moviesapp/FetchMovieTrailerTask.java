package com.kennybright.moviesapp;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;

import java.net.URL;
import java.util.List;
import com.kennybright.utilities.MovieJsonUtils;

class FetchMovieTrailerTask  extends AsyncTask<String,Void, String> {


   Context _context;

   public interface OnUpdateListener {
       public void onUpdate(String key);
   }

    OnUpdateListener listener;
    public FetchMovieTrailerTask (Context context, OnUpdateListener listener)
    {
        _context = context;
        this.listener = listener;
    }
    @Override
    protected String doInBackground(String... params) {
        if (params.length == 0)
        { return null; }

        String itemId = params[0];
        URL moviesDbRequestUrl = com.kennybright.utilities.NetworkUtils.buildItemUrl(itemId,"videos");

        try {
            String response = com.kennybright.utilities.NetworkUtils
                    .getResponseFromHttpUrl(moviesDbRequestUrl);


            String trailerKey = MovieJsonUtils.getMovieTrailerFromJson(_context, response);
           return  trailerKey;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String key) {
       // super.onPostExecute(s);
       String   movieTrailerKey = key;
       if (listener != null )
       {
           listener.onUpdate(movieTrailerKey);
       }


    }


}
