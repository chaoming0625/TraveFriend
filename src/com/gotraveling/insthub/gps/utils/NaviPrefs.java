package com.gotraveling.insthub.gps.utils;

import java.io.File;

import android.content.Context;

/**
 * Created by ChaoMing on 2015/4/24.
 */
public class NaviPrefs {
	public static double[] SpeedMultiplier = { 1.0,/* 公里/小时 */1.0 / 1.852,/* 节 */
		1.0 / 1.609344,/* 英里/小时 */1.0 / 3.6 /* 米/秒 */};
	public static double[] DistanceMultiplier = { 1.0,/* 公里 */1000,/* 米; */
		1.0 / 1.609344,/* 英里; */1.0 / 1.852,/* 海里; */1.0 / 0.0009144 /* 码 */};
	public static double[] ElevationMultiplier = { 0.001,/* 公里; */1.000,/* 米; */
		1.0 / 0.3048,/* 英尺; */1.0 / 0.9144f /* 码 */};
	public static boolean mAutoStart;// 自动开始
	// public static boolean mMinStart;
	public static boolean mFloatShow;
	public static int mLogMode;// log模式
	public static boolean mLogToSD;
	public static String mLogName;
	// public static boolean mVerbose;//详细模式
	// public static boolean mLogNMEA;//是否记录NMEA
	protected static String mBasePath;

	public static int mMinDistance;// 记录最小距离
	public static int mInvalidPos;// 无效位置最大个数
	public static int mUnitSpeed;// 显示速度单位
	public static int mUnitDistance;// 显示距离单位
	public static int mUnitAltitude;// 显示海拔单位
	public static int mRecordRate;// 记录速度，

	public static double mTotalCourse;
	public static int mTotalTicks;

	public static String GetBasePath() {
		return mBasePath;
	}

	public static String GetDataPath() {
		return mBasePath + "data" + File.separator;
	}

	public static String GetExportPath() {
		return mBasePath + "export" + File.separator;
	}

	protected static void MakeSureConf() {
		if (mLogToSD)
			mBasePath = FileUtils.getSDCardPath();
		else
			mBasePath = FileUtils.getNormalSDCardPath();
		mBasePath += File.separator;
		mBasePath = mBasePath + "LogGPS" + File.separator;
		FileUtils.makeDirs(GetDataPath());
		FileUtils.makeDirs(GetExportPath());
		Log.i("NaviPrefs(): LogMode=" + mLogMode + ", set data path to "
				+ GetDataPath());
	}

	public static void Load(Context ct) {
		PreferencesUtils pf = new PreferencesUtils(ct);
		mAutoStart = pf.getBoolean("AutoStart", false);
		// mMinStart = pf.getBoolean("MinStart", false);
		mFloatShow = pf.getBoolean("FloatShow", false);
		mLogMode = pf.getInt("LogMode", 0);
		// mVerbose = pf.getBoolean("Verbose", false);
		// mLogNMEA = pf.getBoolean("LogNMEA",false);
		mLogToSD = pf.getBoolean("LogToSD", false);
		mLogName = pf.getString("LogName", "");

		mMinDistance = pf.getInt("MinDistance", 0);
		mInvalidPos = pf.getInt("InvalidPos", 0);
		mUnitSpeed = pf.getInt("UnitSpeed", 0);
		mUnitDistance = pf.getInt("UnitDistance", 0);
		mUnitAltitude = pf.getInt("UnitAltitude", 1);
		mRecordRate = pf.getInt("RecordRate", 1);

		MakeSureConf();
	}

	public static void LoadData(Context ct) {
		PreferencesUtils pf = new PreferencesUtils(ct);
		// 里程
		mTotalCourse = pf.getDouble("mTotalCourse");
		mTotalTicks = pf.getInt("TotalTicks", 0);
	}

	public static void Save(Context ct) {
		PreferencesUtils pf = new PreferencesUtils(ct);
		pf.putBoolean("AutoStart", mAutoStart);
		pf.putBoolean("FloatShow", mFloatShow);
		pf.putInt("LogMode", mLogMode);
		// pf.putBoolean("Verbose", mVerbose);
		// pf.putBoolean("LogNMEA",mLogNMEA);
		pf.putBoolean("LogToSD", mLogToSD);
		pf.putString("LogNam", mLogName);
		pf.putInt("MinDistance", mMinDistance);
		pf.putInt("InvalidPos", mInvalidPos);
		pf.putInt("UnitSpeed", mUnitSpeed);
		pf.putInt("UnitDistance", mUnitDistance);
		pf.putInt("UnitAltitude", mUnitAltitude);
		pf.putInt("RecordRate", mRecordRate);
	}

	public static void SaveData(Context ct) {
		PreferencesUtils pf = new PreferencesUtils(ct);
		pf.putDouble("mTotalCourse", mTotalCourse);
		pf.putInt("TotalTicks", mTotalTicks);
	}
}
