package com.example.onthe.map.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.onthe.map.data.PlaceContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phucle on 9/6/17.
 */

public final class FakeDataUtils {

    private static ContentValues createFakePlaceContentValues(int placeId) {
        ContentValues fakePlace = new ContentValues();
        fakePlace.put(PlaceContract.PlaceEntry.COLUMN_PLACE_ID, placeId);
        fakePlace.put(PlaceContract.PlaceEntry.COLUMN_NAME, "Bun Bo");
        fakePlace.put(PlaceContract.PlaceEntry.COLUMN_ADDRESS, "123 Nguyen Dinh Chieu");
        fakePlace.put(PlaceContract.PlaceEntry.COLUMN_PHONE, "123456789");
        fakePlace.put(PlaceContract.PlaceEntry.COLUMN_RATING, (float)4.2);
        return fakePlace;
    }

    public static void createFakeData(Context context) {
        List<ContentValues> fakeValues = new ArrayList<ContentValues>();

        for (int i = 0; i < 10; ++i) {
            fakeValues.add(FakeDataUtils.createFakePlaceContentValues((int) Math.random() * (i + 2) * 5));
            Log.d("FakeDataUtils", fakeValues.toString());
        }

        Log.d("FakeDataUtils", "Size: " + fakeValues.size());

        ContentResolver contentResolver = context.getContentResolver();

        int row = contentResolver.bulkInsert(
                PlaceContract.PlaceEntry.CONTENT_URI,
                fakeValues.toArray(new ContentValues[10]));

        Log.d("FakeDataUtils", "# new rows: " + row);
    }
}
