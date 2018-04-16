package com.kennybright.moviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import com.kennybright.moviesapp.model.AverageRatingComparator;
import com.kennybright.moviesapp.model.Movie;
import com.kennybright.moviesapp.model.PopularMovieComparator;
import com.squareup.picasso.Picasso;


public class MoviePosterAdapter  extends RecyclerView.Adapter<MoviePosterAdapter.ViewHolder> {
    //private String[] _movieData;
    List<Movie>  _movies = new ArrayList<>();
    Context  _context;
    private final MovieAdapterOnClickHandler mClickHandler;


    // interface that receives click messages
    public interface  MovieAdapterOnClickHandler  {
        void onClick(Movie movie);
    }

    // constructor
    public MoviePosterAdapter(Context context,MovieAdapterOnClickHandler clickHandler )
    {
        this._context = context;
        this.mClickHandler = clickHandler;
    }

    public void ClearMovieList()
    {

        _movies.clear();
        _movies = null;
    }
    @NonNull
    @Override
    public MoviePosterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the item layout
        View vw = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies,parent,false);
        // set the view's size, margins, paddings, and layout parameters
        ViewHolder vh = new ViewHolder(vw);
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull  MoviePosterAdapter.ViewHolder holder, final int position) {
        try {
            String baseImageUri = "http://image.tmdb.org/t/p/w185/";
            String poster_path = _movies.get(position).getPoster_path();
            String absolutepath = baseImageUri.concat(poster_path);

            Picasso.with(_context).load(absolutepath).into(holder.imageView);
        }catch (Exception ex )
        {
            ex.printStackTrace();
        }
      //  Picasso.with(_context).load(absolutepath).resize(240, 120).into(holder);
        holder.imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //  int pos = holder.getAdapterPosition();
                Movie movie = _movies.get(position);
                if (mClickHandler != null)
                mClickHandler.onClick(movie);
            }
        });
    }



    @Override
    public int getItemCount() {
       // if ( _movies.size() == 0) return 0;
        if (_movies == null) return 0;

        return _movies.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(View itemView) {

            super(itemView);
            // get reference to the item view (row)
            imageView =  itemView.findViewById(R.id.mv_poster_path);

        }



    }

    /**
     *
     * @param movieData movie data to be displayed.
     */
    public void setWeatherData(List<Movie> movieData, boolean sort) {
        _movies = movieData;
       //_movies.sort(Comparator.comparingDouble(Movie::getVote_average));
        if (sort) {
            _movies.sort(new AverageRatingComparator());
        }else {

            _movies.sort(new PopularMovieComparator());
        }
        notifyDataSetChanged();
    }


}


