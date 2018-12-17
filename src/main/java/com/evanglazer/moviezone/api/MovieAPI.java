package com.evanglazer.moviezone.api;

import com.evanglazer.moviezone.model.MovieDetail;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Evan on 1/5/2016.
 */
public interface MovieAPI {
    //http://api.themoviedb.org
    @GET("/3/movie/273248?api_key=ea8f68dc2c7b43a3df248b9a638f5fb4")
    void getMovieDetails(Callback<List<MovieDetail>> callback);

    //http://api.themoviedb.org
    @GET("/3/discover/movie?primary_release_year=2015")
    void getPopularMovies(Callback<List<MovieDetail>> response);
}
