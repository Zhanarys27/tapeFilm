package com.evanglazer.moviezone.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.evanglazer.moviezone.R;

/**
 * Created by Evan on 12/30/2015.
 */

public class FavoriteMovie extends Fragment{
    ImageView img;

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("           Movie Zone Favorites");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fav_fragment, container, false);
        return v;
    }
}
