package com.example.stepcounter;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.stepcounter.model.User;
import com.google.android.gms.maps.GoogleMap;

public class UserMenuActivity extends AppCompatActivity {
    Button mapViewButton;
    Button routeEntriesOverviewButton;
    Button statsButton;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        mapViewButton = findViewById(R.id.mapView);
        routeEntriesOverviewButton = findViewById(R.id.routeEntriesOverview);
        statsButton = findViewById(R.id.statistics);
        /*
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        }
        */
        mapViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMenuActivity.this, MapsActivity.class));
            }
        });

        routeEntriesOverviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMenuActivity.this, RouteEntriesOverviewActivity.class));
            }
        });

        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMenuActivity.this, StatsActivity.class));
            }
        });
    }

}