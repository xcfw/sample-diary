package com.example.losya.watermelondiarynew.utils;

import java.util.Calendar;

/**
 */
public class GetDate {

    public static StringBuilder getDate(){

        StringBuilder stringBuilder = new StringBuilder();
        Calendar now = Calendar.getInstance();
        stringBuilder.append(now.get(Calendar.DAY_OF_MONTH) + "/");
        stringBuilder.append((int)(now.get(Calendar.MONTH) + 1)  + "/");
        stringBuilder.append(now.get(Calendar.YEAR));
        return stringBuilder;
    }
}
