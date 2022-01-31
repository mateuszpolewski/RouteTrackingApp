package com.example.stepcounter.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.stepcounter.utils.RouteHelper;

import java.util.ArrayList;

public class RouteEntry implements Parcelable {
    private ArrayList<MyLatLng> routeMyLatLngs;
    private String name;
    private String userId;
    private int steps;
    private double speed;
    private int distance;
    private int time;
    private String date;

    public RouteEntry() { }

    public RouteEntry(String name, String userId, int steps, double speed, int distance, int time) {
        this.name = name;
        this.userId = userId;
        this.steps = steps;
        this.speed = speed;
        this.distance = distance;
        this.time = time;
        this.date = RouteHelper.getCurrentDate();
    }

    public RouteEntry(int steps, double speed, int distance, int time, ArrayList<MyLatLng> routeMyLatLngs) {
        this.steps = steps;
        this.speed = speed;
        this.distance = distance;
        this.time = time;
        this.routeMyLatLngs = routeMyLatLngs;
        this.date = RouteHelper.getCurrentDate();

    }

    protected RouteEntry(Parcel in) {
        name = in.readString();
        userId = in.readString();
        steps = in.readInt();
        speed = in.readDouble();
        distance = in.readInt();
        time = in.readInt();
        date = in.readString();
    }

    public static final Creator<RouteEntry> CREATOR = new Creator<RouteEntry>() {
        @Override
        public RouteEntry createFromParcel(Parcel in) {
            return new RouteEntry(in);
        }

        @Override
        public RouteEntry[] newArray(int size) {
            return new RouteEntry[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public ArrayList<MyLatLng> getRouteMyLatLngs() {
        return routeMyLatLngs;
    }

    public void setRouteMyLatLngs(ArrayList<MyLatLng> routeMyLatLngs) {
        this.routeMyLatLngs = routeMyLatLngs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(userId);
        dest.writeInt(steps);
        dest.writeDouble(speed);
        dest.writeInt(distance);
        dest.writeInt(time);
        dest.writeList(routeMyLatLngs);
        dest.writeString(date);
    }
}
