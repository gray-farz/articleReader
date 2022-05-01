package com.example.corona.sharedPref;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedApp
{
    private SharedPreferences shpref;
    public SharedApp(Context context) {
        shpref = context.getSharedPreferences("sharedInfo",Context.MODE_PRIVATE);
    }
    public void saveInfo(int numberOfSavedFile)
    {
        SharedPreferences.Editor editor=shpref.edit();
        editor.putInt("fileNumber",numberOfSavedFile);
        editor.apply();
    }
    public int getFileNumber()
    {
        return shpref.getInt("fileNumber",0);
    }
}
