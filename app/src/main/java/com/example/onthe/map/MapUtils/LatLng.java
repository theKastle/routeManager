package com.example.onthe.map.MapUtils;

/**
 * Created by onthe on 6/18/2017.
 */

public class LatLng {
    private Double latitude;
    private Double longitude;

    public LatLng(){}

    public LatLng(Double lat, Double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}