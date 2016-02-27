package com.gotraveling.insthub.gps.view;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gotraveling.external.activeandroid.util.Log;
import com.gotraveling.insthub.BeeFramework.fragment.BaseFragment;
import com.gotraveling.insthub.gps.utils.NaviPrefs;
import com.gotraveling.insthub.gps.utils.PreferencesUtils;
import com.gotraveling.insthub.gps.view.ArcMenu.OnMenuItemClickListener;
import com.insthub.ecmobile.R;

/**
 * Created by ChaoMing on 2015/4/22.
 */
public class GPSFragment extends BaseFragment implements DataFlush,
		OnMenuItemClickListener, OnClickListener {
	protected GpsValueView mGpsTime = null;
	protected GpsValueView mLon = null;
	protected GpsValueView mSati = null;
	protected GpsValueView mAltitude = null;
	protected GpsValueView mLatitude = null;
	protected GpsValueView mGpsStatus = null;
	protected GpsValueView mAccuracy = null;
	protected GpsValueView mSpeed = null;
	protected String[] mFixName = null;
	protected String[] mSigName = null;
	private ArcMenu mArcMenu;
	private LinearLayout gps_linearlayout;
	protected SatelliteSnrView mSateView = null;

	private Thread thread;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_gps, container, false);

		mSateView = (SatelliteSnrView) layout.findViewById(R.id.sate_snr);

		mGpsTime = (GpsValueView) layout.findViewById(R.id.txtGpsTime);
		mLon = (GpsValueView) layout.findViewById(R.id.txtLongitude);
		mSati = (GpsValueView) layout.findViewById(R.id.txtSati);
		mAltitude = (GpsValueView) layout.findViewById(R.id.txtAltitude);
		mLatitude = (GpsValueView) layout.findViewById(R.id.txtLatitude);
		mGpsStatus = (GpsValueView) layout.findViewById(R.id.txtGpsStatus);
		mAccuracy = (GpsValueView) layout.findViewById(R.id.txtAccuracy);
		mSpeed = (GpsValueView) layout.findViewById(R.id.txtSpeed);

		mArcMenu = (ArcMenu) layout.findViewById(R.id.id_menu);
		mArcMenu.setOnMenuItemClickListener(this);

		gps_linearlayout = (LinearLayout) layout
				.findViewById(R.id.gps_linearlayout);
		gps_linearlayout.setOnClickListener(this);

		Resources rs = getResources();
		mFixName = rs.getStringArray(R.array.fix_name);
		mSigName = rs.getStringArray(R.array.sig_name);

		PreferencesUtils pre = new PreferencesUtils(getActivity());
		NaviPrefs.mFloatShow = pre.getBoolean("FloatShow", false);
		onDataChange("");
		return layout;
	}

	protected String Status(int sig, int fix) {// 状态
		String text;
		// 0=未定位，1=非差分定位，2=差分定位，6=正在估算
		if (sig >= mSigName.length)
			sig = 4;
		text = mSigName[sig];
		if (sig == 1) {
			// 1 = 未定位， 2 = 二维定位， 3 = 三维定位。
			if (fix >= mFixName.length)
				fix = 0;
			text = mFixName[fix];
		}
		return text;
	}

	@Override
	public void onDataChange(String act) {
		if (mSateView != null)
			mSateView.invalidate();

		String tmp = TrackService.mInfo.Longitude();
		if (mLon != null)
			mLon.setValue(tmp);
		if (mLatitude != null)
			mLatitude.setValue(TrackService.mInfo.Latitude());
		if (mAltitude != null)
			mAltitude.setValue(TrackService.mInfo.Altitude());
		if (mAccuracy != null)
			mAccuracy.setValue(TrackService.mInfo.DOP());
		if (mSati != null)
			mSati.setValue(TrackService.mInfo.Satellites());
		if (mGpsStatus != null)
			mGpsStatus.setValue(Status(TrackService.mInfo.sig,
					TrackService.mInfo.fix));

		if (mGpsTime != null)
			mGpsTime.setValue(TrackService.mInfo.GpsTime());
		if (mSpeed != null)
			mSpeed.setValue(TrackService.mCourse.CurSpeed());
	}

	@Override
	public void onSelected() {
		onDataChange("");
	}

	@Override
	public void onResume() {
		super.onResume();
		onDataChange("");
	}

	@Override
	public void onDestroy() {
		Log.i("NaviFragment.onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onClick(View view, int pos) {

		Activity parent = getActivity();

		switch (pos) {
		case 1:
			if (TrackService.mOpened) {
				TrackService.FNotify(parent);
				Toast.makeText(getActivity(), "已关闭GPS", Toast.LENGTH_SHORT)
						.show();
			} else {
				TrackService.Notify(parent, TrackService.Cmd_Start);
				Toast.makeText(getActivity(), "已开启GPS", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case 2:
			if (NaviPrefs.mFloatShow) {
				com.gotraveling.insthub.gps.utils.Log.i("NaviPrefs.mFloatShow ---> true 2 false");
				FloatWindowService.FNotify(parent);
//				FloatWindowService.Notify(parent, FloatWindowService.Cmd_Start);
				NaviPrefs.mFloatShow = false;
				Toast.makeText(getActivity(), "已关闭浮窗", Toast.LENGTH_SHORT)
						.show();
			} else {
				com.gotraveling.insthub.gps.utils.Log.i("NaviPrefs.mFloatShow ---> false 2 true");
				FloatWindowService.Notify(parent, FloatWindowService.Cmd_Start);
				Toast.makeText(getActivity(), "已开启浮窗", Toast.LENGTH_SHORT)
						.show();
				NaviPrefs.mFloatShow = true;
			}
//			PreferencesUtils pre = new PreferencesUtils(getActivity());
//			pre.putBoolean("MinStart", NaviPrefs.mFloatShow);
			break;
		case 3:
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(320);
					} catch (InterruptedException e) {
						Toast.makeText(getActivity(), "抱歉，Activity无法启动！",
								Toast.LENGTH_SHORT).show();
					}
					Intent intent = new Intent();
					intent.setClass(getActivity(), SatelliteActivity.class);
					startActivity(intent);
				}
			});
			thread.start();
			break;
		case 4:
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(320);
					} catch (InterruptedException e) {
						Toast.makeText(getActivity(), "抱歉，Activity无法启动！",
								Toast.LENGTH_SHORT).show();
					}
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							LocationActivity.class);
					startActivity(intent);
				}
			});
			thread.start();
			break;
		case 5:
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(320);
					} catch (InterruptedException e) {
						Toast.makeText(getActivity(), "抱歉，Activity无法启动！",
								Toast.LENGTH_SHORT).show();
					}
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							SettingActivity.class);
					startActivity(intent);
				}
			});
			thread.start();
			break;
		case 6:
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(320);
					} catch (InterruptedException e) {
						Toast.makeText(getActivity(), "抱歉，Activity无法启动！",
								Toast.LENGTH_SHORT).show();
					}
					Intent intent = new Intent();
					intent.setClass(getActivity(),
							CompassActivity.class);
					startActivity(intent);
				}
			});
			thread.start();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (mArcMenu.isOpen())
			mArcMenu.toggleMenu(300);
	}
}
