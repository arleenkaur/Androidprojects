package com.ee5453.mytwitter;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;

public class StatusFetchService extends Service {
    static final String TAG = "StatusFetchService";
    boolean keepRunning=true;
    public StatusFetchService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
      //  throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"Service is being started");
        new Thread() {
            @Override
            public void run() {
                super.run();
                while(keepRunning) {
                   /* List<Twitter.Status> statuses = ((MyTwitterApp)getApplication()).getTwitterObject().getPublicTimeline();
                    for (Twitter.Status status : statuses) {
                        Log.d(TAG, status.getUser().getStatus().toString());
                        ((MyTwitterApp)getApplication()).statusData.insert(status);
                    }*/
                    ((MyTwitterApp)getApplication()).getAndPut();
                    sendBroadcast(new Intent(MyTwitterApp.ACTION_NEW_STATUS));

                    try {
                        int delay = ((MyTwitterApp)getApplication()).getDelay();
                        Thread.sleep(delay*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        keepRunning=false;
        Log.d(TAG,"Service is being destroyed");
    }
}
