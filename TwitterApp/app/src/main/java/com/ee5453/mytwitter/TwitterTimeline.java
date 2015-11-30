package com.ee5453.mytwitter;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class TwitterTimeline extends ActionBarActivity {

    ListView list;
    SimpleCursorAdapter adapter;
    static final String TAG = "TwitterTimeline";

    Cursor tweets;
    String[] FROM = {StatusProvider.C_USER,
            StatusProvider.C_CREATED_AT,
            StatusProvider.C_TEXT};
    int[] TO = {R.id.timelineAct_user,R.id.timelineAct_time,R.id.timelineAct_status};
    TimelineReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_timeline);
       /* TextView tv;
        tv=(TextView)findViewById(R.id.statusAct_timeline);
        tv.setText("");*/
        //tweets=((MyTwitterApp) getApplication()).statusData.retrieve();
        tweets=getApplicationContext().getContentResolver().query(StatusProvider.CONTENT_URI,null,null,null,
                StatusProvider.C_CREATED_AT + " DESC");
        list=(ListView)findViewById(R.id.list);


        adapter = new SimpleCursorAdapter(
                this,R.layout.row,tweets,FROM,TO);


        adapter.setViewBinder(VIEW_BINDER);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {


                final EditText input = new EditText(TwitterTimeline.this);
                final Intent serviceIntent = new Intent(TwitterTimeline.this,StatusFetchService.class);


                new AlertDialog.Builder(TwitterTimeline.this)
                        .setTitle("Choose")
                        .setView(input)
                        .setMessage("Press delete to delete or update by entering text and pressing update!")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String text = input.getText().toString();
                                ContentValues content = new ContentValues();
                                content.put("value",text);

                                getApplicationContext().getContentResolver().update(StatusProvider.CONTENT_URI, content,String.valueOf(id),null);
                                startService(serviceIntent);
                                Toast.makeText(TwitterTimeline.this,"Updated Successfully",Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new AlertDialog.Builder(TwitterTimeline.this)
                                        .setTitle("Delete entry")
                                        .setMessage("Are you sure you want to delete this entry?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                getApplicationContext().getContentResolver().delete(StatusProvider.CONTENT_URI, String.valueOf(id), null);
                                                startService(serviceIntent);

                                                Toast.makeText(TwitterTimeline.this,"Deleted Successfully",Toast.LENGTH_LONG).show();

                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                            //Nothing to do
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        })
                        .show();
            }
        });
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent serviceIntent = new Intent(TwitterTimeline.this,StatusFetchService.class);
        Intent syncIntent = new Intent(TwitterTimeline.this,SyncNowService.class);
        Intent intentSettings = new Intent(TwitterTimeline.this,TwitterSettings.class);
        Intent statusUpdateIntent = new Intent(TwitterTimeline.this,StatusActivity.class);

        switch (id){
            case R.id.statusAct_startMenu:
                Log.d(TAG, "Selected start service");
                startService(serviceIntent);
                return true;
            case R.id.statusAct_stopMenu:
                Log.d(TAG,"Selected stop service");
                stopService(serviceIntent);
                return true;
            case R.id.statusAct_syncNow:
                Log.d(TAG,"Selected Sync Now");
                startService(syncIntent);
                return true;
            case R.id.action_settings:
                Log.d(TAG,"Selected Settings");
                startActivity(intentSettings);
                return true;
            case R.id.statusAct_status:
                Log.d(TAG,"Selected Status Update");
                startActivity(statusUpdateIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void filter(View v)
    {
        Intent i = new Intent(this,Filter.class);
        startActivity(i);
    }
}

