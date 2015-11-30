package midnight.quizapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;


public class UpcomingTest extends ActionBarActivity {

    ArrayList<String>date = new ArrayList<>();
    ArrayList<String>name = new ArrayList<>();
    ArrayList<String>details = new ArrayList<>();
    ArrayList<Drawable>images = new ArrayList<>();
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_test);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upcoming Tests");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getDb();
    }

    void set()
    {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i =0; i<name.size();i++)
        {
            upcoming_card card = new upcoming_card(this);
            card.dateTxt= String.valueOf(date.get(i));
            card.titleTxt=name.get(i);
            card.contentTxt=details.get(i);
            card.drawable = images.get(i);
            cards.add(card);

        }

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this,cards);

        CardListView list = (CardListView)findViewById(R.id.menuList);
        list.setBackgroundColor(Color.parseColor("#fafafa"));
        if (list!=null){
            list.setAdapter(mCardArrayAdapter);
        }

    }


    class getData extends AsyncTask<String,Integer,String>
    {

        MaterialDialog mProgressDialog;
        boolean error=false;


        @Override
        protected String doInBackground(String... params) {


            List<ParseObject> ob;

            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Upcoming");
            query.orderByDescending("_created_at");
            ColorGenerator generator = ColorGenerator.DEFAULT; // or use DEFAULT

            try {
                ob = query.find();

                name.clear();
                images.clear();
                details.clear();
                date.clear();

                publishProgress(0,ob.size());
                int i =0;

                for (ParseObject test : ob) {

                    details.add(test.getString("TestName"));
                    date.add(String.valueOf(test.getDate("Date")));
                    String temp = test.getString("Subject");
                    name.add(temp);

                    int color2 = generator.getRandomColor();
                    TextDrawable drawable2 = TextDrawable.builder()
                            .buildRound(String.valueOf(temp.charAt(0)), color2);
                    images.add(drawable2);
                    error=false;

                    publishProgress(i);

                    i++;


                }



            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                error=true;
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            if(values.length==2) {
                mProgressDialog= new MaterialDialog.Builder(UpcomingTest.this)
                        .title("Please wait...").progress(false,values[1]).cancelable(false)
                        .content("Please wait while creating the backup")
                        .show();

            }
            else
                mProgressDialog.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String s) {


            if (error)
            {
                new MaterialDialog.Builder(UpcomingTest.this)
                        .title("Error")
                        .content("There is some error occurred while fetching the list!")
                        .positiveText("Ok")
                        .show();
            }
            else
            {
                set();
                Toast.makeText(UpcomingTest.this,"Information Updated",Toast.LENGTH_SHORT).show();
                saveDb();
            }


            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();



        }
    }

    void getDb()
    {

        name.clear();
        images.clear();
        details.clear();
        date.clear();
        ColorGenerator generator = ColorGenerator.DEFAULT; // or use DEFAULT

        SQLiteDatabase db;
        db = openOrCreateDatabase("QUIZ", Context.MODE_PRIVATE, null);
        if(isTableExists(db,"data"))
        {
            Cursor c = db.rawQuery("SELECT * FROM data ;",null);
            if (c.moveToFirst()) {
                do {
                    date.add(c.getString(c.getColumnIndex("date")));
                    name.add(c.getString(c.getColumnIndex("name")));
                    int color2 = generator.getRandomColor();
                    TextDrawable drawable2 = TextDrawable.builder()
                            .buildRound(String.valueOf(c.getString(c.getColumnIndex("name")).charAt(0)), color2);
                    images.add(drawable2);
                    details.add(c.getString(c.getColumnIndex("details")));

                } while (c.moveToNext());
                db.close();
            }

            set();
        }


        new getData().execute();


    }

    void saveDb()
    {
        SQLiteDatabase db;
        db= openOrCreateDatabase("QUIZ", Context.MODE_PRIVATE, null);
        if(isTableExists(db,"data"))
            db.execSQL("DROP TABLE data;");


            db.execSQL("CREATE TABLE IF NOT EXISTS data(date VARCHAR,name VARCHAR,details VARCHAR);");

            for (int i =0;i<date.size();i++)
            {
                db.execSQL("INSERT INTO data VALUES ('"+date.get(i)+"', '"+name.get(i)+"', '"+details.get(i)+"') ");

            }



    }


    boolean isTableExists(SQLiteDatabase db, String tableName)
    {
        if (tableName == null || db == null || !db.isOpen())
        {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst())
        {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
}
