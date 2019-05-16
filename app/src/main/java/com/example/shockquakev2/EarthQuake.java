package com.example.shockquakev2;

public class EarthQuake
{
    String location, urlSite ;
    long date;
    Double magnitude;

    public EarthQuake(Double magnitude, String location, long date, String urlSite)
    {
        this.magnitude =magnitude;
        this.location = location;
        this.date = date;
        this.urlSite = urlSite;
    }


    public String getLocation()
    {
        return location;
    }

    public double getMagnitude()
    {
        return magnitude;
    }

    public long getDate()
    {
        return date;
    }

    public String getUrl()
    {
        return urlSite;
    }
}