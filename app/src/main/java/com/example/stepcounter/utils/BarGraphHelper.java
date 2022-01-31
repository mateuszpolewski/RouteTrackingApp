package com.example.stepcounter.utils;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BarGraphHelper {
    private static ArrayList<BarEntry> barEntries;
    public static void addEntries(HashMap<String, Integer> map, BarChart barChart, String label, String description) {
        barChart.invalidate();
        barChart.clear();
        barEntries = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            barEntries.add(new BarEntry(Integer.parseInt(entry.getKey()), entry.getValue()));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, label);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(12f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText(description);
        barChart.animateY(1300);

        XAxis xAxis=barChart.getXAxis();

        //xAxis.setLabelCount(barEntries.size() + 2,true);
        xAxis.setGranularity(1.0f);
        xAxis.setGranularityEnabled(true);
    }
}
