package com.ee5453.mytwitter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.ee5453.mytwitter.R;

public class FilteredTweets extends ActionBarActivity {

    ListView list;
    SimpleCursorAdapter adapter;
    static final String TAG = "TwitterTimeline";

    Cursor tweets;
    String[] FROM = {StatusProvider.C_USER,
            StatusProvider.C_CREATED_AT,
            StatusProvider.C_TEXT};
    int[] TO = {R.id.timelineAct_user,R.id.timelineAct_time,R.id.timelineAct_status};
    TimelineReceiver receiver;
    String user,early,latest;
    String filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_tweets);
        Intent i = getIntent();
        user   = i.getStringExtra("user");
        early  = i.getStringExtra("early");
        latest = i.getStringExtra("latest");

        filter ="user_name like '%" + user + "%' and created_at <= "+early+" and created_at <= "+early;
                // and created_at >='%" +early+ "%'";

        tweets=getApplicationContext().getContentResolver().query(StatusProvider.CONTENT_URI,null,filter,null,
                StatusProvider.C_CREATED_AT + " DESC");
        list=(ListView)findViewById(R.id.filteredTweetList);

        adapter = new SimpleCursorAdapter(
                this,R.layout.row,tweets,FROM,TO);


        adapter.setViewBinder(VIEW_BINDER);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        if (receiver==null)
            receiver=new TimelineReceiver();
        registerReceiver(receiver,new IntentFilter(MyTwitterApp.ACTION_NEW_STATUS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");
        unregisterReceiver(receiver);
    }

    final SimpleCursorAdapter.ViewBinder VIEW_BINDER = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if (view.getId() == R.id.timelineAct_status)
                return false;
            else if (view.getId() == R.id.timelineAct_time) {
                long timeInMillis = cursor.getLong(
                        cursor.getColumnIndex(StatusProvider.C_CREATED_AT));

                CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timeInMillis);

                ((TextView) view).setText(relativeTime);
                return true;
            }
            else if (view.getId() == R.id.timelineAct_user) {
                String db_user = cursor.getString(
                        cursor.getColumnIndex(StatusProvider.C_USER));
                String actual_user = "Student";
                ((TextView)view).setText(actual_user);
                return true;
            }
            return true;
        }
    };

    class TimelineReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            //tweets=((MyTwitterApp) getApplication()).statusData.retrieve();
            tweets=context.getContentResolver().query(StatusProvider.CONTENT_URI,null,null,null,
                    StatusProvider.C_CREATED_AT + " DESC");
            Log.d(TAG,"onReceive");
            adapter.changeCursor(tweets);
        }
    }


    }