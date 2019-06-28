package com.example.shockquakev2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shockquakev2.EarthQuake;
import com.example.shockquakev2.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=1";
    ListView quakeListView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new QuakeAsyncTask().execute(USGS_REQUEST_URL);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.refreshMenu:
                String x = "reset";
                return true;
            case R.id.settings:
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menures, menu);
        return true;
    }

    private void updateUi(ArrayList<EarthQuake> earthquake)
    {

        quakeListView = findViewById(R.id.quakeList);
        final QuakeAdapter qAdapter = new QuakeAdapter(this, earthquake);
        quakeListView.setAdapter(qAdapter);
        quakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                EarthQuake currentEarthquake = qAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

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

