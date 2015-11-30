package midnight.quizapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ChatActivity extends ActionBarActivity {

    ListView listView;
    ArrayList<String> message = new ArrayList<>();//
    ArrayList<Integer> type = new ArrayList<>();
    String id;
    CustomAdapter adapter;
    Toolbar mToolbar;
    Timer myTimer;
    String user;
    AutoRefreshTimer myTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent = getIntent();
        id=intent.getStringExtra("postId");
        Log.v("String",id);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Group : "+id);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        listView = (ListView)findViewById(R.id.listView);

        adapter = new CustomAdapter(this,message,type);
        listView.setAdapter(adapter);
        ParseUser pUser = ParseUser.getCurrentUser();
        user = pUser.getString("username");
        myTimer = new Timer();
        myTimerTask= new AutoRefreshTimer();
        myTimer.schedule(myTimerTask, 5000,10000);
        new refresh().execute();

    }

    class refresh extends AsyncTask<String,Integer,String>
    {


        @Override
        protected String doInBackground(String... params) {

            myTimer.cancel();

            List<ParseObject> ob;

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    id);
            query.orderByDescending("_created_at");
            try {
                ob = query.find();

                int i =0;
                message.clear();

                for (ParseObject country : ob) {

                    if (user.equalsIgnoreCase(country.getString("username")))
                        type.add(2);
                    else
                        type.add(1);

                    message.add(country.getString("chat"));

                    i++;
                }
                Collections.reverse(type);
                Collections.reverse(message);


            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            adapter.notifyDataSetChanged();
            myTimer = new Timer();
            myTimerTask= new AutoRefreshTimer();
            myTimer.schedule(myTimerTask, 5000,10000);

        }
    }




    public void send(View v)
    {
        final EditText message = (EditText)findViewById(R.id.messageEditText);

        if(!message.getText().toString().equals(""))
            {
                Thread sendMessage = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            ParseObject send = new ParseObject(id);
                            send.put("username", user);
                            send.put("chat", message.getText().toString());
                            send.save();

                        }catch (Exception e )
                        {
                            Log.e("Error",e.toString());
                        }

                    }
                });
                sendMessage.start();
                message.setText("");

            }
              new refresh().execute();
    }


    class AutoRefreshTimer extends TimerTask {
        long difference;

        @Override
        public void run() {
            List<ParseObject> ob;

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    id);
            query.orderByDescending("_created_at");
            try {
                ob = query.find();

                int i =0;
                message.clear();

                for (ParseObject country : ob) {

                    if (user.equalsIgnoreCase(country.getString("username")))
                        type.add(2);
                    else
                        type.add(1);

                    message.add(country.getString("chat"));

                    i++;
                }
                Collections.reverse(type);
                Collections.reverse(message);


            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }});
        }

    }


}
