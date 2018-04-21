
package com.kennybright.moviesapp.model;

import com.kennybright.moviesapp.model.Movie;
import java.util.Comparator;

public class AverageRatingComparator implements Comparator<Movie> {
    @Override
    public int compare(Movie o1, Movie o2) {
        //return (int) (o1.getVote_average() - o2.getVote_average());
        return Double.compare(o1.getVote_average(), o2.getVote_average());
    }
}
