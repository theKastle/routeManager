package com.example.onthe.map.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by phucle on 9/8/17.
 */

public class PlaceDbHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "place.db";

    private static final int DATABASE_VERSION = 1;

    public PlaceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_PLACE_TABLE =
                "CREATE TABLE " + PlaceContract.PlaceEntry.TABLE_NAME + " (" +
                PlaceContract.PlaceEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                PlaceContract.PlaceEntry.COLUMN_PLACE_ID    + " VARCHAR(255) NOT NULL, "                     +
                PlaceContract.PlaceEntry.COLUMN_NAME        + " VARCHAR(255) NOT NULL, "                +
                PlaceContract.PlaceEntry.COLUMN_ADDRESS     + " VARCHAR(255) NOT NULL,"                 +
                PlaceContract.PlaceEntry.COLUMN_PHONE       + " VARCHAR(20) NOT NULL,"                  +
                PlaceContract.PlaceEntry.COLUMN_RATING      + " FLOAT DEFAULT 0.0);";

        db.execSQL(SQL_CREATE_PLACE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PlaceContract.PlaceEntry.TABLE_NAME);
        onCreate(db);
    }
}
