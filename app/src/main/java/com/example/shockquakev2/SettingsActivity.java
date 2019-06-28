package com.example.shockquakev2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URL;

public class SettingsActivity extends AppCompatActivity
{
    EditText miniMagnitude;
    Button miniMagBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        miniMagnitude = findViewById(R.id.miniMag);
        miniMagBtn = findViewById(R.id.miniMagBtn);

        miniMagBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String newMinMag=miniMagnitude.getText().toString();

                String stringUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=3";
                stringUrl = stringUrl.replaceFirst("\\bminmagnitude=.*?(&|$)", "minmagnitude="+newMinMag+"$1");
                URL url = Utils.createUrl(stringUrl);

            }
        });

    }
}
