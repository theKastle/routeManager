package com.example.onthe.map.data;

/**
 * Created by phucle on 9/6/17.
 */

public class Place {
    private String placeName;
    private String placeAddress;
    private String placePhone;
    private float placeRating;

    public Place() {

    }

    public Place(String name, String address,
                 String phone, float rating) {
        placeName = name;
        placeAddress = address;
        placePhone = phone;
        placeRating = rating;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlacePhone() {
        return placePhone;
    }

    public void setPlacePhone(String placePhone) {
        this.placePhone = placePhone;
    }

    public float getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(float placeRating) {
        this.placeRating = placeRating;
    }
}
