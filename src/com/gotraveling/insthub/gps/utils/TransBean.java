package com.gotraveling.insthub.gps.utils;

import android.content.Context;

/**
 * Created by ChaoMing on 2015/4/24.
 */
public class TransBean {
    public	boolean mIngorBad;
    public	int mMinDst;
    public	boolean mUseLocal;
    public	boolean mIngorTrk;

    public void Load(Context ct){
        PreferencesUtils pf = new PreferencesUtils(ct);
        mIngorBad = pf.getBoolean("IngorBad", true);
        mMinDst = pf.getInt("MinDst", 0);
        mUseLocal = pf.getBoolean("UseLocal", false);
        mIngorTrk = pf.getBoolean("IngorTrk",false);
    }
    public void Save(Context ct){
        PreferencesUtils pf = new PreferencesUtils(ct);
        pf.putBoolean("IngorBad", mIngorBad);
        pf.putInt("MinDst", mMinDst);
        pf.putBoolean("UseLocal", mUseLocal);
        pf.putBoolean("IngorTrk", mIngorTrk);
    }
}
