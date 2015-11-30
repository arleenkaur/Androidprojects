package com.ee5453.mytwitter;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

/**
 * Created by kkx358 on 4/16/2015.
 */
public class StatusProvider extends ContentProvider {
    public static final String AUTHORITY = "content://com.ee5453.mytwitter";
    public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);


    public static final String DB_NAME = "twitterStatus.db";
    static int DB_VERSION = 2;

    public static final String TABLE_NAME = "twitterStatus";
    public static final String C_ID = "_id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_TEXT = "status_text";
    public static final String C_USER = "user_name";

    TwitterDBHelper dbHelper;
    SQLiteDatabase statusDB;

    @Override
    public boolean onCreate() {
        dbHelper=new TwitterDBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        statusDB=dbHelper.getWritableDatabase();
        long id=statusDB.insertWithOnConflict(TABLE_NAME,null,
                values,SQLiteDatabase.CONFLICT_IGNORE);
        Uri itemUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().
                    notifyChange(itemUri, null);
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor tweets;
        statusDB=dbHelper.getReadableDatabase();
        tweets = statusDB.query(TABLE_NAME,
                projection,selection,selectionArgs,null,null,sortOrder);


               return tweets;
    }


   /* @Override
    public Cursor FilteredQuery(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor tweets;
        statusDB=dbHelper.getReadableDatabase();
        tweets = statusDB.query(TABLE_NAME,
                projection,selection,selectionArgs,null,null,sortOrder);

        tweets = statusDB.query(true, TABLE_NAME, new String[] {KEY_ROWID,
                        KEY_CODE, KEY_NAME, KEY_CONTINENT, KEY_REGION},
                KEY_NAME + " like '%" + inputText + "%'", null,
                null, null, null, null);
        return tweets;
    }*/



    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        statusDB=dbHelper.getReadableDatabase();
        String text = values.getAsString("value");
        Log.e("Updated","Now setting the Text = "+text+" And _id is "+selection);
        statusDB.execSQL("UPDATE "+TABLE_NAME+" SET "+C_TEXT+"= '"+text+"' WHERE _id="+selection+";");
        statusDB.close();
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        statusDB=dbHelper.getReadableDatabase();
        statusDB.execSQL("DELETE FROM "+TABLE_NAME+" WHERE _id="+selection+";");
        statusDB.close();
        return 0;
    }
}

class TwitterDBHelper extends SQLiteOpenHelper {

    static String TAG = "TwitterDBHelper";

    /*       public TwitterDBHelper(Context context, String name,
                                  SQLiteDatabase.CursorFactory factory, int version) {
               super(context, name, factory, version);
           }
   */
    public TwitterDBHelper(Context context) {
        super(context,StatusProvider.DB_NAME, null, StatusProvider.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s integer primary key, " +
                        "%s integer, %s text, %s text)",
                StatusProvider.TABLE_NAME,StatusProvider.C_ID,
                StatusProvider.C_CREATED_AT,StatusProvider.C_TEXT,
                StatusProvider.C_USER);
        Log.d(TAG, "onCreate: " + sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //don't do this
        String sql = String.format("drop table if exists %s",
                StatusProvider.TABLE_NAME);
        db.execSQL(sql);
        Log.d(TAG,"onUpgrade");
        onCreate(db);
    }
}