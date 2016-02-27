package com.gotraveling.insthub.gps.utils;

import android.content.Context;

import com.gotraveling.external.activeandroid.util.Log;

/**
 * Created by ChaoMing on 2015/4/24.
 */
public class FloatPrefs {
    public	static int mFloatX;
    public	static int mFloatY;
    public	static int mFloatMode;
    //public	static int mFloatShow;

    public static int FloatBackground = 5;
    public static int  FloatStyle = 2;
    public static int FloatTextColor = 2;
    protected static final int textcolor[] = {
            0xffffffff, 0xff000000, 0xffff0000, 0xffffff00, 0xff0000ff, 0xff00ff00
    };
    public static void Load(Context ct, int screenHeight){
    	Log.i("screenHeight:" + screenHeight);
        PreferencesUtils pf = new PreferencesUtils(ct);
        mFloatX = pf.getInt("FloatX",0);
        mFloatY = pf.getInt("FloatY", screenHeight * (3/4));
        mFloatMode = pf.getInt("FloatMode",0);

        //mFloatAlter = pf.getInt("FloatAlter",2);
        FloatBackground = pf.getInt("FloatBackground",5);
        FloatStyle = pf.getInt("FloatStyle",2);
        int k = pf.getInt("FloatTextColor",2);
        if( k<0 ) k=0;
        if( k>5 ) k=5;
        FloatTextColor = textcolor[k];
    }
    
    public static void Load(Context ct){
    	Load(ct, 0);
    }

    public static void SaveData(Context ct){
        PreferencesUtils pf = new PreferencesUtils(ct);
        pf.putInt("FloatX",mFloatX);
        pf.putInt("FloatY",mFloatY);
    }
}
