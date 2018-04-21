package com.kennybright.moviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

@SuppressWarnings("serial") //With this annotation we are going to hide compiler warnings
//public class Movie implements Serializable {
public class Movie implements Parcelable {

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
    private double popularity;
    private int movie_id;

    public Movie(String title, String overview, Date release_date , String poster_path, double popularity, double vote_avg,int movieId)
    {
        this.title = title;
        this.overview = overview;
        this.release_date = release_date;
        this.poster_path = poster_path;
        this.vote_average = vote_avg;
        this.popularity = popularity;
        this.movie_id = movieId;

    }

    public Movie() {}

    private Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        vote_average = in.readDouble();
        popularity = in.readDouble();
        movie_id = in.readInt();
        long tmpDateVal = in.readLong();
        this.release_date = tmpDateVal == -1 ? null : new Date(tmpDateVal);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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
    public double getPopularity() {
        return popularity;
    }

    public void setPopuplarity(double popuplarity) {
        this.popularity = popuplarity;
    }


    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeDouble(vote_average);
        dest.writeDouble(popularity);
        dest.writeInt(movie_id);
        dest.writeLong(release_date != null? release_date.getTime() :-1 );
    }
}
