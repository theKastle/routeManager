package com.example.onthe.map;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

        mPlaceName = (TextView) findViewById(R.id.tv_place_name);
        mPlacePhone = (TextView) findViewById(R.id.tv_phone);
        mPlaceAddress = (TextView) findViewById(R.id.tv_adress);

        mUri = getIntent().getData();

        if (mUri == null) {
            throw new NullPointerException("Uri cannot be null");
        }

        getLoaderManager().initLoader(ID_PLACE_DETAIL_LOADER, null, this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void sendRequest(String address) {
        String origin = "Khoa hoc tu nhien Nguyen Van Cu";
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(PlaceDetailActivity.class.getSimpleName(), address);
        if (address.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

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
        Log.d(PlaceDetailActivity.class.getSimpleName(), address);
        String phone = data.getString(INDEX_PHONE);

        String rating = data.getString(INDEX_RATING);

        sendRequest(address);

        mPlaceAddress.setText(address);
        mPlacePhone.setText(phone);
        mPlaceName.setText(name);
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
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

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
        progressDialog.dismiss();

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
}
