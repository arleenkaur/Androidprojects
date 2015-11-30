package midnight.quizapp;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Arleen on 16-04-2015.
 */
public class ParseStart extends Application {

    @Override

    public void onCreate()
    {
        Parse.initialize(this, "HP51iwY8OGiSeFRGxh8c1xiJAPDNzElz5KZQzv9A", "SHv1ZTlHWAcvIWQwlKBfKhLJWznu48bKFrkw0qym");
    }
}
