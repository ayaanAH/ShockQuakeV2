package com.example.shockquakev2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    public static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=10";

    ListView quakeListView;
    QuakeAdapter adapter;
    List<EarthQuake> quakes;
    TextView msgTxt;
    ProgressBar pBar;
    RelativeLayout MainQuake;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        adapter = new QuakeAdapter(this, new ArrayList<EarthQuake>());
        quakeListView.setAdapter(adapter);
        quakes = new ArrayList<>();


        /**
         *  when a list item is clicked it takes the app to the repective URL
         */
        quakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
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

        if (isOnline())
        {
            getSupportLoaderManager().initLoader(0, null, this);
        }
        /**
         * when network is not available
         * text is displayed (No Internet Connection)
         */
        else
            {
            MainQuake.setBackgroundResource(R.drawable.internet_o);
            msgTxt.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
            pBar.setVisibility(View.GONE);
            msgTxt.setText(R.string.no_connection);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.removeRule(RelativeLayout.CENTER_IN_PARENT);
            params.setMargins(0, 0, 0, 40);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            msgTxt.setLayoutParams(params);
        }


    }

    //This method is called to initialise all the views in the activity.
    public void init()
    {
        quakeListView = findViewById(R.id.quakeList);
        msgTxt = findViewById(R.id.emptyMsg);
        pBar = findViewById(R.id.listProgress);
        MainQuake = findViewById(R.id.mainQuakeView);
    }


    /**
     * after loader is sucessfully run this method is
     * called to make the required changes in the UI.
     * @param earthquake is  a list that is passed that needs to be displayed.
     */
    private void updateUi(List<EarthQuake> earthquake)
    {

        if (earthquake != null && !earthquake.isEmpty()) {
            adapter.addAll(earthquake);
        } else {
            msgTxt.setVisibility(View.VISIBLE);
            msgTxt.setText(R.string.no_data);
            quakeListView.setEmptyView(msgTxt);
        }

    }

    @NonNull
    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int i, @Nullable Bundle bundle)
    {
        String url = "https://earthquake.usgs.gov/fdsnws/event/1/query";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String set_minMag = sharedPreferences.getString(getString(R.string.preference_minimag_key), getString(R.string.preference_minimag_default));
        String set_limit = sharedPreferences.getString(getString(R.string.preference_limit_key), getString(R.string.preference_limit_default));
        String set_orderBY = sharedPreferences.getString(getString(R.string.preference_orderby_key), getString(R.string.preference_orderby_default));
        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("orderby",set_orderBY);
        uriBuilder.appendQueryParameter("limit",set_limit);
        uriBuilder.appendQueryParameter("minmag",set_minMag);
        quakeListView.setEmptyView(pBar);
        return new QuakeAsyncLoader(this, uriBuilder.toString());

    }


    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthQuake>> loader, List<EarthQuake> earthQuakes)
    {

        adapter.notifyDataSetChanged();
        pBar.setVisibility(View.INVISIBLE);
        updateUi(earthQuakes);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthQuake>> loader)
    {
        adapter.clear();
    }

    /**
     * This method checks the internet connection of the device.
     * @return it returns boolean value (true if connected and false othervise)
     */
    public boolean isOnline()
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menures, menu);
        return true;
    }

    /**
     * This method handles what the menu items can do.
     * @param item it is the item that the user has selected.
     * @return it returns true after the action is done.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.refreshMenu:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, PreferenceActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}