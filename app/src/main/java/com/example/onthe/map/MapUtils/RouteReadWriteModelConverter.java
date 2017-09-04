package com.example.onthe.map.MapUtils;

/**
 * Created by onthe on 6/18/2017.
 */

public class RouteReadWriteModelConverter {
    public Route getRouteFromReadModel(RouteReadModel routeReadModel){
        String endAddress = routeReadModel.endAddress;
        String startAddress = routeReadModel.startAddress;

        Route route = new Route(startAddress, endAddress);

        return route;
    }

    public RouteReadModel getReadModelFromRoute(Route route, String key) {
        String distance = "" + (route.distance.value / 1000) + " km";
        String duration = "" + Math.floor(route.duration.value / 60) + " min";
        String startAddress = route.startAddress.toString();
        String endAddress = route.endAddress.toString();
        String description = route.description;

        RouteReadModel routeReadModel = new RouteReadModel(
                distance, duration, endAddress, startAddress, description, key
        );

        return routeReadModel;
    }
}
