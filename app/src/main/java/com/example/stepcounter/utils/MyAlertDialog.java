package com.example.stepcounter.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.stepcounter.R;
import com.example.stepcounter.dao.RouteEntryDAO;
import com.example.stepcounter.model.RouteEntry;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MyAlertDialog {

    public static final int THEME_HOLO_DARK = 2;
    private static final String TAG = "MyAlertDialog";
    private static RouteEntry routeEntry;
    public static void create(Context context, RouteEntry routeEntryTemp) {
        routeEntry = routeEntryTemp;
        routeEntry.setRouteMyLatLngs(new ArrayList<>(routeEntryTemp.getRouteMyLatLngs()));
        routeEntry.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        AlertDialog.Builder builder = new AlertDialog.Builder(context, THEME_HOLO_DARK);
        builder.setCancelable(true);
        builder.setTitle("Zapisać trasę?");
        builder.setMessage("Wprowadź nazwę:");
        final EditText input = new EditText(context);
        input.setText("Droga " + RouteHelper.getCurrentDate());
        input.setTextColor(ContextCompat.getColor(context, R.color.white));
        float dpi = builder.getContext().getResources().getDisplayMetrics().densityDpi;

        builder.setPositiveButton("Zapisz",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        routeEntry.setName(String.valueOf(input.getText()));
                        RouteEntryDAO.add(routeEntry);

                    }
                });
        //android.R.string.cancel
        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        //, (int)(19*dpi), (int)(5*dpi), (int)(14*dpi), (int)(5*dpi)
        dialog.setView(input);
        dialog.show();
    }
}
