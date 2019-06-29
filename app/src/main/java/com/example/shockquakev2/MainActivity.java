package com.example.shockquakev2;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<EarthQuake>>
{

    public static final String LOG_TAG = MainActivity.class.getName();
    public static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=1";
    ListView quakeListView;
    QuakeAdapter adapter;
    List<EarthQuake> quakes;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        quakeListView = findViewById(R.id.quakeList);

        if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
        {
            Toast.makeText(MainActivity.this,"Internet Connection Available...",Toast.LENGTH_LONG).show();
            adapter = new QuakeAdapter(this, new ArrayList<EarthQuake>());
            quakeListView.setAdapter(adapter);
            quakes = new ArrayList<>();
        }
        else
        {
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
            Intent i = new Intent(getApplicationContext(), NoInternetActivity.class);
            startActivity(i);
        }



        quakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                EarthQuake currentEarthquake = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });


        getSupportLoaderManager().initLoader(0, null, this);

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
                Intent i = new Intent(this, MyPreferences.class);
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

    private void updateUi(List<EarthQuake> earthquake)
    {

        if (earthquake !=null && !earthquake.isEmpty())
        {
            adapter.addAll(earthquake);
        }

    }

    @NonNull
    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int i, @Nullable Bundle bundle)
    {
        Log.i(LOG_TAG,"loader created");
        return new QuakeAsyncLoader(this, USGS_REQUEST_URL);
    }


    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes)
    {
        adapter.notifyDataSetChanged();

        updateUi(earthQuakes);

        Log.i(LOG_TAG, "onLoadFinish");
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthQuake>> loader)
    {
        adapter.clear();
        Log.i(LOG_TAG, "onLoaderReset");
    }

}