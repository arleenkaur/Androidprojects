package com.ee5453.mytwitter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;


public class StatusActivity extends ActionBarActivity {
    //implements View.OnClickListener {

    public static final String TAG = "StatusActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //Button update_button;
        //update_button = (Button) findViewById(R.id.statusAct_updateButton);
        //  update_button.setOnClickListener(this);
    }

    public void buttonClicked(View v) {
        final EditText status;
        status = (EditText) findViewById(R.id.statusAct_statusText);

        if (v.getId() == R.id.statusAct_updateButton) {
            Log.d(TAG, "Clicked Update");
            new PostStatus().execute(status.getText().toString());
        } else if (v.getId() == R.id.statusAct_resetButton) {
            status.setText("");
        }
    }

    class PostStatus extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... status) {

            try {
                Twitter twitter = ((MyTwitterApp) getApplication()).getTwitterObject();
                twitter.setStatus(status[0]);
                //twitter.setStatus(status[0]);
                Log.d(TAG, "Posted.");
                return "Posted";
            } catch (TwitterException e) {
                Log.e(TAG, "Failed to post: " + e.getMessage());
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }

}
