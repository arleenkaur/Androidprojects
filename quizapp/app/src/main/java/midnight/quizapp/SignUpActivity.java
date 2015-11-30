package midnight.quizapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;




public class SignUpActivity extends ActionBarActivity {


     String type;
    boolean imageUpload;
    Bitmap pic;

    String[] s = new String[]{"MA","MS","MBA","Phd","MPA","MSW"};
    private final int SELECT_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);




    }

    public void register(View v)
    {
        EditText sId = (EditText)findViewById(R.id.etUsername);
        EditText password = (EditText)findViewById(R.id.etPassword);
        EditText phone = (EditText)findViewById(R.id.etPhone);
        EditText email = (EditText)findViewById(R.id.etEmail);
        EditText name = (EditText)findViewById(R.id.etName);
        EditText cnfrmPassword = (EditText)findViewById(R.id.etCnfrmPassword);
        final String SiDTxt = sId.getText().toString();
        String passwordtxt = password.getText().toString();
        String phonetxt = phone.getText().toString();
        String nametxt = name.getText().toString();
        String emailtxt = email.getText().toString();
        String cnfrmpasstxt = cnfrmPassword.getText().toString();





        if (SiDTxt.equals("")||passwordtxt.equals("")||phonetxt.equals("")||nametxt.equals("")||emailtxt.equals("")||type.equals(""))
            Toast.makeText(this,"Looks like you forgot something!",Toast.LENGTH_LONG).show();


        else if (passwordtxt.equals(cnfrmpasstxt))
            {
                ParseUser user = new ParseUser();
                user.setUsername(SiDTxt);
                user.setPassword(passwordtxt);
                user.setEmail(emailtxt);
                user.put("phone", phonetxt);
                user.put("name", nametxt);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {

                        if (e == null) {
                            // Show a simple Toast message upon successful registration

                            ParsePush.subscribeInBackground(type);
                            if(imageUpload)
                                upload(SiDTxt);

                            Toast.makeText(getApplicationContext(),
                                    "Successfully Signed up, please log in.",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Sign up Error : "+e.toString(), Toast.LENGTH_LONG)
                                    .show();

                        }

                    }
                });

            }
        else
            {

                Toast.makeText(this,"Looks Like confirm password is something else!",Toast.LENGTH_LONG).show();

            }

    }

    public void setClass(View v)
    {
        final EditText etClass = (EditText)findViewById(R.id.etClass);



                new MaterialDialog.Builder(SignUpActivity.this)
                        .title("Choose class")
                        .items(s)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                /**
                                 * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                                 * returning false here won't allow the newly selected radio button to actually be selected.
                                 **/
                                type=text.toString();
                                etClass.setText(type);


                                return true;
                            }
                        })
                        .positiveText(R.string.choose)
                        .show();

            }

    public void profile(View v)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        imageUpload=true;
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                       /* int height = selectedImage.getHeight();
                        int width = selectedImage.getWidth();
                        Bitmap bitmap = Bitmap.createScaledBitmap(selectedImage, height/50, width/50, true);*/

                        ExifInterface ei = new ExifInterface(imageUri.getPath());
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                            Bitmap rotated=null;
                        switch(orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotated=rotateImage(selectedImage, 90);

                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                               rotated= rotateImage(selectedImage, 180);
                                break;
                            default:
                                rotated=selectedImage;
                                break;
                            // etc.
                        }

                        pic = ThumbnailUtils.extractThumbnail(selectedImage, 60, 60);
                        ImageView img=(ImageView)findViewById(R.id.img_profile);
                        img.setImageBitmap(rotated);
                       // img.setImageResource(R.mipmap.ic_launcher);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        }
    }


    public static Bitmap rotateImage(Bitmap src, float degree)
    {
        // create new matrix
        Matrix matrix = new Matrix();

        matrix.postRotate(degree);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(src,src.getWidth(),src.getHeight(),true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);

        return rotatedBitmap;
    }





    void upload(String id)
    {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        pic.compress(Bitmap.CompressFormat.PNG, 1, stream);
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile(id, image);
        // Upload the image into Parse Cloud
        file.saveInBackground();

        // Create a New Class called "ImageUpload" in Parse
        final ParseObject imgupload = new ParseObject("ImageUpload");


        // Create a column named "ImageName" and set the string
        imgupload.put("ImageName", id);

        // Create a column named "ImageFile" and insert the image
        imgupload.put("ImageFile", file);

        // Create the class and the columns
        imgupload.saveInBackground();



    }


}