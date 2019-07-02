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

    /**
     * @return location where the current earthquake has occured
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * @return Magnitude of the current earthquake has occured
     */
    public double getMagnitude()
    {
        return magnitude;
    }

    /**
     * @return date and time when the current earthquake occured
     */
    public long getDate()
    {
        return date;
    }

    /**
     * @return returns the url
     */
    public String getUrl()
    {
        return urlSite;
    }
}