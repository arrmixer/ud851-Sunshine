/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import java.nio.file.WatchEvent;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //      TODO (21) Implement LoaderManager.LoaderCallbacks<Cursor>
    public static final String TAG = DetailActivity.class.getSimpleName();
    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//  TODO (18) Create a String array containing the names of the desired data columns from our ContentProvider

    public static final String[] WEATHER_DETAIL_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES
    };

//  TODO (19) Create constant int values representing each column name's position above

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_CONDITION_ID = 1;
    public static final int INDEX_WEATHER_MAX_TEMP = 2;
    public static final int INDEX_WEATHER_MIN_TEMP = 3;
    public static final int INDEX_WEATHER_HUMIDITY = 4;
    public static final int INDEX_WEATHER_PRESSURE = 5;
    public static final int INDEX_WEATHER_WIND_SPEED = 6;
    public static final int INDEX_WEATHER_DEGREES = 7;

    //  TODO (20) Create a constant int to identify our loader used in DetailActivity
    private static final int ID_DETAIL_LOADER = 45;


    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

    private Uri uri;

//  TODO (15) Declare a private Uri field called mUri

//  TODO (10) Remove the mWeatherDisplay TextView declaration


    //  TODO (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mWeatherDate;
    private TextView mWeatherDescription;
    private TextView mWeatherHigh;
    private TextView mWeatherLow;
    private TextView mWeatherHumidity;
    private TextView mWeatherWind;
    private TextView mWeatherPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      TODO (12) Remove mWeatherDisplay TextView

//      TODO (13) Find each of the TextViews by ID
        mWeatherDate = findViewById(R.id.display_date);
        mWeatherDescription = findViewById(R.id.display_date_weahter_description);
        mWeatherHigh = findViewById(R.id.display_date_high_temp);
        mWeatherLow = findViewById(R.id.display_date_low_temp);
        mWeatherHumidity = findViewById(R.id.display_date_humidity);
        mWeatherWind = findViewById(R.id.display_date_wind);
        mWeatherPressure = findViewById(R.id.display_date_pressure);

//      TODO (14) Remove the code that checks for extra text
//      TODO (16) Use getData to get a reference to the URI passed with this Activity's Intent
//      TODO (17) Throw a NullPointerException if that URI is null
//      TODO (35) Initialize the loader for DetailActivity
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            try {
                uri = intentThatStartedThisActivity.getData();
            } catch (NullPointerException e) {
                Log.e(TAG, "onCreate: ", e);
                e.getStackTrace();
            }

        }

      getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, DetailActivity.this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    //  TODO (22) Override onCreateLoader
//          TODO (23) If the loader requested is our detail loader, return the appropriate CursorLoader

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_DETAIL_LOADER:

                /*
                 * A SELECTION in SQL declares which rows you'd like to return. In our case, we
                 * want all weather data from today onwards that is stored in our weather table.
                 * We created a handy method to do that in our WeatherEntry class.
                 */
//                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();

               String date =  uri.getPathSegments().get(1);



                return new CursorLoader(this,
                        uri,
                        WEATHER_DETAIL_PROJECTION,
                        WeatherContract.WeatherEntry.COLUMN_DATE + "=?",
                        new String[] {date},
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }

    }

    //  TODO (24) Override onLoadFinished
//      TODO (25) Check before doing anything that the Cursor has valid data
//      TODO (26) Display a readable data string
//      TODO (27) Display the weather description (using SunshineWeatherUtils)
//      TODO (28) Display the high temperature
//      TODO (29) Display the low temperature
//      TODO (30) Display the humidity
//      TODO (31) Display the wind speed and direction
//      TODO (32) Display the pressure
//      TODO (33) Store a forecast summary in mForecastSummary

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        // added this after I saw solution good practice
        boolean cursorHasValidData = false;


        if (data != null) {
            data.moveToFirst();
             /* Read date from the cursor */
            long dateInMillis = data.getLong(INDEX_WEATHER_DATE);
        /* Get human readable string using our utility method */
            String dateString = SunshineDateUtils.getFriendlyDateString(this, dateInMillis, false);
        /* Use the weatherId to obtain the proper description */
            int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
            String description = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherId);
        /* Read high temperature from the cursor (in degrees celsius) */
            double highInCelsius = data.getDouble(INDEX_WEATHER_MAX_TEMP);
            String highInCelsiusString = SunshineWeatherUtils.formatTemperature(this ,highInCelsius);
        /* Read low temperature from the cursor (in degrees celsius) */
            double lowInCelsius = data.getDouble(INDEX_WEATHER_MIN_TEMP);
            String lowInCelsiusString = SunshineWeatherUtils.formatTemperature(this ,lowInCelsius);
            /* get string for high or low */
          /* Read Humidity from the cursor  */
            double humidity = data.getDouble(INDEX_WEATHER_HUMIDITY);
            String humidityString = SunshineWeatherUtils.formatHumidity(this, humidity);
          /* Read Pressure from the cursor (in degrees celsius) */
            double pressure = data.getDouble(INDEX_WEATHER_PRESSURE);
            String pressureString = SunshineWeatherUtils.formatPressure(this, pressure);
          /* Read wind speed and degrees from the cursor (in degrees celsius) */
            float windSpeed = data.getFloat(INDEX_WEATHER_WIND_SPEED);
            float degrees = data.getFloat(INDEX_WEATHER_DEGREES);
            String windSpeedString = SunshineWeatherUtils.getFormattedWind(this, windSpeed, degrees);

            /* set the strings to the corresponding views */
            mWeatherDate.setText(dateString);
            mWeatherDescription.setText(description);
            mWeatherHigh.setText(highInCelsiusString);
            mWeatherLow.setText(lowInCelsiusString);
            mWeatherHumidity.setText(humidityString);
            mWeatherPressure.setText(pressureString);
            mWeatherWind.setText(windSpeedString);

            mForecastSummary = dateString + "-" + description + "-" +
                    SunshineWeatherUtils.formatHighLows(this, highInCelsius, lowInCelsius)
                    + "-" + humidityString + "-" + pressureString + "-" + windSpeedString;

            cursorHasValidData = true;
        }

        // added this after I saw solution good practice
        if(!cursorHasValidData){
            return;
        }



    }

    //  TODO (34) Override onLoaderReset, but don't do anything in it yet

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}