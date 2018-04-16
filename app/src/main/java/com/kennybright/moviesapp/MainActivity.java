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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.kennybright.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;
import com.kennybright.moviesapp.model.Movie;
import com.kennybright.utilities.MovieJsonUtils;
import com.kennybright.moviesapp.MoviePosterAdapter.MovieAdapterOnClickHandler;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity implements MovieAdapterOnClickHandler{


    private float mMostPopular = 0;
    private int mTopRated;
    private static final String TOPRATED_KEY= "top rated";
    private static final String MOSTPOPULAR_KEY ="most popular";

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private MoviePosterAdapter mMoviePosterAdapter;

    private SharedPreferences sPreferences;

    private String sharedPrefFile = "com.kennybright.android.movieappssharedprefs";
    private boolean sort;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        readSharedPreference();


        // get reclyerView
       /* mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        // create layout manager and set recycler view's layout manager
       // LinearLayoutManager layoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
       // LinearLayoutManager layoutManager = new LinearLayoutManager( this, LinearLayoutManager.VERTICAL, false);
       // mRecyclerView.setLayoutManager(layoutManager);
        // child layout views are fixed
       // mRecyclerView.setHasFixedSize(true);*/



        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_movies);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
        // create adapter and set recyclerview adapter
        mMoviePosterAdapter = new MoviePosterAdapter( MainActivity.this, this );
        // attach adapter to recyclerView
        mRecyclerView.setAdapter(mMoviePosterAdapter);

      // loadMoviesData();
        // checkConnection();


    }

    @Override
    protected void onResume() {
        super.onResume();
       // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    //    sPreferences.registerOnSharedPreferenceChangeListener(preferenceListener);
        SharedPreferences.Editor editor = sPreferences.edit();
        editor.putBoolean("sort_switch", sort);
        editor.apply();
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnection(){
        if(isOnline()){

            //  Toast.makeText(MainActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
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

    private void readSharedPreference()
    {
        // get sharedPrerences
        sPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceListener =  new SharedPreferences.OnSharedPreferenceChangeListener() {

             @Override
             public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                  Log.v(TAG, "Settings key changed:" + key);

                  if (key.equals("sort_switch"))
                  {
                      sort = sPreferences.getBoolean("sort_switch", false);
                      loadMoviesData();
                  }

             }
         };
        sPreferences.registerOnSharedPreferenceChangeListener(preferenceListener);
      //  sort = prefs.getBoolean("sort_switch", false);
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

    private void loadMoviesData() {
       String data = sort ? "popular": "top_rated";
       // String data = "top_rated";
      //  if (mMoviePosterAdapter._movies != null)
      //      mMoviePosterAdapter.ClearMovieList();
        // String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchMovieTask().execute(data);
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

        final Bundle bundle = new Bundle();
        intent.putExtra("movie",movie);

        startActivity(intent);
    }



    public class FetchMovieTask extends AsyncTask<String, Void , List<Movie>> {


        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length == 0)
            { return null; }

            String location = params[0];
            URL moviesDbRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String response = NetworkUtils
                        .getResponseFromHttpUrl(moviesDbRequestUrl);

                List<Movie> simpleJsonMovieData = MovieJsonUtils
                        .getSimpleMovieStringsFromJson(MainActivity.this, response);


                return simpleJsonMovieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {

            mMoviePosterAdapter.setWeatherData(movieData ,sort);
        }
    }

  /*  @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor prefrencesEditor = mPreferences.edit();
        //most popular, top rated
        prefrencesEditor.putInt(MOSTPOPULAR_KEY, mTopRated );
        prefrencesEditor.putFloat(TOPRATED_KEY,mMostPopular);

        prefrencesEditor.apply();
    }*/


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

