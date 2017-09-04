package com.example.onthe.map.LocationManager;

import android.util.Log;

import com.example.onthe.map.MapUtils.Route;
import com.example.onthe.map.MapUtils.RouteReadModel;
import com.example.onthe.map.MapUtils.RouteReadWriteModelConverter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by onthe on 6/18/2017.
 */

public class RouteDataService {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();


    public String saveRoute(Route route){
        String userId = mFirebaseUser.getUid();
        Log.i("User Id = ", userId);
        DatabaseReference userRef = this.myRef.child(userId);
        Log.i("Create new user == ", userRef.toString());

        String key;
        DatabaseReference pushedPostKey;

        if (route.key == null) {
            Log.i("Null Route key == ", "dfasdfad");

            pushedPostKey = userRef.push();
            key = pushedPostKey.getKey();
        } else {
            Log.i("Route key == ", route.key);
            pushedPostKey = userRef.child(route.key);
            key = route.key;
        }

        RouteReadWriteModelConverter converter = new RouteReadWriteModelConverter();
        RouteReadModel routeReadModel = converter.getReadModelFromRoute(route, key);

        routeReadModel.key = key;
        pushedPostKey.setValue(routeReadModel);

        return key;
    }
}
