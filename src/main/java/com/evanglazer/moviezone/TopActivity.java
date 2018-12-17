package com.evanglazer.moviezone;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.evanglazer.moviezone.fragments.FavoriteMovie;
import com.evanglazer.moviezone.fragments.NavBar;
import com.evanglazer.moviezone.fragments.Top25;

/**
 * Created by Evan on 1/3/2016.
 */
public class TopActivity extends AppCompatActivity {
    FragmentManager fm = getFragmentManager();
    public static boolean TABLET = false;

    public boolean isTablet(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        TABLET = isTablet(this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB && savedInstanceState == null) {
            fm.beginTransaction().replace(R.id.main1, new FavoriteMovie()).commit();
            fm.beginTransaction().replace(R.id.main1, new NavBar()).commit();
            fm.beginTransaction().replace(R.id.main1, new Top25()).commit();
        }

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

}
