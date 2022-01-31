package com.example.stepcounter;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stepcounter.model.RouteEntry;
import com.example.stepcounter.utils.BarGraphHelper;
import com.example.stepcounter.utils.DateHelper;
import com.example.stepcounter.utils.RouteDAOHelper;
import com.example.stepcounter.utils.RouteHelper;
import com.github.mikephil.charting.charts.BarChart;

import java.text.ParseException;
import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {
    Button dayButton;
    Button monthButton;
    Button yearButton;
    Button allButton;
    Button distanceButton, timeButton, stepsButton;
    String currDateString;
    ArrayList<RouteEntry> routeEntries;
    String currDay, currMonth, currYear;
    TextView distanceTextView, timeTextView, avgSpeedTextView, stepsTextView;
    boolean buttonClicked;
    BarChart barChart;
    String selectedTime = "Dzień";
    int selectColor = R.color.red;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        dayButton = findViewById(R.id.dayButton);
        monthButton = findViewById(R.id.monthButton);
        yearButton = findViewById(R.id.yearButton);
        allButton = findViewById(R.id.allButton);

        distanceTextView = findViewById(R.id.distanceTextView);
        timeTextView = findViewById(R.id.timeTextView);
        avgSpeedTextView = findViewById(R.id.avgSpeedTextView);
        stepsTextView = findViewById(R.id.stepsTextView);

        distanceButton = findViewById(R.id.distanceButton);
        timeButton = findViewById(R.id.timeButton);
        stepsButton = findViewById(R.id.stepsButton);

        barChart = findViewById(R.id.barChart);

        routeEntries = RouteDAOHelper.readUserEntries();
        currDateString = RouteHelper.getCurrentDate();

        buttonClicked = false;

        try {
            currDay = DateHelper.extractString("dd", currDateString);
            currMonth = DateHelper.extractString("MM", currDateString);
            currYear = DateHelper.extractString("yyyy", currDateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        dayButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                selectedTime = "";
                setMeasuredValues("dd", currDay);
                resetChartAndButtons(dayButton);
            }
        });

        monthButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                selectedTime = "Dzień";
                setMeasuredValues("MM", currMonth);
                RouteHelper.groupValues("dd");
                resetChartAndButtons(monthButton);
            }
        });

        yearButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                selectedTime = "Miesiąc";
                setMeasuredValues("yyyy", currYear);
                RouteHelper.groupValues("MM");
                resetChartAndButtons(yearButton);
            }
        });

        allButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                selectedTime = "Rok";
                setMeasuredValues(null, null);
                RouteHelper.groupValues("yyyy");
                resetChartAndButtons(allButton);
            }
        });

        distanceButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setColorLowerButton(distanceButton);
                RouteHelper.constructGraph("distance", barChart, selectedTime, "Wartości zmierzonego dystansu");
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setColorLowerButton(timeButton);
                RouteHelper.constructGraph("time", barChart, selectedTime, "Wartości zmierzonego czasu");
            }
        });

        stepsButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setColorLowerButton(stepsButton);
                RouteHelper.constructGraph("steps", barChart, selectedTime, "Liczba zmierzonych kroków");
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setMeasuredValues(String timeUnit, String partDate) {
        int distance = RouteHelper.sumDistance(routeEntries, timeUnit, partDate);
        int time = RouteHelper.sumTime(routeEntries, timeUnit, partDate);
        double avgSpeed = RouteHelper.getAvgSpeed(routeEntries, timeUnit, partDate);
        int steps = RouteHelper.sumSteps(routeEntries, timeUnit, partDate);
        distanceTextView.setText(String.valueOf(distance) + " m");
        timeTextView.setText(String.valueOf(time) + " s");
        avgSpeedTextView.setText(String.valueOf(avgSpeed) + " km/h");
        stepsTextView.setText(String.valueOf(steps) + " kroków");

    }

    @SuppressLint("ResourceAsColor")
    private void resetColorUpperButtons() {
        int defaultColor = R.color.purple_700;
        dayButton.setBackgroundColor(getResources().getColor(defaultColor));
        monthButton.setBackgroundColor(getResources().getColor(defaultColor));
        yearButton.setBackgroundColor(getResources().getColor(defaultColor));
        allButton.setBackgroundColor(getResources().getColor(defaultColor));
    }
    @SuppressLint("ResourceAsColor")
    private void setColorUpperButton(Button button) {
        resetColorUpperButtons();
        button.setBackgroundColor(getResources().getColor(selectColor));
    }

    @SuppressLint("ResourceAsColor")
    private void resetColorLowerButtons() {
        int defaultColor = R.color.purple_700;
        distanceButton.setBackgroundColor(getResources().getColor(defaultColor));
        timeButton.setBackgroundColor(getResources().getColor(defaultColor));
        stepsButton.setBackgroundColor(getResources().getColor(defaultColor));
    }
    @SuppressLint("ResourceAsColor")
    private void setColorLowerButton(Button button) {
        resetColorLowerButtons();
        button.setBackgroundColor(getResources().getColor(selectColor));
    }

    private void resetChartAndButtons(Button button) {
        setColorUpperButton(button);
        resetColorLowerButtons();
        barChart.clear();
        barChart.invalidate();
    }
}