package com.example.stepcounter.utils;

import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.stepcounter.model.RouteEntry;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class RouteHelper {
    private static final String TAG = "RouteHelper";
    private static Location startLocation = null;
    private static PolylineOptions polylineOptions = createPolylineWithSettings();
    private static ArrayList<RouteEntry> filteredEntries;
    private static HashMap<String, Integer> distanceMap, timeMap, stepsMap;

    public static double calculateDistance(ArrayList<LatLng> routeLatLngs) {
        if(routeLatLngs.size() < 2) return 0;

        Location startPoint=new Location("locationA");
        startPoint.setLatitude(routeLatLngs.get(routeLatLngs.size()-2).latitude);
        startPoint.setLongitude(routeLatLngs.get(routeLatLngs.size()-2).longitude);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(routeLatLngs.get(routeLatLngs.size()-1).latitude);
        endPoint.setLongitude(routeLatLngs.get(routeLatLngs.size()-1).longitude);

        return Math.round(startPoint.distanceTo(endPoint) * 10.0) / 10.0;
    }

    public static int calculateTime(Location location) {
        if(startLocation == null)
            startLocation = location;
        return Math.round((location.getTime() - startLocation.getTime())/1000); //from ms to s
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double calculateAvgSpeed(ArrayList<Double> speedArray) {
        OptionalDouble speedAvg = speedArray
                .stream()
                .mapToDouble(a -> a)
                .average();
        return Math.round(speedAvg.orElse(0) * 10) / 10.0;
    }

    public static Location getStartLocation() {
        return startLocation;
    }

    public static void setStartLocation(Location startLocation) {
        RouteHelper.startLocation = startLocation;
    }

    public static PolylineOptions getUpdatedPolylineOptions(ArrayList<LatLng> routeLatLngs) {
        LatLng latLng = routeLatLngs.get(routeLatLngs.size() - 1);
        polylineOptions.add(latLng);
        return polylineOptions;
    }

    public static String getCurrentDate() {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public static void clearPolylineOptions() {
        polylineOptions = createPolylineWithSettings();
    }

    public static PolylineOptions createPolylineWithSettings() {
        return new PolylineOptions()
                .color(Color.RED)
                .width(30);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<RouteEntry> getFilteredList(List<RouteEntry> routeEntries, String timeUnit, String date) {
        if(timeUnit == null || date == null) return (ArrayList<RouteEntry>) routeEntries;
        return (ArrayList<RouteEntry>) routeEntries.stream()
                .filter(routeEntry -> {
                    try {
                        if(timeUnit.equals("dd") && !routeEntry.getDate().startsWith(DateHelper.extractString("yyyy-MM", RouteHelper.getCurrentDate()))) return false;
                        if(timeUnit.equals("MM") && !routeEntry.getDate().startsWith(DateHelper.extractString("yyyy", RouteHelper.getCurrentDate()))) return false;
                        String routeTime =  DateHelper.extractString(timeUnit, routeEntry.getDate());
                        return routeTime.equals(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double getAvgSpeed(List<RouteEntry> allEntries, String timeUnit, String date) {
        filteredEntries = getFilteredList(allEntries, timeUnit, date);
        double speedSum = filteredEntries.stream().mapToDouble(RouteEntry::getSpeed).sum();
        return Math.round(speedSum / filteredEntries.size() * 10) / 10.0 ;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int sumDistance(List<RouteEntry> allEntries, String timeUnit, String date) {
        filteredEntries = getFilteredList(allEntries, timeUnit, date);
        return filteredEntries.stream().mapToInt(RouteEntry::getDistance).sum();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int sumTime(List<RouteEntry> allEntries, String timeUnit, String date) {
        filteredEntries = getFilteredList(allEntries, timeUnit, date);
        return filteredEntries.stream().mapToInt(RouteEntry::getTime).sum();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int sumSteps(List<RouteEntry> allEntries, String timeUnit, String date) {
        filteredEntries = getFilteredList(allEntries, timeUnit, date);
        return filteredEntries.stream().mapToInt(RouteEntry::getSteps).sum();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void groupValues(String childTimeUnit) {
        distanceMap = new HashMap<>();
        timeMap = new HashMap<>();
        stepsMap = new HashMap<>();
        Log.d(TAG, "groupValues: filteredEntries" + filteredEntries);
        Log.d(TAG, "groupValues: distanceMap" + distanceMap);

        filteredEntries.forEach(routeEntry -> {
            try {
                String routeDay = DateHelper.extractString(childTimeUnit, routeEntry.getDate());
                updateMap(distanceMap, routeDay, routeEntry.getDistance());
                updateMap(timeMap, routeDay, routeEntry.getTime());
                updateMap(stepsMap, routeDay, routeEntry.getSteps());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
    }

    public static void constructGraph(String expression, BarChart barChart, String label, String description) {
        switch(expression) {
            case "distance":
                BarGraphHelper.addEntries(distanceMap, barChart, label, description);
                break;
            case "time":
                BarGraphHelper.addEntries(timeMap, barChart, label, description);
                break;
            case "steps":
                BarGraphHelper.addEntries(stepsMap, barChart, label, description);
                break;
            default:
                // code block
        }

    }

    private static void updateMap(HashMap<String, Integer> map, String routeDay, int value) {
        //map.clear();
        if(!map.containsKey(routeDay)) map.put(routeDay, value);
        else map.put(routeDay, map.get(routeDay) + value);
    }
}
