

package com.kennybright.moviesapp.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Provider {

    private  List<String> trailers;
    private List <String> reviews;

    public Provider() {
        trailers =  new ArrayList<>();
        reviews =  new ArrayList<>();
    }

    public List<String> getReviews() {
        return reviews;
    }

    public List<String> getTrailers() {
        return trailers;
    }

    public enum operation { TRAILERS, REVIEWS}

   public void AddReview(String review)
   {
       trailers.add(review);
   }
    public void AddTrailer(String trailer)
    {
        trailers.add(trailer);
    }


}


