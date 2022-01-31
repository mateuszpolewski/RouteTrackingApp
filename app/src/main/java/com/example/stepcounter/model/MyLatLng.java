package com.example.stepcounter.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MyLatLng implements Parcelable {
    private double latitude;
    private double longtitude;

    public MyLatLng(double latitude, double longtitude) {
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    protected MyLatLng(Parcel in) {
        latitude = in.readDouble();
        longtitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longtitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MyLatLng> CREATOR = new Creator<MyLatLng>() {
        @Override
        public MyLatLng createFromParcel(Parcel in) {
            return new MyLatLng(in);
        }

        @Override
        public MyLatLng[] newArray(int size) {
            return new MyLatLng[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public MyLatLng() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<MyLatLng> arrayOf(ArrayList<LatLng> arr) {
        ArrayList<MyLatLng> newArr = new ArrayList<>();
        arr.forEach(element -> {
            newArr.add(new MyLatLng(element.latitude, element.longitude));
        });
        return newArr;
    }
}
