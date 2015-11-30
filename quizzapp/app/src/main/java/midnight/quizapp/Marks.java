package midnight.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


public class Marks extends ActionBarActivity {

    ArrayList<Card> cards = new ArrayList<>();
    Toolbar mToolbar;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All Marks");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new setGraph().execute();
    }



    class setGraph extends AsyncTask<String,Integer,Integer>
    {

        @Override
        protected Integer doInBackground(String... params) {

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                    "result");
            List<ParseObject> ob;

            query.orderByDescending("_created_at");
            try {
                ob = query.find();


                for (ParseObject object : ob) {

                    ArrayList<BarEntry> entries = new ArrayList<>();
                    ArrayList<String> labels = new ArrayList<String>();
                    String[] daa;

                    String data=object.getString("object");

                    Log.v("String","This shopuld be the object id of two " + data);
                    ParseQuery<ParseObject> query2 = new ParseQuery<>(data);
                    List<ParseObject> ob2;
                    query2.orderByDescending("_created_at");
                    try {
                        ob2 = query2.find();
                        entries.clear();
                        labels.clear();

                        int i =0;
                        for (ParseObject object2 : ob2) {

                            entries.add(new BarEntry(Float.parseFloat((String)object2.get("marks")) ,i));
                            Log.v("Marks",object2.getString("marks"));
                            labels.add(object2.getString("user"));
                            Log.v("Marks",object2.getString("user"));


                            i++;
                        }


                    }catch (Exception e)
                    {
                        Log.e("Error",e.toString());
                        Intent i  = new Intent(Marks.this,Error.class);
                        i.putExtra("code","99");
                        startActivity(i);
                    }

                    Log.e("Breaked","Loop is just breaked");
                    MarksCard card = new MarksCard(Marks.this);
                    card.titletxt=object.getString("name");
                    card.datetxt=object.getString("date");
                    Log.v("Entries","This should be the size of entries "+ entries.size());
                    card.labels=labels;
                    card.entries=entries;
                    cards.add(card);


                }

            } catch (ParseException e) {
                Log.e("Error", e.toString());
            }



            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {

            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(Marks.this,cards);

            CardListView list = (CardListView)findViewById(R.id.menuList);
            list.setBackgroundColor(Color.parseColor("#fafafa"));
            if (list!=null){
                list.setAdapter(mCardArrayAdapter);
            }

            progressBar.setVisibility(View.INVISIBLE);
        }
    }


}
