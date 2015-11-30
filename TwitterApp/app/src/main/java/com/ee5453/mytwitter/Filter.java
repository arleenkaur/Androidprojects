package com.ee5453.mytwitter;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ee5453.mytwitter.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Filter extends ActionBarActivity {

    EditText early,latest,name;
    SimpleDateFormat sdfDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        early=(EditText)findViewById(R.id.editText_early);
        latest=(EditText)findViewById(R.id.editText_latest);
        name=(EditText)findViewById(R.id.editText_name);
         sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String newtime =  sdfDateTime.format(new Date(System.currentTimeMillis()));
        early.setText(newtime);
        latest.setText(newtime);


    }


   public void search(View v)
   {
       String user= name.getText().toString();
       Intent i = new Intent(this,FilteredTweets.class);
       i.putExtra("user",user);
       Date date = null;
       Date date2 = null;
       try {
           date = sdfDateTime.parse(early.getText().toString());
           date2 = sdfDateTime.parse(latest.getText().toString());
       } catch (ParseException e) {
           e.printStackTrace();
       }
       date.getTime();
       i.putExtra("early",String.valueOf(date.getTime()));
       i.putExtra("latest",String.valueOf(date2.getTime()));
       startActivity(i);

   }
}
