package com.example.android.sunshine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView mWeatherItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mWeatherItem = findViewById(R.id.weather_item);


        Intent getWeatherIntent = getIntent();

        if(getWeatherIntent.hasExtra(Intent.EXTRA_TEXT)){
            String textFromWeatherItem = getWeatherIntent.getStringExtra(Intent.EXTRA_TEXT);
            mWeatherItem.setText(textFromWeatherItem);
        }
    }


}
