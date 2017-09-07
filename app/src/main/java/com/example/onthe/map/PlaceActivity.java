package com.example.onthe.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.onthe.map.data.Place;
import com.example.onthe.map.utils.FakeDateUtils;

public class PlaceActivity extends AppCompatActivity implements PlaceAdapter.PlaceAdapterOnClickHandler {
    private PlaceAdapter mPlaceAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_places);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPlaceAdapter = new PlaceAdapter(this, this);

        mRecyclerView.setAdapter(mPlaceAdapter);
        mPlaceAdapter.setPlaceData(FakeDateUtils.insertFakePlaces());
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
