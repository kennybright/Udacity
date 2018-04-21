package com.kennybright.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.kennybright.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.kennybright.moviesapp.model.Movie;
import com.kennybright.utilities.MovieJsonUtils;
import com.kennybright.moviesapp.MoviePosterAdapter.MovieAdapterOnClickHandler;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler{


    private static final String TAG = MainActivity.class.getSimpleName();

    private MoviePosterAdapter mMoviePosterAdapter;

    private SharedPreferences sPreferences;

    private String sort;
    private boolean choice;
    private String category;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceListener;

    private final List<Movie> favoriteMovies = new ArrayList<>();
    private static final int MOVIE_DETAIL_VIEW = 999;
    //private String MOVIE_DATA = "top_rated";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        registerSharedPreference();
        readSharedPreference();


        RecyclerView mRecyclerView = findViewById(R.id.recyclerview_movies);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,4);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        // create adapter and set recyclerview adapter
        mMoviePosterAdapter = new MoviePosterAdapter( MainActivity.this, this );
        // attach adapter to recyclerView
        mRecyclerView.setAdapter(mMoviePosterAdapter);

        checkConnection();
        loadMoviesData(category);



    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putBoolean("choice_switch", choice);

        editor.apply();
        editor.commit();
       sPreferences.unregisterOnSharedPreferenceChangeListener(preferenceListener);
       sPreferences = null;
    }

    @Override
    protected void onResume() {
        super.onResume();


        readSharedPreference();
        loadMoviesData(category);

    }

    private void readSharedPreference() {
        if (sPreferences == null)
        {
          registerSharedPreference();
        }

        choice =  sPreferences.getBoolean("choice_switch", false);
        category = choice ? "popular": "top_rated";
        sort = sPreferences.getString("poster_pref_sort_order","most popular");
    }

    private boolean isOnline() {


        boolean connected = false;
        try {
            NetworkInfo wifiInfo, mobileInfo;
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;
        }catch (Exception e)
        {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }



    public void checkConnection(){
        if(!isOnline()){

          //    Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        //}else{
            Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
      //  sPreferences.unregisterOnSharedPreferenceChangeListener(preferenceListener);
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();


    }

    private void registerSharedPreference()
    {
        // get default manager
        sPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceListener =  new SharedPreferences.OnSharedPreferenceChangeListener() {

             @Override
             public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                  Log.v(TAG, "Settings key changed:" + key);

                  if (key.equals("choice_switch"))
                  {
                      choice = sPreferences.getBoolean("choice_switch", false);

                  }

                 if (key.equals("poster_pref_sort_order"))
                 {
                     sort = sPreferences.getString("poster_pref_sort_order", "most popular");
                     mMoviePosterAdapter.sortMoviePosters(sort);
                 }

             }
         };
        sPreferences.registerOnSharedPreferenceChangeListener(preferenceListener);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        Intent intent;
        int selectedMenuItem = item.getItemId();
        switch (selectedMenuItem)
        {
            case R.id.action_setting :
                //Toast.makeText(getApplicationContext(), String.valueOf(selectedMenuItem), Toast.LENGTH_LONG).show();
                 intent = new Intent(this, SettingsActivity.class );
                 startActivity(intent);
                break;
                default:
                    break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Movie movie;
        if (requestCode == MOVIE_DETAIL_VIEW) {
            if (resultCode == RESULT_OK) {
              movie = data.getParcelableExtra("favorite_movies");
              favoriteMovies.add(movie);
                Toast toast;
                toast = Toast.makeText(this, movie.getTitle(), Toast.LENGTH_LONG);
                toast.show();
            }

        }
    }

    private void loadMoviesData(String choice) {

        new FetchMovieTask().execute(choice);
    }

    /*
     *  when notification is received from the adapter class with a movie payload
     *  create an activity and send movie information
     * */
    @Override
    public void onClick(Movie movie) {
        // Toast toast;
        // toast = Toast.makeText(this, movie.getTitle(), Toast.LENGTH_LONG);
        // toast.show();

        //picasso.load(ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoId)).resize(px, px).centerCrop().into(imageview);
        Intent intent = new Intent(getApplicationContext(),MovieDetailActivity.class);

        //final Bundle bundle = new Bundle();
        intent.putExtra("movie",movie);

     //   startActivityForResult(intent,MOVIE_DETAIL_VIEW);
        startActivityForResult(intent,MOVIE_DETAIL_VIEW);
       // startActivity(intent);
    }



     class FetchMovieTask extends   AsyncTask<String, Void , List<Movie>> {


        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length == 0)
            { return null; }

            String location = params[0];
            URL moviesDbRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String response = NetworkUtils
                        .getResponseFromHttpUrl(moviesDbRequestUrl);


                return MovieJsonUtils
                        .getSimpleMovieStringsFromJson(MainActivity.this, response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {

            mMoviePosterAdapter.setWeatherData(movieData);
        }
    }



    public static class SettingsActivity extends PreferenceActivity {

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (savedInstanceState == null)
            {
                getFragmentManager().beginTransaction().replace(android.R.id.content,new com.kennybright.moviesapp.MyPreferenceFragment()).commit();
            }
        }
    }
}

