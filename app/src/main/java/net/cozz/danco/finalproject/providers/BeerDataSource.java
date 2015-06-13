package net.cozz.danco.finalproject.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by danco on 10/26/14.
 */
public class BeerDataSource {

    private SQLiteDatabase database;
    private DBHandler dbHelper;
    private String[] columns = {
            DBHandler.KEY_ID,
            DBHandler.KEY_BEER_NAME,
            DBHandler.KEY_FILE_URI,
            DBHandler.KEY_DESCRIPTION,
            DBHandler.KEY_PUB_NAME,
            DBHandler.KEY_LOCATION};


    public BeerDataSource(Context context) {
        dbHelper = new DBHandler(context);
    }


    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


    public void close() {
        dbHelper.close();
    }


    public BeerData addBeerData(BeerData beer) {
        return addBeerData(beer.getName(),
                beer.getImageFileUri(),
                beer.getDescription(),
                beer.getPubName(),
                beer.getLocation() == null ? "" : beer.getLocation().toString());
    }


    public BeerData addBeerData(String beerName, String imageFileUri, String description, String pubName, String location) {
        BeerData beerData = null;
        ContentValues values = new ContentValues();
        values.put(DBHandler.KEY_BEER_NAME, beerName);
        values.put(DBHandler.KEY_FILE_URI, imageFileUri);
        values.put(DBHandler.KEY_DESCRIPTION, description);
        values.put(DBHandler.KEY_PUB_NAME, pubName);
        values.put(DBHandler.KEY_LOCATION, location);
        long insertId = database.insert(DBHandler.TABLE_BEER_DETAILS, null, values);
        Cursor cursor = database.query(DBHandler.TABLE_BEER_DETAILS, columns,
                DBHandler.KEY_ID + "=" + insertId, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            beerData = new BeerData(cursor);
            cursor.close();
        }

        return beerData;
    }


    public List<BeerData> getBeerDataList() {
        List<BeerData> captials = new ArrayList<BeerData>();

        Cursor cursor = database.query(DBHandler.TABLE_BEER_DETAILS, columns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BeerData beerData = new BeerData(cursor);
            captials.add(beerData);
            cursor.moveToNext();
        }

        cursor.close();

        return captials;
    }


    public BeerData getBeerData(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHandler.TABLE_BEER_DETAILS,
                columns,
                DBHandler.KEY_BEER_NAME + " =?" + name,
                null, null, null, null);
        cursor.moveToFirst();
        BeerData beerData = new BeerData(cursor);

        return beerData;
    }
}
