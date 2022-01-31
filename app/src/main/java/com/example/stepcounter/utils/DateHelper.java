package com.example.stepcounter.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    public static String extractString(String value, String dateString) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat(value);
        return dateFormat.format(date);
    }
}
