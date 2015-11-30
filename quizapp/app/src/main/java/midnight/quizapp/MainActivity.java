package midnight.quizapp;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;


public class MainActivity extends ActionBarActivity {

    String data;
    String title;
    String time;
    String path;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {

            Bundle b = getIntent().getExtras();

            if (!(b ==null))
            {
                JSONObject jsonObject = new JSONObject(b.getString("com.parse.Data"));
                data = jsonObject.getString("data");
                title = jsonObject.getString("title");
                time = jsonObject.getString("time");
                Log.v("Its all three", data + " " + time + title);
                Thread t;
                t = new  Thread(new Runnable() {
                    @Override
                    public void run() {

                        boolean error=false;
                        ParseQuery<ParseObject> query = new ParseQuery<>("Tests");

                        query.getInBackground(data,
                                new GetCallback<ParseObject>() {

                                    public void done(ParseObject object,
                                                     ParseException e) {


                                        if (object == null) {
                                            Log.e("test", "The object was not found...");
                                        }

                                        else
                                        {
                                            ParseFile fileObject = (ParseFile) object.get("file");
                                            fileObject.getDataInBackground(new GetDataCallback() {

                                                public void done(byte[] bytes,ParseException e)

                                                {

                                                    if (e == null) {
                                                        try {
                                                            Log.d("test","We've got data in data.");


                                                            File dir = new File(Environment.getExternalStorageDirectory().getPath()+"/Android/Quiz/Temp/");
                                                            if (!dir.exists())
                                                                dir.mkdirs();


                                                            File f =new File(dir.getPath()+"/"+data+".xml");
                                                            if (!f.exists())
                                                                f.createNewFile();

                                                            FileOutputStream fileOuputStream =
                                                                    new FileOutputStream(f.getAbsolutePath());
                                                            fileOuputStream.write(bytes);
                                                            fileOuputStream.close();
                                                            path=f.getAbsolutePath();


                                                            Intent i = new Intent(MainActivity.this,TestActivity.class);
                                                            i.putExtra("id",data);
                                                            i.putExtra("path", path);
                                                            i.putExtra("title",title);
                                                            i.putExtra("time",time);
                                                            startActivity(i);

                                                        }catch (Exception exception)
                                                        {

                                                            Log.e("Error",exception.toString());
                                                            Intent i  = new Intent(MainActivity.this,Error.class);
                                                            i.putExtra("code","500");
                                                            startActivity(i);
                                                        }


                                                    } else {
                                                        Log.e("test", "There was a problem downloading the data.");
                                                        Intent i  = new Intent(MainActivity.this,Error.class);
                                                        i.putExtra("code","500");
                                                        startActivity(i);
                                                    }
                                                }
                                            });
                                        }


                                    }
                                });
                    }
                });

                t.start();

            }

            else
            {

               Thread t;
                t = new  Thread(new Runnable() {
                    @Override
                    public void run() {
                        ParseUser currentUser = ParseUser.getCurrentUser();

                        if (currentUser != null) {
                            // Send logged in users to Welcome.class
                            Intent intent = new Intent(MainActivity.this, MainMenu.class);
                            startActivity(intent);
                            finish();
                        } else {

                            // Send user to LoginSignupActivity.class
                            Intent intent = new Intent(MainActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });

                t.start();


            }




        } catch (Exception e) {
            e.printStackTrace();
            Intent i = new Intent(MainActivity.this,Error.class);
            i.putExtra("code","300");
            startActivity(i);

        }
    }




}