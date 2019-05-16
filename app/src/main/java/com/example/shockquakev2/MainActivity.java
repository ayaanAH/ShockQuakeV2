package com.example.shockquakev2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";
    ListView quakeListView;
    ArrayList<EarthQuake> list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new QuakeAsyncTask().execute(USGS_REQUEST_URL);

    }

    private void updateUi(ArrayList<EarthQuake> earthquake) {
        list=earthquake;

        quakeListView=findViewById(R.id.quakeList);
        QuakeAdapter qAdapter=new QuakeAdapter(this,list);
        quakeListView.setAdapter(qAdapter);
    }

    private class QuakeAsyncTask extends AsyncTask<String, Void, ArrayList<EarthQuake>>
    {

        @Override
        protected ArrayList<EarthQuake> doInBackground(String... url)
        {
            ArrayList<EarthQuake> newUrl = Utils.fetchEarthquakeData(url[0]);
            return newUrl;
        }

        @Override
        protected void onPostExecute(ArrayList<EarthQuake> earthquake)
        {
            if (earthquake == null)
            {
                return;
            }

            updateUi(earthquake);
        }
    }
}
