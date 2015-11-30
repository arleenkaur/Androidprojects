package midnight.quizapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayShowTitleEnabled(true);


     //   ParseAnalytics.trackAppOpened(getIntent());

        // inform the Parse Cloud that it is ready for notifications
   //     PushService.setDefaultPushCallback(this, LoginActivity.class);
    //    ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }

    public void signup(View v )
    {
        Intent i = new Intent(this,SignUpActivity.class);
        startActivity(i);
    }

    public void login(View v)
    {
        EditText user = (EditText)findViewById(R.id.etUsername);
        EditText password = (EditText)findViewById(R.id.etPassword);

        String usernametxt = user.getText().toString();
        String passwordtxt = password.getText().toString();


        // Send data to Parse.com for verification
        ParseUser.logInInBackground(usernametxt, passwordtxt,
                new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
        // If user exist and authenticated, send user to Welcome.class
                            Intent intent = new Intent(LoginActivity.this, MainMenu.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),
                                    "Successfully Logged in",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "No such user exist, please signup",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
