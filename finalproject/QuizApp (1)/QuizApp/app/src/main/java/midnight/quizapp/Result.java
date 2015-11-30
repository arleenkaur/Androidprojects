package midnight.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Result extends ActionBarActivity {

    String id ;
    String marks;
    String time;
    TextView graphAlt,wait;
    Timer myTimer;
    BarChart graph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent i = getIntent();
        id = i.getStringExtra("id");
        Log.v("log",id);
        TextView tv = (TextView)findViewById(R.id.title);
        Typeface font = Typeface.createFromAsset(getAssets(), "segoeui.ttf");
        tv.setTypeface(font);
        TextView totalTv = (TextView)findViewById(R.id.tvTotal);
        TextView correctTv = (TextView)findViewById(R.id.tvMarks);
        TextView InCorrectTv = (TextView)findViewById(R.id.tvIncorrect);
        TextView tv1 = (TextView)findViewById(R.id.tv1);
        TextView tv2 = (TextView)findViewById(R.id.tv2);
        TextView tv3 = (TextView)findViewById(R.id.tv3);
        graphAlt = (TextView)findViewById(R.id.graphAlt);
        wait = (TextView)findViewById(R.id.wait);
        graph = (BarChart)findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);
        tv1.setTypeface(font);
        tv2.setTypeface(font);
        tv3.setTypeface(font);
        graphAlt.setTypeface(font);
        wait.setTypeface(font);
        totalTv.setTypeface(font);
        correctTv.setTypeface(font);
        InCorrectTv.setTypeface(font);


        String total = i.getStringExtra("total");
        marks = i.getStringExtra("marks");
        time = i.getStringExtra("time");
        totalTv.setText(total);
        correctTv.setText(marks);
        int incorrect = Integer.parseInt(total)-Integer.parseInt(marks);
        Log.e("TOTal",total+" Marks"+marks);
        InCorrectTv.setText(String.valueOf(incorrect));

        tv.setText("Congrats you scored!");

        TextDrawable mark = TextDrawable.builder()
                .buildRound(marks, Color.parseColor("#03a9f4"));

        ImageView Marks = (ImageView)findViewById(R.id.marks);
        Marks.setImageDrawable(mark);

        save();
        MyTimerTask myTimerTask = new MyTimerTask();
        myTimer = new Timer();
        myTimer.schedule(myTimerTask, 1000, 1000);
    }


    void save() {
        try {

            ParseUser pUser = ParseUser.getCurrentUser();
            String name = pUser.getString("name");

            final ParseObject saveMarks = new ParseObject("Test"+id);

            saveMarks.put("marks", marks);
            saveMarks.put("user", name);
            saveMarks.save();
            ParseObject testName = new ParseObject("result");
            testName.put("object","Test"+id);
            testName.save();

        } catch (Exception e) {

            Log.e("Error",e.toString());
        }

    }


    void graph()
    {
        myTimer.cancel();
      List<ParseObject> ob;
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Test"+id);
        query.orderByDescending("_created_at");
        try {
            ob = query.find();

            int i =0;

            for (ParseObject country : ob) {
                labels.add((String) country.get("user"));
                entries.add(new BarEntry(Float.parseFloat((String)country.get("marks")) ,i));
                  // Doing double conversion because direct conversion is not working!
            i++;
            }
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }


        wait.setVisibility(View.INVISIBLE);
        graphAlt.setVisibility(View.INVISIBLE);


        BarDataSet dataset = new BarDataSet(entries, "Marks");

        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(labels, dataset);
        graph.setData(data);

        graph.animateXY(2000, 2000);
        graph.setDescription("Marks of all students");
        graph.setVisibility(View.VISIBLE);

    }


    class MyTimerTask extends TimerTask {
        long difference;

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat("HH:mm:ss");

            final String strDate = simpleDateFormat.format(calendar.getTime());



            try {
                Date date1 = simpleDateFormat.parse(time);
                Date date2 = simpleDateFormat.parse(strDate);
                difference = date1.getTime() - date2.getTime();
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    difference = difference/1000;
                    if (difference<0)
                        graph();
                    graphAlt.setText(String.valueOf(difference) + "  Secs");
                }});
        }

    }


}
