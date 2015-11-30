package midnight.quizapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.mikephil.charting.data.BarEntry;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class PostsList extends ActionBarActivity {

    ListView listView;
    ArrayList<String> postTitle=new ArrayList<>();
    Toolbar mToolbar;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);
        listView=(ListView)findViewById(R.id.postList);
        mToolbar = (Toolbar) findViewById(R.id.my_toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Discussion Groups");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        new set().execute();
    }

    class set extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... params) {

            List<ParseObject> ob;
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("posts");
            query.orderByDescending("_created_at");
            try {
                ob = query.find();

                int i =0;

                for (ParseObject posts : ob) {
                    postTitle.add((String) posts.get("title"));
                    i++;
                }}catch (Exception e)
            {
                Log.e("Error",e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            progressBar.setVisibility(View.INVISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(PostsList.this,android.R.layout.simple_list_item_2, android.R.id.text1,postTitle);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent i = new Intent(PostsList.this,ChatActivity.class);
                    i.putExtra("postId",postTitle.get(position));
                    startActivity(i);
                }
            });



        }
    }







    public void CreatePost(View v)
    {

        new MaterialDialog.Builder(this)
                .title("New thread")
                .content("Create a new Thread")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT)
                .input(" Thread Name", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {


                        try {

                            if (input.toString().contains(" "))
                                Toast.makeText(PostsList.this,"Sorry you can only use A-a,0-9 in the name of thread",Toast.LENGTH_LONG).show();

                                else
                            {
                                ParseUser pUser = ParseUser.getCurrentUser();
                                String nameText = pUser.getString("name");


                                final ParseObject register = new ParseObject("posts");
                                register.put("name", nameText);
                                register.put("title", input.toString());
                                register.save();

                                final ParseObject post = new ParseObject(input.toString());
                                post.save();


                                new set().execute();
                            }


                        } catch (Exception e) {

                            Log.e("Error", e.toString());
                        }
                    }
                }).show();

    }



}
