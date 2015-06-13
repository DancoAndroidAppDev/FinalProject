package net.cozz.danco.finalproject.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by danco on 10/25/14.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "beerPhotos";
    public static final String TABLE_BEER_DETAILS = "beer_details";
    public static final String KEY_ID = "_id";
    public static final String KEY_BEER_NAME = "name";
    public static final String KEY_FILE_URI = "file_location";
    public static final String KEY_DESCRIPTION = "beer_description";
    public static final String KEY_PUB_NAME = "pub_name";
    public static final String KEY_LOCATION = "geo_location";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_BEER_DETAILS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_BEER_NAME + " TEXT, " +
                KEY_FILE_URI + " TEXT, " +
                KEY_DESCRIPTION + " TEXT, " +
                KEY_PUB_NAME + " TEXT, " +
                KEY_LOCATION + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEER_DETAILS);

        onCreate(db);
    }
}
