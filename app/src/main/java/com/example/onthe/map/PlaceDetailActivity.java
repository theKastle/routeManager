package com.example.onthe.map;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.onthe.map.data.PlaceContract;

public class PlaceDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] PLACE_PROJECTION = {
            PlaceContract.PlaceEntry.COLUMN_PLACE_ID,
            PlaceContract.PlaceEntry.COLUMN_NAME,
            PlaceContract.PlaceEntry.COLUMN_ADDRESS,
            PlaceContract.PlaceEntry.COLUMN_PHONE,
            PlaceContract.PlaceEntry.COLUMN_RATING
    };

    public static final int INDEX_PLACE_ID = 0;
    public static final int INDEX_NAME = 1;
    public static final int INDEX_ADDRESS = 2;
    public static final int INDEX_PHONE = 3;
    public static final int INDEX_RATING = 4;

    private static final int ID_PLACE_DETAIL_LOADER = 109;

    private TextView mPlaceDisplayInfo;

    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        mPlaceDisplayInfo = (TextView) findViewById(R.id.tv_display_place_info);

        mUri = getIntent().getData();

        if (mUri == null) {
            throw new NullPointerException("Uri cannot be null");
        }

        getLoaderManager().initLoader(ID_PLACE_DETAIL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_PLACE_DETAIL_LOADER:
                return new CursorLoader(this,
                        mUri,
                        PLACE_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Unknown loader: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean hasValidData = false;

        if (data != null && data.moveToFirst()) {
            hasValidData = true;
        }

        if (!hasValidData) {
            return;
        }

        String placeId = data.getString(INDEX_PLACE_ID);
        String name = data.getString(INDEX_NAME);
        String address = data.getString(INDEX_ADDRESS);
        String phone = data.getString(INDEX_PHONE);
        String rating = data.getString(INDEX_RATING);

        mPlaceDisplayInfo.setText(name + " - " + address + " - " + phone + " - " + placeId + " - " + rating);
        Log.d(PlaceDetailActivity.class.getSimpleName(), mPlaceDisplayInfo.getText().toString());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
