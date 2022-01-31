package com.example.stepcounter.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.stepcounter.RouteEntriesOverviewActivity;
import com.example.stepcounter.ShowRouteEntryActivity;
import com.example.stepcounter.UserMenuActivity;
import com.example.stepcounter.model.RouteEntry;

public class RouteEntryButton {
    private RouteEntry routeEntry;
    private LinearLayout layout;
    private Context context;
    private Button button;

    public RouteEntryButton(RouteEntry routeEntry, LinearLayout layout, Context context) {
        this.routeEntry = routeEntry;
        this.layout = layout;
        this.context = context;
    }

    public Button create() {
        this.routeEntry = routeEntry;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        button = new Button(context);
        button.setText(String.valueOf(routeEntry.getName()));
        layout.addView(button, layoutParams);
        return button;
    }

    public void addOnClickListener() {

    }
}
