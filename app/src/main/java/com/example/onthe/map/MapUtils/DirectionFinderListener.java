package com.example.onthe.map.MapUtils;

import java.util.List;

/**
 * Created by onthe on 6/18/2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}

