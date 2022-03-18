package com.tabeladetetive.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by charleston on 25/05/14.
 */
public class Crime {
    public String type;
    public String name;
    public int color;
    public int checked;
    public boolean my;
    public static String SUSPECT = "Suspect";
    public static String GUN = "Gun";
    public static String PLACE = "Place";
    SharedPreferences prefs;
    public Crime(String type, String name, int color, boolean checked)
    {
        this.type = type;
        this.name = name;
        this.color = color;
        //this.checked = checked;
    }
    public Crime(Context context, String type, String name, int color)
    {
        this.type = type;
        this.name = name;
        this.color = color;
        prefs = context.getSharedPreferences("data", context.MODE_PRIVATE);
        this.checked = prefs.getInt(name, 0);
        this.my = prefs.getBoolean(name+"my", false);
    }
    public void setChecked(int checked)
    {
        this.checked = checked;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(this.name, this.checked);
        editor.commit();
    }
    public void setMy(boolean my)
    {
        this.my = my;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(this.name+"my", this.my);
        editor.commit();
    }
}
