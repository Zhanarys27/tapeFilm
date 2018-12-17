package com.evanglazer.moviezone;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Evan on 1/3/2016.
 */
public class SettingActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

        Switch switchFav;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.setting_layout);
            switchFav = (Switch) findViewById(R.id.switchfav);
            SharedPreferences sharedPreferences = getSharedPreferences("savemovies", Context.MODE_PRIVATE);
            switchFav.setChecked(sharedPreferences.getBoolean("save", false));
            switchFav.setOnCheckedChangeListener(this);

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            SharedPreferences sharedPreferences = getSharedPreferences("savemovies", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if(isChecked == false)
            {
                editor.putBoolean("save",isChecked);
                Toast.makeText(getApplicationContext(), "You have chosen to not save your movies!", Toast.LENGTH_LONG).show();
            }
            else
            {
                editor.putBoolean("save", isChecked);
                Toast.makeText(getApplicationContext(), "You have enabled saving your movies!", Toast.LENGTH_LONG).show();
            }
        }
}
