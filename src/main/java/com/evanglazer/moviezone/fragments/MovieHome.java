package com.evanglazer.moviezone.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.evanglazer.moviezone.DetailActivity;
import com.evanglazer.moviezone.MainActivity;
import com.evanglazer.moviezone.R;
import com.evanglazer.moviezone.adapters.ImageAdapter;
import com.evanglazer.moviezone.api.MovieAPI;
import com.evanglazer.moviezone.model.MovieDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Evan on 12/30/2015.
 */
public class MovieHome extends Fragment{
    static GridView gridView;
    static int width;
    Intent intent;
    static ArrayList<String> posters;
    List<MovieDetail> detailList;
    public static ProgressDialog loading = null;
    static boolean sortByPop;
    static String URI_POP_ENDPOINT = "http://api.themoviedb.org/3/discover/movie?primary_release_year=2015&api_key=";
    static String API_KEY = "ea8f68dc2c7b43a3df248b9a638f5fb4";

    //Strings to bind with intent will be used to send data to other activity
    public static final String KEY_MOVIE_TITLE = "key_movie_title";
    public static final String KEY_IMDB_RATING = "key_imdb_rating";
    public static final String KEY_USER_RATING = "key_user_rating";
    public static final String KEY_RELEASE_DATE = "key_release_date";

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_gridview, container, false);

        WindowManager wm = (WindowManager) getActivity().getSystemService((Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        // coords of x and y
        Point size = new Point();
        display.getSize(size);

        // change display for tablet and phone 1/6 or 1/3
        if (MainActivity.TABLET) {
            width = size.x / 6;
        } else width = size.x / 3;

        // populate 3 posters in a row
        if (getActivity() != null) {
            ArrayList<String> array = new ArrayList<String>();
            ImageAdapter adapter = new ImageAdapter(getActivity(), array, width);
            gridView = (GridView) v.findViewById(R.id.gridView);

            // 3 per row or 6 based on width windows manager
            gridView.setColumnWidth(width);
            gridView.setAdapter(adapter);

        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Creating an intent
                intent = new Intent(getActivity(), DetailActivity.class);

                //Getting the requested book from the list
                MovieDetail detail = new MovieDetail();
                detail = null;
//                detail = detailList.get(position);
                System.out.println("...");
                //Log.e(TAGOFMESSAGE,"Raised an exception during doInBackground Method",e);
                detail.current = position;

                requestData();

                //Starting another activity to show book detail
                startActivity(intent);

            }
        });
        return v;
    }


    private void requestData(){
        //While the app fetched data we are displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Fetching Data","Please wait...",false,false);
        if(isNetworkAvailable()) {

            //Creating a rest adapter
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(DetailActivity.URL_API_ENDPOINT)
                    .build();

            //Creating an object of our api interface
            MovieAPI api = adapter.create(MovieAPI.class);

            //Defining the method
            api.getMovieDetails(new Callback<List<MovieDetail>>() {
                @Override
                public void success(List<MovieDetail> list, Response response) {

                    //Storing the data in our list
                    detailList = list;
                   //MovieDetail detail = new MovieDetail();
                   //detail = detailList.get(detail.current);
                   // System.out.println("" + detail);

                    //Dismissing the loading progressbar
                    loading.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {

                    MovieDetail detail = new MovieDetail();
                    System.out.println("Not working  " + MovieDetail.original_title[detail.current].toString());

                    //Adding book details to intent
                    intent.putExtra(KEY_MOVIE_TITLE, MovieDetail.original_title[detail.current].toString());
                    intent.putExtra(KEY_IMDB_RATING, MovieDetail.vote_average[detail.current].doubleValue());
                    intent.putExtra(KEY_USER_RATING, MovieDetail.vote_average[detail.current].doubleValue());
                    intent.putExtra(KEY_RELEASE_DATE,MovieDetail.release_date[detail.current].toString());

                    Bundle bundle = intent.getExtras();
                }
            });
        }
        else
        {
            loading.dismiss();
            Toast.makeText(getActivity(), "Cannot connect to the internet", Toast.LENGTH_LONG).show();
        }
    }




    public class ImageLoadTask extends AsyncTask<Void, Void, ArrayList<String>> {

        // array list will hold strings of image paths
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            while (true) {
                try {
                    posters = new ArrayList(Arrays.asList(getPathsFromAPI(sortByPop)));
                    return posters;
                } catch (Exception e) {
                    continue;
                }
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            if (strings != null && getActivity() != null) {
                ImageAdapter adapter = new ImageAdapter(getActivity(), strings, width);
                gridView.setAdapter(adapter);
            }
        }

        public String[] getPathsFromAPI(boolean sort) {
            while (true) {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String JSONResult;
                try {
                    String urlString = null;
                    if (sortByPop) {
                        urlString = URI_POP_ENDPOINT + API_KEY;
                    } else {
                        urlString = URI_POP_ENDPOINT + API_KEY;
                    }
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    //Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    if (buffer.length() == 0) {
                        return null;
                    }
                    JSONResult = buffer.toString();

                    try {
                        return getPathsFromJSON(JSONResult);
                    } catch (JSONException e) {
                        return null;
                    }
                } catch (Exception e) {
                    continue;
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                        }
                    }
                }


            }
        }

        public String[] getPathsFromJSON(String JSONStringParam) throws JSONException {

            JSONObject JSONString = new JSONObject(JSONStringParam);

            JSONArray moviesArray = JSONString.getJSONArray("results");

            MovieDetail.poster_path = new String[moviesArray.length()];
            MovieDetail.release_date = new String[moviesArray.length()];
            MovieDetail.original_title = new String[moviesArray.length()];;
            MovieDetail.vote_average = new Double[moviesArray.length()];;
            MovieDetail.overview = new String[moviesArray.length()];
            MovieDetail.movie_id = new String[moviesArray.length()];

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movie = moviesArray.getJSONObject(i);

                String moviePath = movie.getString("poster_path");
                String release = movie.getString("release_date");
                String id = movie.getString("id");
                String original = movie.getString("original_title");
                Double vote = movie.getDouble("vote_average");
                String desc = movie.getString("overview");

                MovieDetail.movie_id[i] = id;
                MovieDetail.poster_path[i] = moviePath;
                MovieDetail.release_date[i] = release;
                MovieDetail.original_title[i] = original;
                MovieDetail.vote_average[i] = vote;
                MovieDetail.overview[i] = desc;
            }
            return MovieDetail.poster_path;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("                     Movie Zone");


        // check if network is available
        if (isNetworkAvailable()) {
            gridView.setVisibility(GridView.VISIBLE);
            new ImageLoadTask().execute();
        } else {
            Toast.makeText(getActivity(), "There is no internet connection!", Toast.LENGTH_LONG).show();
            // gridview visibility gone
            gridView.setVisibility(GridView.GONE);
        }
    }



        // check network
        public boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }
