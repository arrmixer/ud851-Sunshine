package com.example.android.sunshine.sync;

// TODO (9) Create a class called SunshineSyncUtils
    //  TODO (10) Create a public static void method called startImmediateSync
    //  TODO (11) Within that method, start the SunshineSyncIntentService

import android.content.Context;
import android.content.Intent;

public class SunshineSyncUtils {
    public static void startImmediateSync(Context context) {
        Intent myIntent = new Intent(context, SunshineSyncIntentService.class);
        //didn't realized to use context to start service
        context.startService(myIntent);


    }
}