package com.evanglazer.moviezone.model;

import android.graphics.Bitmap;

/**
 * Created by Evan on 1/6/2016.
 */
public class MovieDetail {
    public static int current = 0;

    public static String[] poster_path;
    public static String[] movie_id;
    public static String[] release_date;
    public static String[] original_title;
    public static String[] trailer;
    public static String[] overview;
    public static Double[] vote_average;

    private Bitmap bitmap;


    public Bitmap getBitmap() {
        return bitmap;
    }
}
