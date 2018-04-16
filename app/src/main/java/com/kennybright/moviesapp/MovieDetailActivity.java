package com.kennybright.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kennybright.moviesapp.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity  {


    TextView mvTitle;
    TextView mvOverview;
    TextView mvRatings;
    TextView mvReleaseDate;
    ImageView mvImageView;
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    String trailerKey = null;
    RatingBar ratingBar;
    Button ratingButton;
  private  Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
         movie = (Movie) intent.getSerializableExtra("movie");

        mvTitle = findViewById(R.id.mv_title);
        mvOverview = findViewById(R.id.mv_overview);
        mvRatings = findViewById(R.id.mv_ratings);
        mvReleaseDate = findViewById(R.id.mv_releasedate);
        mvImageView = findViewById(R.id.mv_poster_thumbnail);

        mvTitle.setText(movie.getTitle());
        mvOverview.setText(movie.getOverview());
        mvRatings.setText(String.valueOf(movie.getVote_average()));
        mvReleaseDate.setText(movie.getRelease_date().toString());

        String baseImageUri = "http://image.tmdb.org/t/p/w185";
        String poster_path = movie.getPoster_path();
        String absPath = baseImageUri.concat(poster_path);
        Picasso.with(getApplicationContext()).load(absPath).into(mvImageView);

        AddListenerOnClickBt();
        addListenerOnRatingBar();
        final Context  context = this;
        //http://api.themoviedb.org/3/movie/550/videos?api_key=###
        //https://www.youtube.com/watch?v=SUXWAEX2jlg
        // register for
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Click event fired" );

                String trailerId = String.valueOf(movie.getMovie_id());

                     // context parameter is use to fetch data
                     // listener is use to harvest trailer key ; we need to pass the trailer back from the task
                     // we use an interface declared in the task and implemented by our listener for communication
                     new FetchMovieTrailerTask(context, new FetchMovieTrailerTask.OnUpdateListener() {
                         @Override
                         public void onUpdate(String key) {

                                 trailerKey = key;
                                 Intent intent = new Intent(Intent.ACTION_VIEW);
                                 intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailerKey));
                                 intent.setPackage("com.google.android.youtube");
                                 startActivity(intent);

                         }
                     }).execute(trailerId);


            }


        };
        mvImageView.setOnClickListener(onClickListener);


    }

    public void AddListenerOnClickBt()
    {
        ratingBar = findViewById(R.id.ratingBar);
        ratingButton = findViewById(R.id.ratingBtn);
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
    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
    }


}
