package com.gotraveling.insthub.gps.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * PreferencesUtils, easy to get or put data
 * <ul>
 * <strong>Put Value</strong>
 * <li>put string {@link #putString( String, String)}</li>
 * <li>put int {@link #putInt( String, int)}</li>
 * <li>put long {@link #putLong( String, long)}</li>
 * <li>put float {@link #putFloat( String, float)}</li>
 * <li>put boolean {@link #putBoolean( String, boolean)}</li>
 * </ul>
 * <ul>
 * <strong>Get Value</strong>
 * <li>get string {@link #getString( String)}, {@link #getString( String, String)}</li>
 * <li>get int {@link #getInt( String)}, {@link #getInt( String, int)}</li>
 * <li>get long {@link #getLong( String)}, {@link #getLong(, String, long)}</li>
 * <li>get float {@link #getFloat( String)}, {@link #getFloat( String, float)}</li>
 * <li>get boolean {@link #getBoolean( String)}, {@link #getBoolean( String, boolean)}</li>
 * </ul>
 * 
 * @author ChaoMing
 */
public class PreferencesUtils {

    private SharedPreferences settings;

    /**
     *  open default preferences to get or put data,normal it is  /data/data/packet_name/shared_prefs/packet_name_preferences.xml
     * @param context
     */
    public  PreferencesUtils(Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     *  open a named preferences to get or put data
     * @param context
     * @param name preferences name
     */
    public PreferencesUtils(Context context,String name){
        settings = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
    /**
     * put string preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public  boolean putString( String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * get string preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or null. Throws ClassCastException if there is a preference with this
     *         name that is not a string
     * @see #getString( String, String)
     */
    public  String getString(String key) {
        return getString(key, null);
    }

    /**
     * get string preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a string
     */
    public  String getString(String key, String defaultValue) {
        return settings.getString(key, defaultValue);
    }

    /**
     * put int preferences
     * 
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public  boolean putInt(String key, int value) {
        //SharedPreferences.Editor editor = settings.edit();
        //editor.putInt(key, value);
        //return editor.commit();
        return putString(key,Integer.toString(value));
    }

    /**
     * get int preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a int
     * @see #getInt( String, int)
     */
    public  int getInt( String key) {
        return getInt(key, -1);
    }

    /**
     * get int preferences
     * 
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a int
     */
    public  int getInt( String key, int defaultValue) {
        String tmp  = settings.getString(key, "");
        if( tmp.isEmpty() ) return defaultValue;
        return Integer.parseInt(tmp);
    }

    /**
     * put long preferences
     * 
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public  boolean putLong( String key, long value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    /**
     * get long preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a long
     * @see #getLong(String, long)
     */
    public  long getLong( String key) {
        return getLong(key, -1);
    }

    /**
     * get long preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a long
     */
    public  long getLong(String key, long defaultValue) {
        String tmp = getString(key,"");
        if( tmp.isEmpty() ) return defaultValue;
        return Long.parseLong(tmp);
    }

    /**
     * put float preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public  boolean putFloat( String key, float value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * get float preferences
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
     *         name that is not a float
     * @see #getFloat( String, float)
     */
    public  float getFloat( String key) {
        return getFloat(key, -1);
    }

    /**
     * get float preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a float
     */
    public  float getFloat(  String key, float defaultValue) {
        String tmp = getString(key,"");
        if( tmp.isEmpty() ) return defaultValue;
        return Float.parseFloat(tmp);
    }
    public double getDouble(String key ){
        return getDouble(key, 0.000);
    }
    public double getDouble(String key, double  f){
        try {
            return Double.parseDouble(settings.getString(key, ""));
        }catch(NumberFormatException e){
            return f;
        }
    }
    public boolean putDouble(String key,double value){
        //SharedPreferences.Editor editor = settings.edit();
        //editor.putString(key, String.valueOf(value));
        //return editor.commit();
        return putString(key,Double.toString(value));
    }
    /**
     * put boolean preferences
     *
     * @param key The name of the preference to modify
     * @param value The new value for the preference
     * @return True if the new values were successfully written to persistent storage.
     */
    public  boolean putBoolean( String key, boolean value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * get boolean preferences, default is false
     *
     * @param key The name of the preference to retrieve
     * @return The preference value if it exists, or false. Throws ClassCastException if there is a preference with this
     *         name that is not a boolean
     * @see #getBoolean(String, boolean)
     */
    public  boolean getBoolean(String key) {
        return getBoolean( key, false);
    }

    /**
     * get boolean preferences
     *
     * @param key The name of the preference to retrieve
     * @param defaultValue Value to return if this preference does not exist
     * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
     *         this name that is not a boolean
     */
    public  boolean getBoolean( String key, boolean defaultValue) {
        return settings.getBoolean(key, defaultValue);
    }
}
