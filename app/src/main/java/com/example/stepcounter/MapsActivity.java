package com.example.stepcounter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stepcounter.databinding.ActivityMapsBinding;
import com.example.stepcounter.model.MyLatLng;
import com.example.stepcounter.model.RouteEntry;
import com.example.stepcounter.utils.MyAlertDialog;
import com.example.stepcounter.utils.RouteHelper;
import com.example.stepcounter.utils.MyTimer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, SensorEventListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    boolean routeStarted = false;
    ArrayList<LatLng> latLngs = new ArrayList<>();
    ArrayList<LatLng> routeLatLngs = new ArrayList<>();
    ArrayList<Double> speedArray = new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    String TAG = "MapsActivity";
    private Button startRouteButton;
    private TextView stepCountTextView;
    private TextView distanceTextView;
    private TextView timeTextView;
    private TextView speedTextView;
    SensorManager sensorManager;
    private Sensor stepSensor;
    private Integer stepCount = 0;
    private MyTimer myTimer;
    private double avgSpeed = 0;
    private int time = 0;
    private double distance = 0;
    private double speed = 0;

    private int startTime;

    final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 42));
                if (!routeStarted) {
                    latLngs.clear();
                    routeLatLngs.clear();
                    speed = 0;
                    distance = 0;
                    speedArray.clear();
                }
                if (routeStarted) {
                    latLngs.add(latLng);
                    routeLatLngs.add(latLng);
                    speed = Math.round(location.getSpeed() * 3.6 * 10) / 10.0  ; //to km/h, one decimal place
                    speedArray.add(speed);
                        RouteHelper.setStartLocation(location);
                        mMap.addPolyline(RouteHelper.getUpdatedPolylineOptions(routeLatLngs));
                        latLngs.remove(0);
                        distance += RouteHelper.calculateDistance(routeLatLngs);

                }
                speedTextView.setText(String.valueOf(speed == 0 ? " --.--" : speed) + " km/h");
                distanceTextView.setText(String.valueOf((int) distance == 0 ? " --" : (int) distance) + " m");

            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startRouteButton = findViewById(R.id.startRouteButton);

        Context appContext = this;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);
        }

        //requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 1);

        startRouteButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(!routeStarted) {
                    myTimer.startTimer();

                    startRouteButton.setBackgroundResource(R.drawable.ic_baseline_pause_circle_outline_24);
                    routeStarted = true;

                } else {
                    MyAlertDialog.create(appContext, new RouteEntry(stepCount, RouteHelper.calculateAvgSpeed(speedArray),
                            (int) distance, myTimer.getTime(), MyLatLng.arrayOf(routeLatLngs)));
                    myTimer.cancelAndResetTimer();
                    startRouteButton.setBackgroundResource(R.drawable.ic_baseline_radio_button_checked_24);
                    routeStarted = false;
                    mMap.clear();
                    stepCount = 0;
                    RouteHelper.clearPolylineOptions();
                    stepCountTextView.setText(String.valueOf(" -- kroków"));
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        stepCountTextView = findViewById(R.id.stepsCount);
        distanceTextView = findViewById(R.id.distance);
        timeTextView = findViewById(R.id.time);
        speedTextView = findViewById(R.id.speed);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        myTimer = new MyTimer(timeTextView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
            enableUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_LOCATION_REQUEST_CODE);
            }
        }
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
                enableUserLocation();
            } else {
                //odmowa dostepu
            }
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(stepSensor != null) {
            Log.d(TAG, "onResume: " + "YES");
            //Toast.makeText(this,  "Sensor is available!", Toast.LENGTH_SHORT).show();
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        else {
            //Toast.makeText(this,  "Sensor not available!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onResume: " + "NOPE");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent != null && routeStarted) {
            stepCount++;
            stepCountTextView.setText(String.valueOf(stepCount == 0 ? " --" : stepCount + " kroków"));
            System.out.println(sensorEvent);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}