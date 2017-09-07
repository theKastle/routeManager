package com.example.onthe.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class PlaceDetailActivity extends AppCompatActivity {

    private TextView mPlaceDisplayInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        mPlaceDisplayInfo = (TextView) findViewById(R.id.tv_display_place_info);

        Intent parentIntent = getIntent();

        if (parentIntent != null) {
            if (parentIntent.hasExtra(Intent.EXTRA_TEXT)) {
                mPlaceDisplayInfo.setText(parentIntent.getStringExtra(Intent.EXTRA_TEXT));
            }
        }
    }
}
