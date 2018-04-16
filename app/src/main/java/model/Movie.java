package com.kennybright.moviesapp.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
public class Movie implements Serializable {

    /* original title
     movie poster image thumbnail
     A plot synopsis (called overview in the api)
     user rating (called vote_average in the api)
     release date*/
    private String title;
    private String overview;
    private Date release_date;
    private String poster_path;
    private double vote_average;
    private double popuplarity;
    private int movie_id;

    public Movie(String title, String overview, Date release_date , String poster_path, double popularity, double vote_avg,int movieId)
    {
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.vote_average = vote_avg;
        this.popuplarity = popularity;
        this.movie_id = movieId;

    }

    public Movie() {}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }
    public double getPopulaity() {
        return popuplarity;
    }

    public void setPopuplarity(double popuplarity) {
        this.popuplarity = popuplarity;
    }


    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }
}
