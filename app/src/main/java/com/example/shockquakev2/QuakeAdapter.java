package com.example.shockquakev2;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuakeAdapter extends ArrayAdapter<EarthQuake>
{
    private static final String SEP = "of";
    TextView magnitudeView, locationViewT, locationViewB, dateView, timeView;

    public QuakeAdapter(Activity context, List<EarthQuake> numberList) {
        super(context,0,numberList);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View root= LayoutInflater.from(getContext()).inflate(R.layout.list_element, parent,false);

        EarthQuake currentQuake=getItem(position);


        magnitudeView=root.findViewById(R.id.magnitude);
        locationViewT=root.findViewById(R.id.location);
        locationViewB=root.findViewById(R.id.locationDown);
        dateView=root.findViewById(R.id.date);
        timeView=root.findViewById(R.id.time);


        String magTxt = GetMag1(currentQuake.getMagnitude());
        magnitudeView.setText(magTxt);
        int backCol = getMagnitudeColor(currentQuake.getMagnitude());
        GradientDrawable circleMagnitude = (GradientDrawable)magnitudeView.getBackground();
        circleMagnitude.setColor(backCol);
        String locTxtTop, locTxtBottom;

        String dateTxt = GetDate1(currentQuake.getDate());
        String timeTxt = GetTime1(currentQuake.getDate());

        if (isContainKm(currentQuake.getLocation()))
        {
            locTxtTop = GetLocTop(currentQuake.getLocation());
            locTxtBottom = GetLocBottom(currentQuake.getLocation());
            locationViewT.setText(locTxtTop);
            locationViewB.setText(locTxtBottom);
        }
        else
        {
            locationViewT.setText("Near to");
            locationViewB.setText(currentQuake.getLocation());
        }
        dateView.setText(dateTxt);
        timeView.setText(timeTxt);


        return root;
    }

    private String GetMag1(double magnitude)
    {
        double roundedOneDigitX = Math.round(magnitude * 10) / 10.0;
        return String.valueOf(roundedOneDigitX);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private int getMagnitudeColor(double magnitude)
    {
        int backColor;

        switch ((int) magnitude)
        {
            case 0:
            case 1: backColor = R.color.magnitude1;break;

            case 2: backColor = R.color.magnitude2;break;

            case 3: backColor = R.color.magnitude3;break;

            case 4: backColor = R.color.magnitude4;break;

            case 5: backColor = R.color.magnitude5;break;

            case 6: backColor = R.color.magnitude6;break;

            case 7: backColor = R.color.magnitude7;break;

            case 8: backColor = R.color.magnitude8;break;

            case 9: backColor = R.color.magnitude9;break;

            default: backColor = R.color.magnitude10plus;break;
        }


        int colorMag = getContext().getColor(backColor);
        return colorMag;
    }


    private boolean isContainKm(String location)
    {
        if(location.contains("km"))
            return true;
        return false;
    }

    private String GetLocBottom(String location)
    {
        String[] topLoc = location.split(SEP);
        return topLoc[1];
    }

    private String GetLocTop(String location)
    {
        String[] topLoc = location.split(SEP);
        return topLoc[0];
    }

    private String GetTime1(long date)
    {
        Date timeObject = new Date(date);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String timeToDisplay = timeFormat.format(timeObject);

        return timeToDisplay;
    }

    private String GetDate1(long date)
    {
        Date dateObject = new Date(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM, DD, yyyy");
        String dateToDisplay = dateFormat.format(dateObject);

        return dateToDisplay;
    }
}
