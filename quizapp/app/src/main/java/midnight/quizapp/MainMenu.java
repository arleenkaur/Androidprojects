package midnight.quizapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardGridView;
import it.gmariotti.cardslib.library.view.CardListView;


public class MainMenu extends ActionBarActivity {

    String[] text = new String[]{"Upcoming Tests","Marks","Discuss","Help"};
    int[] imageId = new int[]{R.mipmap.test,R.mipmap.analytics,R.mipmap.bubbles,R.mipmap.help};
    Bitmap profile;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        new setImage().execute();
        ArrayList<Card> cards = new ArrayList<>();

        for (int i =0;i<4;i++)
        {
            CustomCard card = new CustomCard(this);
            card.image=imageId[i];
            card.text=text[i];
            card.setShadow(true);
            card.setId(String.valueOf(i));
            Log.e("LOOP","Loop is running");


            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                   if(card.getId().equals("0"))
                        {
                            Intent intent = new Intent(MainMenu.this,UpcomingTest.class);
                            startActivity(intent);
                        }
                    else if (card.getId().equals("1"))
                        {
                            Intent intent = new Intent(MainMenu.this,Marks.class);
                            startActivity(intent);
                        }
                    else if (card.getId().equals("2"))
                        {
                            Intent i = new Intent(MainMenu.this,PostsList.class);
                            startActivity(i);

                        }

                    else if (card.getId().equals("3"))
                        {

                            Intent i = new Intent(MainMenu.this,Help.class);
                            startActivity(i);

                        }

                }
            });

            cards.add(card);

        }



        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this,cards);

        CardListView list = (CardListView)findViewById(R.id.menuList);
        list.setBackgroundColor(Color.parseColor("#fafafa"));
        if (list!=null){
            list.setAdapter(mCardArrayAdapter);
        }

    }



    public String getId(String className, String itemSelected, String colName) {


        String objectId="";
        ParseQuery query = new ParseQuery(className);
        query.whereEqualTo(colName, itemSelected);
        try {
            List<ParseObject> dataHolder = query.find();

             objectId = dataHolder.get(0).getObjectId();

            //  ParseObject fileHolder = query.get(objectId);

        } catch (ParseException e) {
            Log.e("Error",e.toString());
            // message if faile
        }

        return objectId;
    }








    class setImage extends AsyncTask<String,Integer,Integer>
    {

        @Override
        protected Integer doInBackground(String... params) {


            ParseQuery<ParseObject> query = new ParseQuery<>(
                    "ImageUpload");



            // Locate the objectId from the class
            ParseUser pUser= ParseUser.getCurrentUser();
            String username = pUser.getUsername();

            TextView name = (TextView)findViewById(R.id.userName);
            name.setText(pUser.getString("name"));



            String id = getId("ImageUpload",username,"ImageName");


            query.getInBackground(id,
                    new GetCallback<ParseObject>() {

                        public void done(ParseObject object,
                                         ParseException e) {
                            // TODO Auto-generated method stub

                            // Locate the column named "ImageName" and set
                            // the string
                            if (object == null) {
                                Log.d("test", "The object was not found...");
                                Toast.makeText(MainMenu.this,"Object not found",Toast.LENGTH_LONG).show();
                            }

                            else
                            {
                                ParseFile fileObject = (ParseFile) object.get("ImageFile");
                                fileObject.getDataInBackground(new GetDataCallback() {

                                    public void done(byte[] data, ParseException e)

                                    {
                                        if (e == null) {
                                            Log.d("test", "We've got data in data.");
                                            // Decode the Byte[] into
                                            // Bitmap
                                            profile = BitmapFactory
                                                    .decodeByteArray(
                                                            data, 0,
                                                            data.length);

                                            publishProgress(0); // using publish progress to update image because doinbackground is on other thread


                                        } else {
                                            Log.d("test",
                                                    "There was a problem downloading the data.");
                                        }
                                    }
                                });
                            }


                        }
                    });

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            CircleImageView profileImage = (CircleImageView) findViewById(R.id.profile_image);

            // Set the Bitmap into the
            // ImageView
            profileImage.setImageBitmap(profile);
        }
    }



}
