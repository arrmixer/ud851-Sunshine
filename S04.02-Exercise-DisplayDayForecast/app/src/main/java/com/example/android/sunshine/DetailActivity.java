package com.example.android.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private TextView mWeatherItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mWeatherItem = findViewById(R.id.weather_item);

        // TODO (2) Display the weather forecast that was passed from MainActivity
        Intent weatherDetailIntent = getIntent();

        if(weatherDetailIntent.hasExtra(Intent.EXTRA_TEXT)){
            String textFromWeatherItem = weatherDetailIntent.getStringExtra(Intent.EXTRA_TEXT);
            mWeatherItem.setText(textFromWeatherItem);
        }
    }
}