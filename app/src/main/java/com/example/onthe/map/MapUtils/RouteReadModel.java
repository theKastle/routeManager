package com.example.onthe.map.MapUtils;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by onthe on 6/18/2017.
 */

public class RouteReadModel {
    public RouteReadModel(){

    }

    public RouteReadModel(String startAddress, String endAddress){
        this.startAddress = startAddress;
        this.endAddress = endAddress;
    }

    public String distance;
    public String duration;

    public RouteReadModel(String distance, String duration, String endAddress, String startAddress, String description, String key) {
        this.distance = distance;
        this.duration = duration;
        this.endAddress = endAddress;
        this.startAddress = startAddress;
        this.description = description;
        this.key = key;
    }

    public String endAddress;
    public String startAddress;

    public String description;
    public String key;

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("startAddress", startAddress);
        result.put("endAddress", endAddress);
        result.put("description", description);
        result.put("distance", distance);
        result.put("duration", duration);

        return result;
    }

}

