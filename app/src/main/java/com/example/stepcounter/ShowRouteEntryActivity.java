package com.example.stepcounter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.widget.TextView;

import com.example.stepcounter.model.MyLatLng;
import com.example.stepcounter.model.RouteEntry;
import com.example.stepcounter.utils.RouteHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.stepcounter.databinding.ActivityShowRouteEntryBinding;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class ShowRouteEntryActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityShowRouteEntryBinding binding;
    private TextView stepCountTextView;
    private TextView distanceTextView;
    private TextView timeTextView;
    private TextView speedTextView;
    private TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowRouteEntryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        stepCountTextView = findViewById(R.id.stepsCount);
        distanceTextView = findViewById(R.id.distance);
        timeTextView = findViewById(R.id.time);
        speedTextView = findViewById(R.id.speed);
        nameTextView = findViewById(R.id.name);

        Bundle b = this.getIntent().getExtras();
        RouteEntry routeEntry = b.getParcelable("RouteEntry");
        ArrayList<MyLatLng> routeEntryMyLatLngs = getIntent().getParcelableArrayListExtra("myLatLngs");
        routeEntry.setRouteMyLatLngs(routeEntryMyLatLngs);
        Log.d(TAG, "onMapReady: " + routeEntryMyLatLngs);

        PolylineOptions polylineOptions = RouteHelper.createPolylineWithSettings();
        routeEntry.getRouteMyLatLngs().forEach(myLatLng ->
                polylineOptions.add(new LatLng(myLatLng.getLatitude(), myLatLng.getLongtitude())));

        ArrayList<MyLatLng> myLatLngs = routeEntry.getRouteMyLatLngs();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(myLatLngs.get(0).getLatitude(), myLatLngs.get(0).getLongtitude()))
                .title("Start"));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(myLatLngs.get(myLatLngs.size()-1).getLatitude(), myLatLngs.get(myLatLngs.size()-1).getLongtitude()))
                .title("Koniec"));


        stepCountTextView.setText(routeEntry.getSteps() + " kroków");
        distanceTextView.setText(routeEntry.getDistance() + " m");
        timeTextView.setText(routeEntry.getTime() + " s");
        speedTextView.setText(routeEntry.getSpeed() + " km/h");
        nameTextView.setText(routeEntry.getName());
        mMap.addPolyline(polylineOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(polylineOptions.getPoints().get(0), 18));
    }

}