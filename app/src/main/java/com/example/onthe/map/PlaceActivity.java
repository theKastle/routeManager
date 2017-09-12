package com.example.onthe.map;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.onthe.map.data.PlaceContract;
import com.example.onthe.map.utils.FakeDataUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PlaceActivity extends AppCompatActivity implements
        PlaceAdapter.PlaceAdapterOnClickHandler, OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

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

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPlaceDataRef;
    private ChildEventListener mChildEventListener;

    private static final int PLACE_LOADER_ID = 3000;

    private PlaceAdapter mPlaceAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

//        FakeDataUtils.createFakeData(this);
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//
//        mPlaceDataRef = mFirebaseDatabase.getReference().child("places");


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_places);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPlaceAdapter = new PlaceAdapter(this, this);

        mRecyclerView.setAdapter(mPlaceAdapter);

        showLoading();

        getLoaderManager().initLoader(PLACE_LOADER_ID, null, this);

//        if (mChildEventListener == null) {
//            mChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    Place newPlace = dataSnapshot.getValue(Place.class);
////                    Log.d(LOG_TAG, "Key: " + dataSnapshot.getKey() + "\nvalues: " + aPlace.getPlaceName());
//                    mPlaces.add(newPlace);
//                    mPlaceAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            };
//
//            mPlaceDataRef.addChildEventListener(mChildEventListener);
//        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case PLACE_LOADER_ID:
                Uri placeUri = PlaceContract.PlaceEntry.CONTENT_URI;

            return new CursorLoader(this,
                    placeUri,
                    PLACE_PROJECTION,
                    null,
                    null,
                    null);

            default:
                throw new RuntimeException("Unknown Loader ID: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPlaceAdapter.swapCursor(data);

        if (mPosition == RecyclerView.NO_POSITION)
            mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);

        if (data != null) {
            showPlaceData();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPlaceAdapter.swapCursor(null);
    }

    @Override
    public void onClick(int placeId) {
        Intent intentStartDetailActivity = new Intent(this, PlaceDetailActivity.class);
        Uri uriIdClicked = PlaceContract.PlaceEntry.buildUriWithId(placeId);
        intentStartDetailActivity.setData(uriIdClicked);
        startActivity(intentStartDetailActivity);
    }

    private void showPlaceData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
