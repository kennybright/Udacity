
package com.kennybright.moviesapp.model;

import com.kennybright.moviesapp.model.Movie;
import java.util.Comparator;

public class PopularMovieComparator  implements Comparator<Movie>{
    @Override
    public int compare(Movie o1, Movie o2) {
        //return (int) (o1.getPopulaity()  -  o2.getPopulaity());
        if ( o1.getPopulaity() < o2.getPopulaity() ) return -1;
        else if ( o1.getPopulaity() == o2.getPopulaity()) return 0;
        else return 1;
    }
}
