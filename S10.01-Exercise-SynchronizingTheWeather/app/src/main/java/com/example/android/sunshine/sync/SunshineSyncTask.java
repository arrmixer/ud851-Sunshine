package com.example.android.sunshine.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.data.WeatherDbHelper;
import com.example.android.sunshine.data.WeatherProvider;
import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

import java.net.URL;

//  TODO (1) Create a class called SunshineSyncTask



public class SunshineSyncTask{
    //  TODO (2) Within SunshineSyncTask, create a synchronized public static void method called syncWeather
    synchronized public static void syncWeather(Context context){

        //      TODO (3) Within syncWeather, fetch new weather data
        // I knew I needed to looked in the NetworkUtils but didn't know where to start
        // I should have been patience and reviewed the code a little bit more
        try{
            // get the Url using the util method getUrl it will decide how to
            // build either lat and long or a simple location string.
            URL fetchWeather = NetworkUtils.getUrl(context);

            // gets a JSON object from the url
            String jsonfetchWeather = NetworkUtils.getResponseFromHttpUrl(fetchWeather);


            //create values for the content Resolver to read via the getWeatherContentValuesFromJson Utility Method
            ContentValues[] weatherDates = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonfetchWeather);

            //      TODO (4) If we have valid results, delete the old data and insert the new
            // after successfully creating ContentValues[] we can now retrieve
            // the values with the ContentResolver if any new values are present
            if(weatherDates != null && weatherDates.length != 0){
                ContentResolver sunshineResolver = context.getContentResolver();
                sunshineResolver.delete(WeatherContract.WeatherEntry.CONTENT_URI, null, null);
                sunshineResolver.bulkInsert(WeatherContract.WeatherEntry.CONTENT_URI, weatherDates);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}