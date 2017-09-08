package com.example.onthe.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.onthe.map.data.Place;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PlaceActivity extends AppCompatActivity implements PlaceAdapter.PlaceAdapterOnClickHandler {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPlaceDataRef;
    private ChildEventListener mChildEventListener;

    private PlaceAdapter mPlaceAdapter;
    private RecyclerView mRecyclerView;

    private List<Place> mPlaces;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mPlaceDataRef = mFirebaseDatabase.getReference().child("places");

        mPlaces = new ArrayList<>();


        mRecyclerView = (RecyclerView) findViewById(R.id.rv_places);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPlaceAdapter = new PlaceAdapter(this, this);

        mRecyclerView.setAdapter(mPlaceAdapter);
        mPlaceAdapter.setPlaceData(mPlaces);

        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Place newPlace = dataSnapshot.getValue(Place.class);
//                    Log.d(LOG_TAG, "Key: " + dataSnapshot.getKey() + "\nvalues: " + aPlace.getPlaceName());
                    mPlaces.add(newPlace);
                    mPlaceAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(Place aPlace) {
        Intent intentStartDetailActivity = new Intent(this, PlaceDetailActivity.class);
        String textToPut = aPlace.getPlaceName() +  " - "
                + aPlace.getPlaceAddress() + " - "
                + aPlace.getPlacePhone() + " - "
                + aPlace.getPlaceRating();
        intentStartDetailActivity.putExtra(Intent.EXTRA_TEXT, textToPut);
        startActivity(intentStartDetailActivity);
    }
}
