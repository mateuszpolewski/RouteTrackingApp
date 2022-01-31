package com.example.stepcounter.utils;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.example.stepcounter.model.RouteEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RouteDAOHelper {

    public static ArrayList<RouteEntry> readUserEntries() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://stepcounter-4b081-default-rtdb.europe-west1.firebasedatabase.app").getReference("RouteEntry");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query routeQuery = ref.orderByChild("userId").equalTo(userId);
        ArrayList<RouteEntry> routeEntries = new ArrayList<>();

        routeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    RouteEntry routeEntry = singleSnapshot.getValue(RouteEntry.class);
                    routeEntries.add(routeEntry);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
        return routeEntries;
    }

}
