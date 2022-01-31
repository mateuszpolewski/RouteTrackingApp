package com.example.stepcounter.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyStepSensor extends Activity implements SensorEventListener {
    SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    Sensor sSensor= sensorManager .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
