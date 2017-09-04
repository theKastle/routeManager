package com.example.onthe.map.MapUtils;



import java.util.List;

/**
 * Created by onthe on 6/18/2017.
 */

public class Route {
    public Route(){

    }

    public Route(String startAddress, String endAddress){
        this.startAddress = startAddress;
        this.endAddress = endAddress;
    }

    public Distance distance;
    public Duration duration;

    public Route(Distance distance, Duration duration, String endAddress, String startAddress, LatLng startLocation, LatLng endLocation, List<LatLng> points) {
        this.distance = distance;
        this.duration = duration;
        this.endAddress = endAddress;
        this.startAddress = startAddress;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.points = points;
    }

    public String endAddress;
    public String startAddress;

    public String description;

    public LatLng startLocation;
    public LatLng endLocation;

    public List<LatLng> points;

    public String key;
}

