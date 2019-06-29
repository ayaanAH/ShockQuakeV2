package com.example.shockquakev2;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class QuakeAsyncLoader extends AsyncTaskLoader<List<EarthQuake>>
{

    public static final String LOG_TAG = QuakeAsyncLoader.class.getName();
    String USGS_URL;
    List<EarthQuake> newList;

    public QuakeAsyncLoader(Context context, String USGS_URL)
    {
        super(context);
        this.USGS_URL = USGS_URL;
        Log.i(LOG_TAG, "init Asynctask Loader");
    }

    @Override
    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public List<EarthQuake> loadInBackground() {
        //loading data here
        Log.i(LOG_TAG, "loadInBackground");
        try
        {
                newList = Utils.fetchEarthquakeData(USGS_URL);
                return newList;
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        return newList;
    }

}