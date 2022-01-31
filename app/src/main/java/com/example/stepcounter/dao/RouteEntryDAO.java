package com.example.stepcounter.dao;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.stepcounter.model.RouteEntry;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RouteEntryDAO {
    private static final String TAG = "RouteEntryDAO";
    private static FirebaseDatabase database = FirebaseDatabase.getInstance("https://stepcounter-4b081-default-rtdb.europe-west1.firebasedatabase.app");
    private static DatabaseReference ref = database.getReference(RouteEntry.class.getSimpleName());

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void add(RouteEntry routeEntry) {
        String key = ref.push().getKey();
        ref.child(key).setValue(routeEntry);
        //ref.child(key).child("routeLatLngs").setValue(routeEntry.getRouteLatLngs());

        /*
        ref.child(key).child("latlng " + 1).child("lat").setValue("dadad");
        ref.child(key).child("latlng " + 1).child("lng").setValue("routeEntry.getRouteLatLngs().get(0).longitude");


        Map<String, Object> mapLatLng = new HashMap<>();
        mapLatLng.put("lat", Double.valueOf(routeEntry.getRouteLatLngs().get(1).latitude));
        mapLatLng.put("lng", Double.valueOf(routeEntry.getRouteLatLngs().get(1).longitude));
        ref.child(key).child("latlng " + 2).setValue(mapLatLng);

        Log.d(TAG, "add: " + routeEntry.getRouteLatLngs());
    */
        /*
        AtomicInteger i = new AtomicInteger(0);
        routeEntry.getRouteLatLngs().forEach(
                latLng ->
                {
                    i.getAndIncrement();
                    Map<String, Object> mapLatLng = new HashMap<>();
                    mapLatLng.put("lat", latLng.latitude);
                    mapLatLng.put("long", latLng.longitude);
                    ref.child(key).child("latlng " + i).setValue(mapLatLng);
                }


        );
        */

    //database.getReference()
    }
}
