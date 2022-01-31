package com.example.stepcounter.utils;

import android.os.Handler;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MyTimer extends Handler{
    private int startTime;
    private TextView timeTextView;
    private Handler handler = new Handler();
    private int time = 0;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm/SS");
    private boolean isStarted = false;

    public MyTimer(TextView timeTextView) {
        this.timeTextView = timeTextView;
    }

    public void setTimeTextView(TextView myTimeTextView) {
        timeTextView = myTimeTextView;
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time++;
            timeTextView.setText(time == 0 ? " --:--" : secondsToString(time));
            startTimer();
        }
    };

    public int getTime() {
        return time;
    }

    public void startTimer() {
        handler.postDelayed(runnable, 1000);
    }

    public void cancelTimer() {
        handler.removeCallbacks(runnable);
    }

    public void cancelAndResetTimer() {
        time = 0;
        timeTextView.setText(time == 0 ? " --:--" : secondsToString(time));
        cancelTimer();
    }

}
