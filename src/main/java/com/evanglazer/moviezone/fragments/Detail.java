package com.evanglazer.moviezone.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.evanglazer.moviezone.R;
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


/**
 * Created by Evan on 1/3/2016.
 */
public class Detail extends AppCompatActivity{
    WebView webView;
    public static String TRAILER_ENDPOINT = "http://api.themoviedb.org/3/movie/";
    public static String YOUTUBE_ENDPOINT = "https://www.youtube.com/watch?v=";
    MovieDetail detail = new MovieDetail();
    boolean sort = true;
    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_fragment);
        setTitle(MovieDetail.original_title[MovieDetail.current] + " Trailer");
        webView = (WebView)findViewById(R.id.webView);

        webView.setInitialScale(1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);

        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        webView.setWebChromeClient(new WebChromeClient() {
        });
        String html = getHTML();
        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }

    public String getHTML() {
        String html = "<iframe class=\"youtube-player\" " +
                "style=\"border: 0; width: 100%; height: 95%; padding:0px; margin:0px\" id=\"ytplayer\" " +
                "type=\"text/html\" src=\"http://www.youtube.com/embed/"
                + MovieDetail.trailer
                + "?fs=0\" frameborder=\"0\">\n"
                + "</iframe>\n";
        return html;
    }



    public String[] getPathsFromAPI(boolean sort) {
        while (true) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String JSONResult;
            try {
                String urlString = null;

                    urlString = TRAILER_ENDPOINT + MovieDetail.movie_id[detail.current]
                         + "/videos?api_key="+MovieHome.API_KEY;

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
        System.out.println(moviesArray.length() + "length long");
        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);

            String trailerPath = movie.getString("key");
            MovieDetail.trailer[i] = trailerPath;
        }
        return MovieDetail.trailer;
    }

}


class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}