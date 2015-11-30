package com.ee5453.mytwitter;

import android.app.Application;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import winterwell.jtwitter.Twitter;

/**
 * Created by kkx358 on 3/17/2015.
 */
public class MyTwitterApp extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {
    static String TAG="MyTwitterApp";
    SharedPreferences sp;
    private Twitter twitter;
    private String url;
    private int delay;
    //TwitterStatusData statusData;

    static final String ACTION_NEW_STATUS = "com.ee5453.mytwitter.ACTION.NEW_STATUS";

    @Override
    public void onCreate() {
        super.onCreate();
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sp.registerOnSharedPreferenceChangeListener(this);
        String user = sp.getString("user_name","");
        String password = sp.getString("password","");
        twitter = new Twitter(user,password);
        delay=Integer.parseInt(sp.getString("sync_frequency","10"));
        url=sp.getString("url", "");
        twitter.setAPIRootUrl(url);
        //statusData=new TwitterStatusData(this);
        Log.d(TAG,"onCreate");
    }

    Twitter getTwitterObject(){
        return twitter;
    }

    public int getDelay(){
        return delay;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG,key);
        twitter=null;

        //if (key.equals("user_name"))

        String user = sp.getString("user_name","");
        String password = sp.getString("password","password");
        twitter = new Twitter(user,password);
        delay=Integer.parseInt(sp.getString("sync_frequency","10"));
        url=sp.getString("url","");
        //twitter.setAPIRootUrl("http://yamba.marakana.com/api");
        twitter.setAPIRootUrl(url);
    }

    long latestTimeStampDB = -1L;
    public boolean getAndPut(){
        Log.d(TAG,"initial TS in table: "+latestTimeStampDB);
        List<Twitter.Status> statuses = getTwitterObject().getPublicTimeline();
        long biggestFromTwitter=-1;
        Log.d(TAG, "Number of statuses: " + statuses.size());
        Log.d(TAG, "Oldest time: " + statuses.get(statuses.size()-1));
        Log.d(TAG, "Newest time: " + statuses.get(0));

        //Cursor cursor = statusData.retrieve();
       /* Cursor cursor;
        cursor=getApplicationContext().getContentResolver().query(
                StatusProvider.CONTENT_URI,null,null,null,
                StatusProvider.C_CREATED_AT + " DESC");
        while(cursor.moveToNext()){
            long ts = cursor.getLong(cursor.getColumnIndex(StatusProvider.C_CREATED_AT));
            if (ts > latestTimeStampDB) {
                latestTimeStampDB = ts;
                Log.d(TAG,"biggest TS in table so far: "+latestTimeStampDB);
                Log.d(TAG,"status text: " +
                cursor.getString(cursor.getColumnIndex(StatusProvider.C_TEXT)));
            }
        }
        Log.d(TAG,"biggest TS in table: "+latestTimeStampDB);*/

        for (Twitter.Status status : statuses) {
            //Log.d(TAG, status.getUser().getStatus().toString());
            //statusData.insert(status);
            ContentValues values = new ContentValues();
            values.put(StatusProvider.C_ID, status.id);
            values.put(StatusProvider.C_CREATED_AT, status.createdAt.getTime());
            values.put(StatusProvider.C_TEXT, status.text);
            values.put(StatusProvider.C_USER, status.user.name);
            getApplicationContext().getContentResolver().insert(StatusProvider.CONTENT_URI,
                    values);
        }
        return true; //if new status exists
        //return false; //if no new status
    }
}
