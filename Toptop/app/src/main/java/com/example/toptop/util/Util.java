package com.example.merchantDemo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static  String convertIntToTime(long t){
        Date date = new Date(t * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        String formattedDate = sdf.format(date);
       return formattedDate;
    }
}
