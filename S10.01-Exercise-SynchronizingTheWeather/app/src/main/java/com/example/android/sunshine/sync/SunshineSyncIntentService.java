package com.example.android.sunshine.sync;

// TODO (5) Create a new class called SunshineSyncIntentService that extends IntentService

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class SunshineSyncIntentService extends IntentService{

    public SunshineSyncIntentService(){
        super("SunshineSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = getApplicationContext();
        SunshineSyncTask.syncWeather(this);

    }
}
//  TODO (6) Create a constructor that calls super and passes the name of this class
//  TODO (7) Override onHandleIntent, and within it, call SunshineSyncTask.syncWeather