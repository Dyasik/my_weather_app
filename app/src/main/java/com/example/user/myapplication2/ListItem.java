package com.example.user.myapplication2;

import android.graphics.Bitmap;

public class ListItem {
    String day;
    String date;
    String temp;
    Bitmap icon;

    public ListItem(String day, String date, String temp, Bitmap icon_url){
        this.day = day;
        this.date = date;
        this.temp = temp;
        this.icon = icon_url;
    }
}
