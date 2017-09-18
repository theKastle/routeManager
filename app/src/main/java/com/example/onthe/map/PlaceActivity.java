package com.example.onthe.map;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.onthe.map.data.Place;
import com.example.onthe.map.data.PlaceContract;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PlaceActivity extends AppCompatActivity implements
        PlaceAdapter.PlaceAdapterOnClickHandler, OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String ANONYMOUS = "anonymous";

    private static final int PLACE_LOADER_ID = 3000;

    private static final int RC_LOG_IN = 1;

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

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Cursor cachedData;

    private PlaceAdapter mPlaceAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private int mPosition = RecyclerView.NO_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        // Initialize FirebaseAuth instance
        mFirebaseAuth = FirebaseAuth.getInstance();

        mPlaceDataRef = mFirebaseDatabase.getReference().child("places");



        mRecyclerView = (RecyclerView) findViewById(R.id.rv_places);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPlaceAdapter = new PlaceAdapter(this, this);

        mRecyclerView.setAdapter(mPlaceAdapter);

        getLoaderManager().initLoader(PLACE_LOADER_ID, null, this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    attachDatabaseReadListener();
                } else {
                    mPlaceAdapter.swapCursor(null);
                    detachDatabaseReadListener();
                    loadLogInView();
                }
            }
        };
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
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPlaceAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String placeId) {
        Intent intentStartDetailActivity = new Intent(this, PlaceDetailActivity.class);
        Uri uriIdClicked = PlaceContract.PlaceEntry.buildUriWithId(placeId);
        intentStartDetailActivity.setData(uriIdClicked);
        startActivity(intentStartDetailActivity);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.place, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
//                Toast.makeText(PlaceActivity.this, "Open", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Toast.makeText(PlaceActivity.this, "Close", Toast.LENGTH_SHORT).show();
                mPlaceAdapter.swapCursor(cachedData);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the final search
                Uri placeUri = PlaceContract.PlaceEntry.CONTENT_URI;
                String selections = PlaceContract.PlaceEntry.COLUMN_NAME + " LIKE ?";
                String[] selectionArgs = new String[]{"%" + query + "%"};
                Cursor searchData = getContentResolver().query(placeUri,
                        PLACE_PROJECTION,
                        selections,
                        selectionArgs,
                        null);

                cachedData = mPlaceAdapter.cachedCursor();
                mPlaceAdapter.swapCursor(searchData);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Text has changed, apply filtering?
                Uri placeUri = PlaceContract.PlaceEntry.CONTENT_URI;
                String selections = PlaceContract.PlaceEntry.COLUMN_NAME + " LIKE ?";
                String[] selectionArgs = new String[]{"%" + newText + "%"};
                Cursor searchData = getContentResolver().query(placeUri,
                        PLACE_PROJECTION,
                        selections,
                        selectionArgs,
                        null);

                cachedData = mPlaceAdapter.cachedCursor();
                mPlaceAdapter.swapCursor(searchData);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
//                Toast.makeText(this, "Sign out", Toast.LENGTH_SHORT).show();
                mFirebaseAuth.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadLogInView() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(intent, RC_LOG_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LOG_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(PlaceActivity.this, "Sign in successfully!", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }

        mPlaceAdapter.swapCursor(null);
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        getContentResolver().delete(PlaceContract.PlaceEntry.CONTENT_URI,
                null,
                null);

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    ContentValues placeValue = new ContentValues();
                    Place newPlace = dataSnapshot.getValue(Place.class);
                    placeValue.put(PlaceContract.PlaceEntry.COLUMN_PLACE_ID, dataSnapshot.getKey());
                    placeValue.put(PlaceContract.PlaceEntry.COLUMN_NAME, newPlace.getPlaceName());
                    placeValue.put(PlaceContract.PlaceEntry.COLUMN_ADDRESS, newPlace.getPlaceAddress());
                    placeValue.put(PlaceContract.PlaceEntry.COLUMN_PHONE, newPlace.getPlacePhone());
                    placeValue.put(PlaceContract.PlaceEntry.COLUMN_RATING, newPlace.getPlaceRating());

                    getContentResolver().insert(PlaceContract.PlaceEntry.CONTENT_URI, placeValue);


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mPlaceDataRef.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mPlaceDataRef.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}
