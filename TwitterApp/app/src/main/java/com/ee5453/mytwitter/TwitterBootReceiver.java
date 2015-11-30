package com.ee5453.mytwitter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TwitterBootReceiver extends BroadcastReceiver {
    static String TAG="TwitterBootReceiver";
    public TwitterBootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //start the refresh service
        Log.d(TAG,"onReceive");
       // context.startService(new Intent(context,StatusFetchService.class));
    }
}
