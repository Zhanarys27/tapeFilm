package com.evanglazer.moviezone.fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.evanglazer.moviezone.adapters.ImageAdapter;
import com.evanglazer.moviezone.MainActivity;
import com.evanglazer.moviezone.R;

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

/**
 * Created by Evan on 1/3/2016.
 */
public class Top25 extends Fragment {
    static GridView gridView;
    static int width;
    static ArrayList<String> posters;
    static boolean sortByPop;
    static String API_KEY = "ea8f68dc2c7b43a3df248b9a638f5fb4";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movie_detail_fragment, container, false);

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
                System.out.println(position);
            }
        });
        return v;
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
                        urlString = MovieHome.URI_POP_ENDPOINT  + API_KEY;
                    } else {
                        urlString = MovieHome.URI_POP_ENDPOINT + API_KEY;
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
            String[] result = new String[moviesArray.length()];

            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movie = moviesArray.getJSONObject(i);
                String moviePath = movie.getString("poster_path");
                result[i] = moviePath;
            }
            return result;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("                           Top 25");


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

