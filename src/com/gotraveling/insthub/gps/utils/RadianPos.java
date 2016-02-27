package com.gotraveling.insthub.gps.utils;

import android.location.Location;

/**
 * Created by ChaoMing on 2015/4/16.
 */
public class RadianPos {
    public double lat = 0.0;         /**< Latitude */
    public double lon = 0.0;         /**< Longitude */
    public RadianPos(){

    }
    public RadianPos(GpsInfo info){
        set(info);
    }
    public void zero(){
        lat=lon=0.00;
    }
    public static RadianPos Get(GpsInfo info){
        return new RadianPos(info);
    }
    public void set(GpsInfo info){
        //degree2ndeg
        //ndeg2radian
        lat = GpsMath.degree2radian(info.lat);
        lon = GpsMath.degree2radian(info.lon);
    }
    /**< To position in radians */
    public double nmea_distance(  RadianPos to_pos )
    {
        double dist = (GpsMath.NMEA_EARTHRADIUS_M) * Math.acos(
                Math.sin(to_pos.lat) * Math.sin(lat) +
                        Math.cos(to_pos.lat) * Math.cos(lat) * Math.cos(to_pos.lon - lon)
        );
        return dist;
    }
    /**
     * brief Calculate distance between two points
     * return Distance in meters
     * @param  to_pos To position in radians
     */
    public  double distance( RadianPos to_pos  )
    {
        float results[] = new float[3];
        Location.distanceBetween(lat,lon,to_pos.lat,to_pos.lon,results);
        return Math.abs(results[0]);
    }

}
