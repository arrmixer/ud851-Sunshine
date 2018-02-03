package com.example.android.sunshine;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG2 = DetailActivity.class.getSimpleName();
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private String mForecast;
    private TextView mWeatherDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mWeatherDisplay = (TextView) findViewById(R.id.tv_display_weather);

        mForecast = getIntentDailyWeatherInfo();

        mWeatherDisplay.setText(mForecast);
    }

    // TODO (3) Create a menu with an item with id of action_share
    // TODO (4) Display the menu and implement the forecast sharing functionality
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // inflate the menu resource resource
        MenuInflater inflater = getMenuInflater();
        // inflate menu
        inflater.inflate(R.menu.share, menu);
        // return true so that menu is displayed on the toolbar
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
       int id = item.getItemId();

       if(id == R.id.share){
           String info = getIntentDailyWeatherInfo();
           shareText(info);
           return true;
       }

       return super.onOptionsItemSelected(item);
    }

    public void shareText(String text){
        String mimeType = "text/plain";

       String title = "Today's Weather";

        Intent shareIntent = ShareCompat.IntentBuilder
                .from(DetailActivity.this)
                .setType(mimeType)
                .setText(text)
                .getIntent();

        Log.d(TAG2, "shareText: " + shareIntent.toString());

        if(shareIntent.resolveActivity(getPackageManager()) != null){
            startActivity(shareIntent);
        }
    }

    //helper method to get Extra data from intent
    public String getIntentDailyWeatherInfo(){
        Intent intentThatStartedThisActivity = getIntent();
        String weatherDailyInfo = "no info";
        if (intentThatStartedThisActivity != null) {
            weatherDailyInfo = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
            Log.d(TAG2, "getIntentDailyWeatherInfo: "  + weatherDailyInfo);
        }


        return weatherDailyInfo;
    }
}