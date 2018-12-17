package com.evanglazer.moviezone.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.evanglazer.moviezone.FavoritesActivity;
import com.evanglazer.moviezone.MainActivity;
import com.evanglazer.moviezone.R;
import com.evanglazer.moviezone.TopActivity;

/**
 * Created by Evan on 12/30/2015.
 */

public class NavBar extends Fragment implements OnClickListener {
    ImageButton top25;
    ImageButton home;
    ImageButton favorites;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.nav_action_fragment, container, false);
        top25 = (ImageButton) v.findViewById(R.id.top25);

        top25.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TopActivity.class);
                startActivity(intent);
            }
        });

        favorites = (ImageButton) v.findViewById(R.id.featured);
        favorites.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavoritesActivity.class);
                startActivity(intent);
            }
        });

        home = (ImageButton) v.findViewById(R.id.home);
        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {

    }
}
