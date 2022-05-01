package com.example.corona;

import android.app.Application;
import android.util.Log;

import com.example.corona.database.AppDatabase;
import com.example.corona.interfaces.AfterGetListFromDatabase;
import com.example.corona.model.ModelRoom;

import java.util.List;

public class Global extends Application implements AfterGetListFromDatabase
{
    public static final int RECORDS_PER_PAGE=5;
    public static final String FOLDER_NAME_APP="/corona/";
    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.ReadDatabase(AppDatabase.getInstance(this),this);
        //AppDatabase.fillDatabase(AppDatabase.getInstance(this));
    }

    @Override
    public void GetListArticles(List<ModelRoom> listArticles)
    {
        Log.d("aaa", "GetListArticles: in Global");
        if(listArticles.size() == 0)
        {
            Log.d("aaa", "***   fillDatabase    ***");
            AppDatabase.fillDatabase(AppDatabase.getInstance(this));
        }

    }
}
