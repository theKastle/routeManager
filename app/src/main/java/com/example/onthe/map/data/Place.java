package com.example.onthe.map.data;

/**
 * Created by phucle on 9/6/17.
 */

public class Place {
    private String placeName;
    private String placeAddress;
    private String placePhone;
    private double placeRating;
    private String photoUrl;

    public Place(String name, String address,
                 String phone, double rating, String photo) {
        placeName = name;
        placeAddress = address;
        placePhone = phone;
        placeRating = rating;
        photoUrl = photo;
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

    public double getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(float placeRating) {
        this.placeRating = placeRating;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
