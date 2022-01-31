package com.example.stepcounter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.stepcounter.model.RouteEntry;
import com.example.stepcounter.utils.RouteEntryButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RouteEntriesOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_entries_overview);
        DatabaseReference ref = FirebaseDatabase.getInstance("https://stepcounter-4b081-default-rtdb.europe-west1.firebasedatabase.app").getReference("RouteEntry");

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query routeQuery = ref.orderByChild("userId").equalTo(userId);
        LinearLayout layout = (LinearLayout)findViewById(R.id.layout);
        Context context = this;
        routeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    RouteEntry routeEntry = singleSnapshot.getValue(RouteEntry.class);
                    Button button = new RouteEntryButton(routeEntry, layout, context).create();
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent();
                            Bundle b = new Bundle();
                            b.putParcelable("RouteEntry", routeEntry);
                            i.putExtras(b);
                            i.putParcelableArrayListExtra( "myLatLngs", routeEntry.getRouteMyLatLngs());
                            i.setClass(context, ShowRouteEntryActivity.class);
                            startActivity(i);
                            //startActivity(new Intent(RouteEntriesOverviewActivity.this, ShowRouteEntryActivity.class));
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
}