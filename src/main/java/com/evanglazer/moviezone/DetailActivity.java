package com.evanglazer.moviezone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.evanglazer.moviezone.fragments.Detail;
import com.evanglazer.moviezone.model.MovieDetail;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Evan on 1/3/2016.
 */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String URL_IMAGE_ENDPOINT = "http://image.tmdb.org";
    public static final String URL_API_ENDPOINT = "http://api.themoviedb.org";

    ImageView imageView;
    TextView IMDBRating;
    TextView UserRating;
    TextView ReleaseDate;
    TextView Description;
    Button trailer1;
    Button trailer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_fragment);

        MovieDetail detail = new MovieDetail();
        imageView = (ImageView) findViewById(R.id.movieView);
        IMDBRating = (TextView) findViewById(R.id.imdbRatingText);
        UserRating = (TextView) findViewById(R.id.userRatingText);
        ReleaseDate = (TextView)findViewById(R.id.releaseDateText);
        Description = (TextView) findViewById(R.id.description);
        trailer1 = (Button) findViewById(R.id.trailer1);
        trailer2 = (Button) findViewById(R.id.trailer2);

        String url = "http://image.tmdb.org/t/p/w185/" + MovieDetail.poster_path[detail.current];
        Picasso.with(getApplicationContext()).load(url).placeholder(R.drawable.placeholder).into(imageView);

        //Displaying values by fetching from intent
        setTitle(MovieDetail.original_title[detail.current]);
        IMDBRating.setText(String.valueOf(MovieDetail.vote_average[detail.current]));
        UserRating.setText(String.valueOf(MovieDetail.vote_average[detail.current]));
        ReleaseDate.setText(String.valueOf(MovieDetail.release_date[detail.current]));
        Description.setText(MovieDetail.overview[detail.current]);

        trailer1.setOnClickListener(this);
        trailer2.setOnClickListener(this);

    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, Detail.class);
        this.startActivity(i);
    }
}
