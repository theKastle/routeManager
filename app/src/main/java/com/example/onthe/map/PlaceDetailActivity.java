package com.example.onthe.map;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.onthe.map.MapUtils.DirectionFinder;
import com.example.onthe.map.MapUtils.DirectionFinderListener;
import com.example.onthe.map.MapUtils.Route;
import com.example.onthe.map.data.PlaceContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity
        implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor>, DirectionFinderListener {

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

    private TextView mPlaceName;
    private TextView mPlacePhone;
    private TextView mPlaceAddress;
    private RatingBar mRatingBar;

    private String mPlaceSummary;

    private Uri mUri;


    private ProgressDialog progressDialog;
    private GoogleMap mMap;
    private List<Route> myRoutes = new ArrayList<>();

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        mPlaceName = (TextView) findViewById(R.id.place_text_view_detail);
        mPlacePhone = (TextView) findViewById(R.id.phone_text_view_detail);
        mPlaceAddress = (TextView) findViewById(R.id.address_text_view_detail);
        mRatingBar = (RatingBar) findViewById(R.id.rating_bar);

        mUri = getIntent().getData();

        if (mUri == null) {
            throw new NullPointerException("Uri cannot be null");
        }

        getLoaderManager().initLoader(ID_PLACE_DETAIL_LOADER, null, this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mPlacePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startCallIntent = new Intent(Intent.ACTION_DIAL);
                startCallIntent.setData(Uri.parse("tel:" + mPlacePhone.getText().toString()));
                if (startCallIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(startCallIntent);
                }
            }
        });


    }

    private void sendRequest(String address) {
        String origin = "Khoa hoc tu nhien Nguyen Van Cu";
//        if (origin.isEmpty()) {
//            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (address.isEmpty()) {
//            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
//            return;
//        }

        try {
            new DirectionFinder(this, origin, address).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
        float rating = data.getFloat(INDEX_RATING);

        sendRequest(address);

        mPlaceAddress.setText(address);
        mPlacePhone.setText(phone);
        mPlaceName.setText(name);
        mRatingBar.setRating(rating);

        mPlaceSummary = name + " - "
                + "Address: " + address + " - "
                +  "Tel: " + phone;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcmus = new LatLng(10.762963, 106.682394);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onDirectionFinderStart() {
//        progressDialog = ProgressDialog.show(this, "Please wait.",
//                "Finding direction!", true);

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait.");
            progressDialog.setMessage("Finding direction!");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        drawRoutes(routes);
        this.myRoutes = routes;
    }

    private void drawRoutes(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(createMapLatLng(route.startLocation), 16));
           // ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
           // ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(createMapLatLng(route.startLocation))));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(createMapLatLng(route.endLocation))));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.rgb(131, 185, 255)).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(createMapLatLng(route.points.get(i)));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    private LatLng createMapLatLng(com.example.onthe.map.MapUtils.LatLng latLng){
        com.google.android.gms.maps.model.LatLng mapsLatLng =
                new com.google.android.gms.maps.model.LatLng(latLng.getLatitude(),
                        latLng.getLongitude());

        return  mapsLatLng;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                Intent shareIntent = createShareIntent();
                startActivity(shareIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Intent createShareIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mPlaceSummary)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }
}
