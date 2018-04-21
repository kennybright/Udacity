package com.kennybright.moviesapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kennybright.moviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity  {


    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private List<String> trailerKey = null;
    private RatingBar ratingBar;
    private  Movie movie;
    FetchMovieTrailerTask fetchMovieTrailerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        // movie = (Movie) intent.getSerializableExtra("movie");
        movie = intent.getParcelableExtra("movie");

        TextView mvTitle = findViewById(R.id.mv_title);
        TextView mvOverview = findViewById(R.id.mv_overview);
        mvOverview.setMovementMethod(new ScrollingMovementMethod());
        TextView mvRatings = findViewById(R.id.mv_ratings);
        TextView mvReleaseDate = findViewById(R.id.mv_releasedate);
        ImageView mvImageView = findViewById(R.id.mv_poster_thumbnail);

        mvTitle.setText(movie.getTitle());
        mvOverview.setText(movie.getOverview());
        String ratings = getString(R.string.ratings_format) + String.valueOf(movie.getVote_average());
        mvRatings.setText(ratings);
        ImageView mvtrailerImage1 = findViewById(R.id.mv_trailer_image1);
        ImageView mvtrailerImage2 = findViewById(R.id.mv_trailer_image2);

        String stringDate = DateFormat.getDateInstance().format(movie.getRelease_date());
        mvReleaseDate.setText(stringDate);

        String baseImageUri = "http://image.tmdb.org/t/p/w185";
        String poster_path = movie.getPoster_path();
        String absPath = baseImageUri.concat(poster_path);
        Picasso.with(getApplicationContext()).load(absPath).into(mvImageView);


        AddListenerOnClickBt();
        addListenerOnRatingBar();
        final Context  context = this;

        mvtrailerImage1.setOnClickListener(new TrailerButtonClick());
        mvtrailerImage2.setOnClickListener(new TrailerButtonClick());
        LoadMovieTrailers();
        //http://api.themoviedb.org/3/movie/550/videos?api_key=###
        //https://www.youtube.com/watch?v=SUXWAEX2jlg
        // register for
      /*  View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Click event fired" );

                String trailerId = String.valueOf(movie.getMovie_id());

                     // context parameter is use to fetch data
                     // listener is use to harvest trailer key ; we need to pass the trailer back from the task
                     // we use an interface declared in the task and implemented by our listener for communication
                     new FetchMovieTrailerTask(context, new FetchMovieTrailerTask.OnUpdateListener() {
                         @Override
                         public void onUpdate(List<String> key) {

                                 trailerKey = key;
                                 Intent intent = new Intent(Intent.ACTION_VIEW);
                                 intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailerKey.get(0)));
                                 intent.setPackage("com.google.android.youtube");
                                 startActivity(intent);

                         }
                     }).execute(trailerId);


            }


        };
        mvImageView.setOnClickListener(onClickListener);  */


    }

    private void LoadMovieTrailers() {
        String trailerId = String.valueOf(movie.getMovie_id());

        new FetchMovieTrailerTask(this, new FetchMovieTrailerTask.OnUpdateListener() {
            @Override
            public void onUpdate(List<String> key) {

                trailerKey = key;
               /* Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailerKey.get(0)));
                intent.setPackage("com.google.android.youtube");
                startActivity(intent);*/

            }
        }).execute(trailerId);
    }

    private void AddListenerOnClickBt()
    {
        ratingBar = findViewById(R.id.ratingBar);
        Button ratingButton = findViewById(R.id.ratingBtn);
        // add listener to button
        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting the rating and displaying it on the toast
                String rating=String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void addListenerOnRatingBar() {

        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
    }

   /* @Override
    protected void onStop() {

        Intent intent = new Intent();
        // movies.add(movie);
        intent.putExtra("favorite_movies",  movie);
        setResult(Activity.RESULT_OK, intent );
        finish();
        super.onStop();
    }*/

    @Override
    public void onBackPressed() {
       super.onBackPressed();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /*
         * without call to super onBackPress() will not be called when
         * keyCode == KeyEvent.KEYCODE_BACK
         */
        super.onKeyUp(keyCode, event);
        Intent intent = new Intent();
        // movies.add(movie);
        intent.putExtra("favorite_movies",  movie);
        setResult(Activity.RESULT_OK, intent );

        finish();

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode != KeyEvent.KEYCODE_BACK && super.onKeyDown(keyCode, event);
    }

    private void mvtrailerImage1Click()
    {
        Toast.makeText(getApplicationContext(), "trailer-1 will start playing shortly", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailerKey.get(0)));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }

    private void mvtrailerImage2Click()
    {
        Toast.makeText(getApplicationContext(), "trailer-2 will start playing shortly", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailerKey.get(1)));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }
    class TrailerButtonClick implements  View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent;
          int id =  v.getId();
          switch (id)
          {
              case R.id.mv_trailer_image1:
                  mvtrailerImage1Click();
                  break;
              case R.id.mv_trailer_image2:
                  mvtrailerImage2Click();
                  break;
          }



        }
    }
}
